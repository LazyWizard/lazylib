package org.lazywizard.lazylib.opengl;

import org.lazywizard.lazylib.FastTrig;
import static org.lwjgl.opengl.GL11.*;

/**
 * Contains methods to draw primitive 2D shapes using LWJGL/OpenGL.
 *
 * @author LazyWizard
 * @since 1.7
 */
public class DrawUtils
{
    /**
     * Draws a simple circle made of line segments. This method only contains
     * the actual drawing code, and assumes all OpenGL flags, color, line width
     * etc have been set by the user beforehand. Optimized circle-drawing
     * algorithm taken from: http://slabode.exofire.net/circle_draw.shtml
     *
     * @param centerX     The x value of the center point of the circle.
     * @param centerY     The y value of the center point of the circle.
     * @param radius      The radius (in pixels) of the circle to be drawn.
     * @param numSegments How many line segments the circle should be made up
     *                    of (higher number = smoother circle, but higher GPU
     *                    cost).
     * <p>
     * @since 1.7
     */
    public static void drawCircle(float centerX, float centerY,
            float radius, int numSegments)
    {
        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        float theta = 2f * 3.1415926f / (float) numSegments;
        float cos = (float) FastTrig.cos(theta);
        float sin = (float) FastTrig.sin(theta);
        float t;

        // Start at angle = 0
        float x = radius;
        float y = 0;

        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < numSegments; i++)
        {
            // Output vertex
            glVertex2f(x + centerX, y + centerY);

            // Apply the rotation matrix
            t = x;
            x = (cos * x) - (sin * y);
            y = (sin * t) + (cos * y);
        }
        glEnd();
    }

    private DrawUtils()
    {
    }
}
