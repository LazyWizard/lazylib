package org.lazywizard.lazylib.opengl;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * A class to simplify multi-shape primitive rendering by keeping track of draw
 * modes and vertex/color buffers for you and drawing in an efficient way. It
 * uses vertex buffer objects (VBOs), but will fall back to passing in the data
 * from main memory every frame in the unlikely event that the user's card
 * doesn't support OpenGL 1.5.
 * <p>
 * Usage instructions:
 * <p>
 * Step 1: Create a DrawQueue. This only has to be done once, as the same
 * DrawQueue can be reused an infinite amount of times. Make sure to pass in as
 * high an initial capacity as you think you will use, as when this capacity is
 * exceeded new native buffers will have to be allocated, which is a relatively
 * expensive operation.
 * <p>
 * Step 2: Call {@link DrawQueue#setNextColor(java.awt.Color, float)} to set the
 * color of the following vertices. If you don't call this the DrawQueue will
 * use a default color of solid white, or whatever was used previously if you
 * are reusing an existing DrawQueue.
 * <p>
 * Step 3: Add the vertices of your shape with
 * {@link DrawQueue#addVertices(float[])}. You can change the color again
 * between sets of vertices.
 * <p>
 * Step 4: Once you are done setting up a shape call
 * {@link DrawQueue#finishShape(int)} with the draw mode that shape should use.
 * You <i>must</i> finish a shape after adding all vertices or you will
 * encounter graphical errors!
 * <p>
 * Step 5: Repeat steps 2-4 for each shape you wish to draw. Once all shapes
 * have been added to the DrawQueue, call {@link DrawQueue#finish()} to
 * finalize the contents and ready it for drawing.
 * <p>
 * Step 6: Call {@link DrawQueue#draw()} to draw the DrawQueue's contents. The
 * OpenGL client states {@link GL11#GL_VERTEX_ARRAY} and
 * {@link GL11#GL_COLOR_ARRAY} must be enabled for this method to function
 * correctly. You can call this method as many times as you want.
 * <p>
 * Step 7: When you need to reuse a DrawQueue, just return to Step 2. After
 * finishing a DrawQueue it is ready for writing again.
 *
 * @author LazyWizard
 * @since 3.0
 */
// TODO: Implement interleavened VBO for maximum efficiency
// TODO: Remove all references to the radar mod
public class DrawQueue
{
    private static final boolean USE_VBOS = GLContext.getCapabilities().OpenGL15;
    private static final Logger Log = Global.getLogger(DrawQueue.class);
    private static final int SIZEOF_VERTEX = 2, SIZEOF_COLOR = 4,
            STRIDE_VERTEX = 8, STRIDE_COLOR = 4;
    private static final Map<WeakReference<DrawQueue>, IntBuffer> refs = new LinkedHashMap<>();
    private final byte[] currentColor = new byte[]
            {
                    Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE
            };
    private final List<BatchMarker> batchMarkers = new ArrayList<>();
    private final int vertexId, colorId, drawFlag;
    private ByteBuffer vertexMap, colorMap;
    private boolean finished = false;

    /**
     * Releases the vertex and color buffers of all DrawQueues that have been
     * garbage collected. Necessary due to the lack of a callback at the end of
     * a combat/campaign scenario. This is called internally by the mod, so you
     * should never need to call it yourself.
     * <p>
     *
     * @since 3.0
     */
    public static void releaseDeadQueues()
    {
        // Check if any of our old DrawQueues are ready for garbage collection
        // If so, ensure we release their allocated buffers from the graphics card
        int totalReleased = 0;
        for (Iterator<Map.Entry<WeakReference<DrawQueue>, IntBuffer>> iter
             = refs.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry<WeakReference<DrawQueue>, IntBuffer> entry = iter.next();
            if (entry.getKey().get() == null)
            {
                totalReleased++;
                glDeleteBuffers(entry.getValue());
                iter.remove();
            }
        }

        if (totalReleased > 0)
        {
            Log.debug("Released buffers of " + totalReleased + " dead DrawQueues");
        }
    }

    /**
     * Creates a new auto-resizing DrawQueue with the draw flag
     * {@link GL15#GL_DYNAMIC_DRAW}.
     * <p>
     *
     * @param initialCapacity The initial maximum number of vertices this
     *                        DrawQueue should hold, used for allocating native
     *                        buffers of the proper size. If this capacity is
     *                        exceeded new native buffers of the proper size
     *                        will be allocated automatically. Resizing is a
     *                        relatively expensive operation, so you should try
     *                        to set this to the maximum number of vertices you
     *                        expect the DrawQueue to hold over its lifetime.
     *                        <p>
     *
     * @since 3.0
     */
    public DrawQueue(int initialCapacity)
    {
        this(initialCapacity, GL_DYNAMIC_DRAW);
    }

    /**
     * Creates a new auto-resizing DrawQueue.
     * <p>
     *
     * @param initialCapacity The initial maximum number of vertices this
     *                        DrawQueue should hold, used for allocating native
     *                        buffers of the proper size. If this capacity is
     *                        exceeded new native buffers of the proper size
     *                        will be allocated automatically. Resizing is a
     *                        relatively expensive operation, so you should try
     *                        to set this to the maximum number of vertices you
     *                        expect the DrawQueue to hold over its lifetime.
     * @param drawFlag        The buffer data stream type, only used if VBOs are
     *                        active. Default is {@link GL15#GL_DYNAMIC_DRAW}.
     *                        <p>
     *
     * @since 3.0
     */
    public DrawQueue(int initialCapacity, int drawFlag)
    {
        // If using vertex buffer objects, allocate buffer space on the graphics card
        if (USE_VBOS)
        {
            final IntBuffer ids = BufferUtils.createIntBuffer(2);
            glGenBuffers(ids);
            vertexId = ids.get(0);
            colorId = ids.get(1);
            refs.put(new WeakReference(this), ids);
        }
        else
        {
            vertexId = 0;
            colorId = 0;
        }

        // Allocate native buffers
        vertexMap = BufferUtils.createByteBuffer(initialCapacity * STRIDE_VERTEX);
        colorMap = BufferUtils.createByteBuffer(initialCapacity * STRIDE_COLOR);
        this.drawFlag = drawFlag;
    }

    private void resize(int newCapacity)
    {
        // Ensure that the data is ready for writing again
        if (!finished)
        {
            vertexMap.flip();
            colorMap.flip();
        }

        // Allocate new buffers of the required size and transfer the existing data to them
        Log.debug("Resizing to " + newCapacity + " vertices");
        vertexMap = BufferUtils.createByteBuffer(newCapacity * STRIDE_VERTEX).put(vertexMap);
        colorMap = BufferUtils.createByteBuffer(newCapacity * STRIDE_COLOR).put(colorMap);
        finished = false;
    }

    /**
     * Clears all data from the DrawQueue.
     * <p>
     *
     * @since 3.0
     */
    public void clear()
    {
        vertexMap.clear();
        colorMap.clear();
        batchMarkers.clear();
        finished = false;
    }

    /**
     * Checks whether this DrawQueue contains any vertex data yet.
     *
     * @return {@code true} if no vertices have been added to the DrawQueue,
     *         {@code false} otherwise.
     *         <p>
     *
     * @since 3.0
     */
    public boolean isEmpty()
    {
        return vertexMap.limit() == 0;
    }

    /**
     * Sets the color of any vertices added after this method is called.
     * <p>
     *
     * @param color    The color of the next shape. All vertices added until
     *                 this method is called again will use this color.
     * @param alphaMod Multiplies {@code color}'s alpha channel by this number
     *                 (should be between 0 and 1). You will usually pass in
     *                 {@link CommonRadar#getContactAlpha()} for contacts, or
     *                 {@link CommonRadar#getRadarAlpha()} for UI elements. Also
     *                 useful to avoid creating a new Color object every frame
     *                 just to add fade effects.
     *                 <p>
     *
     * @since 3.0
     */
    public void setNextColor(Color color, float alphaMod)
    {
        // Convert the color data to a byte array
        currentColor[0] = (byte) color.getRed();
        currentColor[1] = (byte) color.getGreen();
        currentColor[2] = (byte) color.getBlue();
        currentColor[3] = (byte) Math.round(color.getAlpha() * alphaMod);
    }

    /**
     * Sets the color of any vertices added after this method is called. Color
     * values must be in the [0..1] range.
     * <p>
     *
     * @param red   The red channel value of the color you wish to use (should
     *              be between 0 and 1).
     * @param green The green channel value of the color you wish to use (should
     *              be between 0 and 1).
     * @param blue  The blue channel value of the color you wish to use (should
     *              be between 0 and 1).
     * @param alpha The alpha channel value of the color you wish to use (should
     *              be between 0 and 1).
     *              <p>
     *
     * @since 3.0
     */
    public void setNextColor(float red, float green, float blue, float alpha)
    {
        currentColor[0] = (byte) Math.round(red * 255f);
        currentColor[1] = (byte) Math.round(green * 255f);
        currentColor[2] = (byte) Math.round(blue * 255f);
        currentColor[3] = (byte) Math.round(alpha * 255f);
    }

    /**
     * Sets the color of any vertices added after this method is called. Color
     * values must be in the [0..255] range.
     * <p>
     *
     * @param red   The red channel value of the color you wish to use (should
     *              be between 0 and 255).
     * @param green The green channel value of the color you wish to use (should
     *              be between 0 and 255).
     * @param blue  The blue channel value of the color you wish to use (should
     *              be between 0 and 255).
     * @param alpha The alpha channel value of the color you wish to use (should
     *              be between 0 and 255).
     *              <p>
     *
     * @since 3.0
     */
    public void setNextColor(int red, int green, int blue, int alpha)
    {
        currentColor[0] = (byte) red;
        currentColor[1] = (byte) green;
        currentColor[2] = (byte) blue;
        currentColor[3] = (byte) alpha;
    }

    /**
     * Add vertex data to the current shape. If called on a finished DrawQueue,
     * this will reset it and start a new set of vertex data.
     * <p>
     *
     * @param vertices The vertex x,y pairs to be added.
     *                 <p>
     *
     * @return {@code true} if the DrawQueue had to resize to fit
     *         {@code vertices}, {@code false} otherwise.
     *         <p>
     *
     * @since 3.0
     */
    public boolean addVertices(float[] vertices)
    {
        // Ensure the vertex array has an even number of floats
        if ((vertices.length & 1) != 0)
        {
            throw new RuntimeException("Vertices must be added in pairs!");
        }

        // If this is a new set of data, clear out the old data first
        if (finished)
        {
            clear();
        }

        // Ensure we have space remaining (and resize if we don't)
        boolean resized = false;
        final int requiredCapacity = vertexMap.position() + (vertices.length * 4);
        if (requiredCapacity > vertexMap.capacity())
        {
            // Resize to 150% of the newly required capacity
            // Odd multiplier is to adjust for byte size and vertex pairs
            resize((int) (requiredCapacity * .1875));
            resized = true;
        }

        // Individual puts are much faster, but won't check limitations on bounds
        for (int x = 0; x < vertices.length; x++)
        {
            vertexMap.putFloat(vertices[x]);

            // Keep color map updated
            if ((x & 1) == 0)
            {
                for (int y = 0; y < currentColor.length; y++)
                {
                    colorMap.put(currentColor[y]);
                }
            }
        }

        finished = false;
        return resized;
    }

    /**
     * Add vertex data to the current shape. If called on a finished DrawQueue,
     * this will reset it and start a new set of vertex data.
     * <p>
     *
     * @param vertices The vertices to be added.
     *                 <p>
     *
     * @return {@code true} if the DrawQueue had to resize to fit
     *         {@code vertices}, {@code false} otherwise.
     *         <p>
     *
     * @since 3.0
     */
    public boolean addVertices(List<Vector2f> vertices)
    {
        // Convert to a raw float array and pass to the real implementation
        // Lazy and inefficient, yes, but much easier to maintain
        float[] rawVertices = new float[vertices.size() * 2];
        for (int x = 0; x < vertices.size() * 2; x += 2)
        {
            final Vector2f vertex = vertices.get(x / 2);
            rawVertices[x] = vertex.x;
            rawVertices[x + 1] = vertex.y;
        }

        return addVertices(rawVertices);
    }

    /**
     * Add a single vertex to the current shape. If called on a finished
     * DrawQueue, this will reset it and start a new set of vertex data.
     * <p>
     *
     * @param x The x coordinate of the vertex to be added.
     * @param y The y coordinate of the vertex to be added.
     *          <p>
     *
     * @return {@code true} if the DrawQueue had to resize to fit
     *         {@code vertex}, {@code false} otherwise.
     *         <p>
     *
     * @since 3.0
     */
    public boolean addVertex(float x, float y)
    {
        // If this is a new set of data, clear out the old data first
        if (finished)
        {
            clear();
        }

        // Ensure we have space remaining (and resize if we don't)
        boolean resized = false;
        final int requiredCapacity = vertexMap.position() + STRIDE_VERTEX;
        if (requiredCapacity > vertexMap.capacity())
        {
            // Resize to 150% of the newly required capacity
            // Odd multiplier is to adjust for byte size and vertex pairs
            resize((int) (requiredCapacity * .1875));
            resized = true;
        }

        // Add vertex and color data
        vertexMap.putFloat(x).putFloat(y);

        for (int i = 0; i < currentColor.length; i++)
        {
            colorMap.put(currentColor[i]);
        }

        finished = false;
        return resized;
    }

    /**
     * Add a single vertex to the current shape. If called on a finished
     * DrawQueue, this will reset it and start a new set of vertex data.
     * <p>
     *
     * @param vertex The vertex to be added.
     *               <p>
     *
     * @return {@code true} if the DrawQueue had to resize to fit
     *         {@code vertex}, {@code false} otherwise.
     *         <p>
     *
     * @since 3.0
     */
    public boolean addVertex(Vector2f vertex)
    {
        return addVertex(vertex.x, vertex.y);
    }

    /**
     * Finalizes the current shape. You <i>must</i> call this after every shape,
     * otherwise rendering errors will occur.
     * <p>
     *
     * @param shapeDrawMode The draw mode for this shape (what you would
     *                      normally call with {@link GL11#glBegin(int)}).
     *                      <p>
     *
     * @since 3.0
     */
    public void finishShape(int shapeDrawMode)
    {
        // Keep track of the start/end indices of each shape and how it should be drawn
        batchMarkers.add(new BatchMarker(shapeDrawMode, vertexMap.position() / 8));
    }

    /**
     * Readies the list of shapes for drawing. <i>Must</i> be called before
     * calling {@link DrawQueue#draw()}, otherwise an exception will be thrown!
     * <p>
     * Once this method has been called, any further calls to
     * {@link DrawQueue#addVertices(float[])} will <i>replace</i> the
     * DrawQueue's existing contents, not add to them.
     * <p>
     *
     * @since 3.0
     */
    public void finish()
    {
        if (finished)
        {
            throw new RuntimeException("DrawQueue is already finished!");
        }

        // Prepare our data for reading/later rewriting
        vertexMap.flip();
        colorMap.flip();

        // If we're using vertex buffer objects, send the data to the card now
        if (USE_VBOS)
        {
            // Vertex data
            glBindBuffer(GL_ARRAY_BUFFER, vertexId);
            glBufferData(GL_ARRAY_BUFFER, vertexMap, drawFlag);

            // Color data
            glBindBuffer(GL_ARRAY_BUFFER, colorId);
            glBufferData(GL_ARRAY_BUFFER, colorMap, drawFlag);

            // Release buffer binding
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        finished = true;
    }

    /**
     * Renders all data in the DrawQueue. {@link DrawQueue#finish()} must be
     * called before using this. This method requires OpenGL client states
     * {@link GL11#GL_VERTEX_ARRAY} and {@link GL11#GL_COLOR_ARRAY} to be
     * enabled.
     * <p>
     *
     * @since 3.0
     */
    public void draw()
    {
        if (!finished)
        {
            throw new RuntimeException("Must call finish() before drawing!");
        }

        // Don't draw if there's nothing to render!
        if (isEmpty())
        {
            return;
        }

        // If using vertex buffer objects, draw using the data we already sent to the card
        if (USE_VBOS)
        {
            // Vertex data
            glBindBuffer(GL_ARRAY_BUFFER, vertexId);
            glVertexPointer(SIZEOF_VERTEX, GL_FLOAT, STRIDE_VERTEX, 0);

            // Color data
            glBindBuffer(GL_ARRAY_BUFFER, colorId);
            glColorPointer(SIZEOF_COLOR, GL_UNSIGNED_BYTE, STRIDE_COLOR, 0);

            // Release buffer binding
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        // Otherwise, send the data to the card from main memory every frame :(
        else
        {
            glVertexPointer(SIZEOF_VERTEX, GL_FLOAT, STRIDE_VERTEX, vertexMap);
            glColorPointer(SIZEOF_COLOR, GL_UNSIGNED_BYTE, STRIDE_COLOR, colorMap);
        }

        // Hacky solution for drawing multiple shapes from one buffer
        int lastIndex = 0;
        for (BatchMarker marker : batchMarkers)
        {
            glDrawArrays(marker.drawMode, lastIndex, marker.resetIndex - lastIndex);
            lastIndex = marker.resetIndex;
        }
    }

    // Stores miscellaneous data for each shape to be drawn, currently
    // which draw mode to use and the position in the buffer to draw to
    private record BatchMarker(int drawMode, int resetIndex)
    {
    }
}
