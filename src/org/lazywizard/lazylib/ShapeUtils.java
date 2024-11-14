package org.lazywizard.lazylib;

import org.lazywizard.lazylib.opengl.DrawUtils;

/**
 * Provides methods to calculate vertices of common shapes. Equivalent
 * algorithms to {@link DrawUtils}, but returns the raw vertices
 * instead of drawing them for you.
 *
 * @author LazyWizard
 * @since 3.0
 */
public class ShapeUtils
{
    /**
     * Creates the vertices for a simple circle.
     * <p>
     * Optimized circle-drawing algorithm based on code taken from:
     * <a href=http://slabode.exofire.net/circle_draw.shtml>
     * http://slabode.exofire.net/circle_draw.shtml</a>
     *
     * @param centerX     The x value of the center point of the circle.
     * @param centerY     The y value of the center point of the circle.
     * @param radius      The radius of the circle to be drawn.
     * @param numSegments How many line segments the circle should be made up
     *                    of (higher number = smoother circle).
     *
     * @return The vertices needed to draw a circle with the given parameters.
     *
     * @since 3.0
     */
    public static float[] createCircle(float centerX, float centerY,
                                       float radius, int numSegments)
    {
        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = 2f * 3.1415926f / numSegments;
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);

        // Start at angle = 0
        float x = radius;
        float y = 0;
        float tmp;

        float[] vertices = new float[numSegments * 2];
        for (int i = 0; i < vertices.length; i += 2)
        {
            // Output vertex
            vertices[i] = x + centerX;
            vertices[i + 1] = y + centerY;

            // Apply the rotation matrix
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }

        return vertices;
    }

    /**
     * Creates the vertices for an elliptical shape.
     * <p>
     * Optimized circle-drawing algorithm based on code taken from:
     * <a href=http://slabode.exofire.net/circle_draw.shtml>
     * http://slabode.exofire.net/circle_draw.shtml</a>
     *
     * @param centerX     The x value of the center point of the circle.
     * @param centerY     The y value of the center point of the circle.
     * @param width       The width (size on unrotated x-axis) of the ellipse.
     * @param height      The height (size on unrotated y-axis) of the ellipse.
     * @param angleOffset How much to rotate the ellipse from its original axis,
     *                    in degrees.
     * @param numSegments How many line segments the ellipse should be made up
     *                    of (higher number = smoother ellipse).
     *
     * @return The vertices needed to draw an ellipse with the given parameters.
     *
     * @since 3.0
     */
    public static float[] createEllipse(float centerX, float centerY,
                                        float width, float height, float angleOffset, int numSegments)
    {
        // Convert angles into radians for our calculations
        angleOffset = (float) Math.toRadians(angleOffset);

        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = 2f * 3.1415926f / numSegments;
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);
        final float offsetCos = (float) FastTrig.cos(angleOffset);
        final float offsetSin = (float) FastTrig.sin(angleOffset);
        final float yFactor = (height / width);

        // Start at angle = 0
        float x = width;
        float y = 0f;
        float scaledX, scaledY, tmp;

        float[] vertices = new float[numSegments * 2];
        for (int i = 0; i < vertices.length; i += 2)
        {
            // Rotate/scale the circle into an ellipse
            scaledX = (x * offsetCos) - (y * yFactor * offsetSin);
            scaledY = (x * offsetSin) + (y * yFactor * offsetCos);

            // Output vertex
            vertices[i] = scaledX + centerX;
            vertices[i + 1] = scaledY + centerY;

            // Apply the rotation matrix to the circle
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }

        return vertices;
    }

    /**
     * Creates the vertices for an arc shape.
     * <p>
     * Optimized arc-drawing algorithm based on code taken from:
     * <a href=http://slabode.exofire.net/circle_draw.shtml>
     * http://slabode.exofire.net/circle_draw.shtml</a>
     *
     * @param centerX     The x value of the center point of the arc.
     * @param centerY     The y value of the center point of the arc.
     * @param radius      The radius of the arc to be drawn.
     * @param startAngle  The angle the arc should start at, in degrees.
     * @param arcAngle    The size of the arc, in degrees.
     * @param numSegments How many line segments the arc should be made up
     *                    of (higher number = smoother arc).
     *
     * @return The vertices needed to draw an arc with the given parameters.
     *
     * @since 3.0
     */
    public static float[] createArc(float centerX, float centerY, float radius,
                                    float startAngle, float arcAngle, int numSegments)
    {
        // Convert angles into radians for our calculations
        startAngle = (float) Math.toRadians(startAngle);
        arcAngle = (float) Math.toRadians(arcAngle);

        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = arcAngle / numSegments;
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);

        // Start at angle startAngle
        float x = (float) (radius * FastTrig.cos(startAngle));
        float y = (float) (radius * FastTrig.sin(startAngle));
        float tmp;

        float[] vertices = new float[numSegments * 2 + 2];
        for (int i = 0; i < vertices.length - 2; i += 2)
        {
            // Output vertex
            vertices[i] = x + centerX;
            vertices[i + 1] = y + centerY;

            // Apply the rotation matrix
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }
        vertices[vertices.length - 2] = x + centerX;
        vertices[vertices.length - 1] = y + centerY;

        return vertices;
    }

    private ShapeUtils()
    {
    }
}
