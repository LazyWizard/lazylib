package org.lazywizard.lazylib.opengl;

import org.lazywizard.lazylib.FastTrig;
import static org.lwjgl.opengl.GL11.*;

/**
 * Contains methods to draw simple 2D shapes using OpenGL primitives.
 * These methods only contain the actual drawing code and assumes all OpenGL
 * flags, color, line width etc have been set by the user beforehand.
 *
 * @author LazyWizard
 * @since 1.7
 */
public class DrawUtils
{
    /**
     * Draws a simple circle made of line segments, or a filled circle if
     * {@code drawFilled} is true.
     * <p>
     * This method only contains the actual drawing code and assumes all OpenGL
     * flags, color, line width etc have been set by the user beforehand.
     * <p>
     * Optimized circle-drawing algorithm based on code taken from:
     * <a href=http://slabode.exofire.net/circle_draw.shtml>
     * http://slabode.exofire.net/circle_draw.shtml</a>
     *
     * @param centerX     The x value of the center point of the circle.
     * @param centerY     The y value of the center point of the circle.
     * @param radius      The radius (in pixels) of the circle to be drawn.
     * @param numSegments How many line segments the circle should be made up
     *                    of (higher number = smoother circle, but higher GPU
     *                    cost).
     * @param drawFilled  Whether the circle should be hollow or filled.
     * <p>
     * @since 1.7
     */
    public static void drawCircle(float centerX, float centerY,
            float radius, int numSegments, boolean drawFilled)
    {
        if (numSegments < 3)
        {
            return;
        }

        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = 2f * 3.1415926f / (float) numSegments;
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);

        // Start at angle = 0
        float x = radius;
        float y = 0;
        float tmp;

        glBegin(drawFilled ? GL_TRIANGLE_FAN : GL_LINE_LOOP);
        for (int i = 0; i < numSegments - 1; i++)
        {
            // Output vertex
            glVertex2f(x + centerX, y + centerY);

            // Apply the rotation matrix
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }
        glVertex2f(x + centerX, y + centerY);
        glEnd();
    }

    // TODO: Javadoc this
    public static void drawEllipse(float centerX, float centerY, float width,
            float height, float angleOffset, int numSegments, boolean drawFilled)
    {
        if (numSegments < 3)
        {
            return;
        }

        // Convert angles into radians for our calculations
        angleOffset = (float) Math.toRadians(angleOffset);

        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = 2f * 3.1415926f / (float) numSegments;
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);
        final float offsetCos = (float) FastTrig.cos(angleOffset);
        final float offsetSin = (float) FastTrig.sin(angleOffset);
        final float yFactor = (height / width);

        // Start at angle = 0
        float x = width;// * offsetCos;
        float y = 0f;//height * offsetSin;
        float scaledX, scaledY, tmp;

        glBegin(drawFilled ? GL_TRIANGLE_FAN : GL_LINE_LOOP);
        for (int i = 0; i < numSegments - 1; i++)
        {
            // Rotate/scale the circle into an ellipse
            scaledX = (x * offsetCos) - (y * yFactor * offsetSin);
            scaledY = (x * offsetSin) + (y * yFactor * offsetCos);

            // Output vertex
            glVertex2f(scaledX + centerX, scaledY + centerY);

            // Apply the rotation matrix to the circle
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }
        scaledX = (x * offsetCos) - (y * yFactor * offsetSin);
        scaledY = (x * offsetSin) + (y * yFactor * offsetCos);
        glVertex2f(scaledX + centerX, scaledY + centerY);
        glEnd();
    }

    /**
     * Draws an arc made up of line segments.
     * <p>
     * This method only contains the actual drawing code and assumes all OpenGL
     * flags, color, line width etc have been set by the user beforehand.
     * <p>
     * Optimized arc-drawing algorithm based on code taken from:
     * <a href=http://slabode.exofire.net/circle_draw.shtml>
     * http://slabode.exofire.net/circle_draw.shtml</a>
     *
     * @param centerX     The x value of the center point of the arc.
     * @param centerY     The y value of the center point of the arc.
     * @param radius      The radius (in pixels) of the arc to be drawn.
     * @param startAngle  The angle the arc should start at, in degrees.
     * @param arcAngle    The size of the arc, in degrees.
     * @param numSegments How many line segments the arc should be made up
     *                    of (higher number = smoother arc, but higher GPU
     *                    cost).
     * @param drawFilled  Whether to draw the arc filled.
     * <p>
     * @since 1.8
     */
    public static void drawArc(float centerX, float centerY, float radius,
            float startAngle, float arcAngle, int numSegments, boolean drawFilled)
    {
        // Convert angles into radians for our calculations
        startAngle = (float) Math.toRadians(startAngle);
        arcAngle = (float) Math.toRadians(arcAngle);

        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = arcAngle / (float) (numSegments);
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);

        // Start at angle startAngle
        float x = (float) (radius * FastTrig.cos(startAngle));
        float y = (float) (radius * FastTrig.sin(startAngle));
        float tmp;

        glBegin(drawFilled ? GL_TRIANGLE_FAN : GL_LINE_STRIP);
        if (drawFilled)
        {
            glVertex2f(centerX, centerY);
        }
        for (int i = 0; i < numSegments; i++)
        {
            // Output vertex
            glVertex2f(x + centerX, y + centerY);

            // Apply the rotation matrix
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }
        glVertex2f(x + centerX, y + centerY);
        glEnd();
    }

    /**
     * @deprecated Use {@link DrawUtils#drawArc(float, float, float, float,
     * float, int, boolean)} instead.
     * @since 1.7
     */
    @Deprecated
    public static void drawArc(float centerX, float centerY, float radius,
            float startAngle, float arcAngle, int numSegments)
    {
        drawArc(centerX, centerY, radius, startAngle, arcAngle, numSegments, false);
    }

    private DrawUtils()
    {
    }
}
