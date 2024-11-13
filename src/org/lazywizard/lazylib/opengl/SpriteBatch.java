package org.lazywizard.lazylib.opengl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import com.fs.starfarer.api.graphics.SpriteAPI;
import static org.lwjgl.opengl.GL11.*;

/**
 * Used to efficiently render the same sprite many times. Each instance only
 * handles one unique sprite, and does not modify the underlying
 * {@link SpriteAPI}. All scaling is based on the state of the {@link SpriteAPI}
 * at the time of {@code SpriteBatch} instantiation.
 *
 * @author LazyWizard
 * @since 3.0
 */
// TODO: Javadoc all methods
// TODO: Rewrite to handle multiple SpriteAPIs
// TODO: Rewrite to use buffers (clone of DrawQueue?)
// TODO: Remove all references to the radar mod
public class SpriteBatch
{
    //private static final Logger Log = Logger.getLogger(SpriteBatch.class);
    private static final boolean DEBUG_MODE = false;
    private final int textureId, blendSrc, blendDest;
    private final float textureWidth, textureHeight, offsetScaleX, offsetScaleY, hScale;
    private final List<DrawCall> toDraw = new ArrayList<>();
    private boolean finished = false;

    public SpriteBatch(SpriteAPI sprite)
    {
        this(sprite, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public SpriteBatch(SpriteAPI sprite, int blendSrc, int blendDest)
    {
        this.blendSrc = blendSrc;
        this.blendDest = blendDest;
        textureId = sprite.getTextureId();
        textureWidth = sprite.getTextureWidth();
        textureHeight = sprite.getTextureHeight();
        hScale = sprite.getWidth() / sprite.getHeight();
        offsetScaleX = (sprite.getCenterX() > 0f
                ? sprite.getCenterX() / (sprite.getWidth() * .5f) : 1f);
        offsetScaleY = (sprite.getCenterY() > 0f
                ? sprite.getCenterY() / (sprite.getHeight() * .5f) : 1f);
    }

    // Size is height of sprite, width is automatically calculated
    public void add(float x, float y, float angle, float size, Color color, float alphaMod)
    {
        add(x, y, angle, size * hScale, size, color, alphaMod);
    }

    public void add(float x, float y, float angle, float width, float height, Color color, float alphaMod)
    {
        if (finished)
        {
            clear();
        }

        toDraw.add(new DrawCall(x, y, angle, width, height, color, alphaMod));
        finished = false;
    }

    public int size()
    {
        return toDraw.size();
    }

    public void clear()
    {
        toDraw.clear();
        finished = false;
    }

    public boolean isEmpty()
    {
        return toDraw.isEmpty();
    }

    // Does nothing for now, usage is enforced for later move to buffers
    public void finish()
    {
        if (finished)
        {
            throw new RuntimeException("SpriteBatch is already finished!");
        }

        finished = true;
    }

    public void draw()
    {
        if (!finished)
        {
            throw new RuntimeException("Must call finish() before drawing!");
        }

        if (toDraw.isEmpty())
        {
            return;
        }

        glBlendFunc(blendSrc, blendDest);
        glBindTexture(GL_TEXTURE_2D, textureId);
        for (DrawCall call : toDraw)
        {
            glPushMatrix();

            if (DEBUG_MODE)
            {
                glDisable(GL_TEXTURE_2D);
                glPointSize(3f);
                glColor4f(1f, 1f, 1f, 1f);
                glBegin(GL_POINTS);
                glVertex2f(call.x, call.y);
                glEnd();
                glEnable(GL_TEXTURE_2D);
            }

            glTranslatef(call.x, call.y, 0f);
            glRotatef(call.angle, 0f, 0f, 1f);
            glTranslatef((-call.width * 0.5f) * offsetScaleX, (-call.height * 0.5f) * offsetScaleY, 0f);

            glColor4ub(call.color[0], call.color[1], call.color[2], call.color[3]);
            glBegin(GL_QUADS);
            glTexCoord2f(0f, 0f);
            glVertex2f(0f, 0f);
            glTexCoord2f(textureWidth, 0f);
            glVertex2f(call.width, 0f);
            glTexCoord2f(textureWidth, textureHeight);
            glVertex2f(call.width, call.height);
            glTexCoord2f(0f, textureHeight);
            glVertex2f(0f, call.height);
            glEnd();
            glPopMatrix();
        }
    }

    private static class DrawCall
    {
        private final float x, y, angle, width, height;
        private final byte[] color;

        private static byte[] getColorBytes(Color color, float alphaMod)
        {
            final int value = color.getRGB();
            return new byte[]
            {
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF),
                (byte) (Math.round((value >> 24) & 0xFF) * alphaMod)
            };
        }

        private DrawCall(float x, float y, float angle, float width, float height,
                Color color, float alphaMod)
        {
            this.x = x;
            this.y = y;
            this.angle = angle - 90f;
            this.width = width;
            this.height = height;
            this.color = getColorBytes(color, alphaMod);
        }
    }
}
