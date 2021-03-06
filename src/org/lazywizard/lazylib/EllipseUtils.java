package org.lazywizard.lazylib;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

/**
 * Contains methods for dealing with ellipses. Since these methods are only
 * used in very specific cases, they have been moved into their own class to
 * reduce clutter in the main library classes.
 *
 * @author LazyWizard
 * @since 1.9
 */
public class EllipseUtils
{
    /**
     * Returns a point along the circumference of an ellipse at the given angle
     * and facing.
     *
     * @param ellipseCenter      The center point of the ellipse (can be null
     *                           for a 0, 0 origin).
     * @param ellipseWidth       The width (size on unrotated x-axis) of the
     *                           ellipse.
     * @param ellipseHeight      The height (size on unrotated y-axis) of the
     *                           ellipse.
     * @param ellipseAngleOffset How much to rotate the ellipse from its
     *                           original axis, in degrees.
     * @param angle              The angle, in degrees, to get the point at.
     *
     * @return A {@link Vector2f} at [@code angle} degrees along the
     *         circumference of the given ellipse.
     *
     * @since 1.9
     */
    public static Vector2f getPointOnEllipse(@Nullable Vector2f ellipseCenter,
                                             float ellipseWidth, float ellipseHeight, float ellipseAngleOffset, float angle)
    {
        ellipseAngleOffset = (float) Math.toRadians(ellipseAngleOffset);
        angle = (float) Math.toRadians(angle);
        float sin = (float) FastTrig.sin(angle),
                cos = (float) FastTrig.cos(angle);

        // Get point on unrotated ellipse around origin (0, 0)
        final float x = ellipseWidth * cos;
        final float y = ellipseHeight * sin;

        // Rotate point to match ellipses rotation and translate back to center
        sin = (float) FastTrig.sin(ellipseAngleOffset);
        cos = (float) FastTrig.cos(ellipseAngleOffset);
        return new Vector2f((x * cos) - (y * sin)
                + (ellipseCenter == null ? 0f : ellipseCenter.x),
                (x * sin) + (y * cos) + (ellipseCenter == null ? 0f : ellipseCenter.y));
    }

    /**
     * Returns a random point along the circumference of an ellipse.
     *
     * @param ellipseCenter      The center point of the ellipse (can be null
     *                           for a 0, 0 origin).
     * @param ellipseWidth       The width (size on unrotated x-axis) of the
     *                           ellipse.
     * @param ellipseHeight      The height (size on unrotated y-axis) of the
     *                           ellipse.
     * @param ellipseAngleOffset How much to rotate the ellipse from its
     *                           original axis, in degrees.
     *
     * @return A random point along the circumference of the given ellipse.
     *
     * @see EllipseUtils#getPointOnEllipse(Vector2f, float, float, float, float)
     * @since 1.9
     */
    public static Vector2f getRandomPointOnEllipse(@Nullable Vector2f ellipseCenter,
                                                   float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
        return getPointOnEllipse(ellipseCenter, ellipseWidth, ellipseHeight,
                ellipseAngleOffset, MathUtils.getRandom().nextFloat() * 360f);
    }

    /**
     * Returns a random point inside of an ellipse with uniform distribution.
     *
     * @param ellipseCenter      The center point of the ellipse (can be null
     *                           for a 0, 0 origin).
     * @param ellipseWidth       The width (size on unrotated x-axis) of the
     *                           ellipse.
     * @param ellipseHeight      The height (size on unrotated y-axis) of the
     *                           ellipse.
     * @param ellipseAngleOffset How much to rotate the ellipse from its
     *                           original axis, in degrees.
     *
     * @return A random point inside of the given ellipse.
     *
     * @since 2.0
     */
    public static Vector2f getRandomPointInEllipse(@Nullable Vector2f ellipseCenter,
                                                   float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
        final Random rng = MathUtils.getRandom();
        final double u = rng.nextDouble() + rng.nextDouble();
        final float r = (float) (u > 1 ? 2 - u : u);

        return getRandomPointOnEllipse(ellipseCenter, ellipseWidth * r,
                ellipseHeight * r, ellipseAngleOffset);
    }

    /**
     * Checks whether a point is on or within the bounds of an ellipse.
     *
     * @param point              The {@link Vector2f} to check.
     * @param ellipseCenter      The center point of the ellipse (can be null
     *                           for a 0, 0 origin).
     * @param ellipseWidth       The width (size on unrotated x-axis) of the
     *                           ellipse.
     * @param ellipseHeight      The height (size on unrotated y-axis) of the
     *                           ellipse.
     * @param ellipseAngleOffset How much to rotate the ellipse from its
     *                           original axis, in degrees.
     *
     * @return {@code true} if {@code point} is on or within the ellipse,
     *         {@code false} otherwise.
     *
     * @since 1.9
     */
    public static boolean isPointWithinEllipse(Vector2f point, @Nullable Vector2f ellipseCenter,
                                               float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
        // Move relative to 0, 0 and rotate to match ellipse offset
        Vector2f origin = (ellipseCenter == null ? new Vector2f(point)
                : Vector2f.sub(point, ellipseCenter, null));
        VectorUtils.rotate(origin, -ellipseAngleOffset, origin);

        final float x = (origin.x * origin.x) / (ellipseWidth * ellipseWidth),
                y = (origin.y * origin.y) / (ellipseHeight * ellipseHeight);
        return ((x + y) <= 1.0001f);
    }

    private EllipseUtils()
    {
    }
}
