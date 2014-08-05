package org.lazywizard.lazylib;

import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for manipulating {@link Vector2f}s.
 *
 * @author LazyWizard
 * @since 1.7
 */
public class VectorUtils
{
    private static final Vector2f TEMP_VECTOR = new Vector2f();

    /**
     * Returns the facing of a {@link Vector2f}.
     *
     * @param vector The vector to get the facing of.
     * <p>
     * @return The facing (angle) of {@code vector}.
     * <p>
     * @since 1.7
     */
    public static float getFacing(Vector2f vector)
    {
        return MathUtils.clampAngle((float) Math.toDegrees(Math.atan2(vector.y, vector.x)));
    }

    /**
     * Returns the angle between two {@link Vector2f}s.
     *
     * @param from The source {@link Vector2f}.
     * @param to   The {@link Vector2f} to get the angle to.
     * <p>
     * @return The angle pointing from {@code from} to {@code to}.
     * <p>
     * @since 1.7
     */
    public static float getAngle(Vector2f from, Vector2f to)
    {
        return getFacing(Vector2f.sub(to, from, null));
    }

    /**
     * Returns a normalized {@link Vector2f} pointing from {@code source} to
     * {@code destination}.
     *
     * @param source      The origin of the vector.
     * @param destination The location to point at.
     * <p>
     * @return A normalized {@link Vector2f} pointing at {@code destination}.
     * <p>
     * @since 1.8
     */
    public static Vector2f getDirectionalVector(Vector2f source, Vector2f destination)
    {
        Vector2f dir = Vector2f.sub(destination, source, null);

        // Avoid crash with identical vectors
        if (dir.x == 0f && dir.y == 0f)
        {
            return dir;
        }

        return dir.normalise(null);
    }

    /**
     * Returns the cross product of two {@link Vector2f}s.
     *
     * @param vector1 The first {@link Vector2f}.
     * @param vector2 The second {@link Vector2f}.
     * <p>
     * @return The cross product of the two vectors.
     * <p>
     * @since 1.7
     */
    public static float getCrossProduct(Vector2f vector1, Vector2f vector2)
    {
        return (vector1.x * vector2.y) - (vector1.y * vector2.x);
    }

    /**
     * Rotates a {@link Vector2f} by a specified amount.
     *
     * @param toRotate The {@link Vector2f} to rotate.
     * @param angle    How much to rotate {@code toRotate}, in degrees.
     * @param dest     The destination {@link Vector2f}. Can be
     *                 {@code toRotate}.
     * <p>
     * @return A rotated version of {@code toRotate} placed in {@code dest}.
     * <p>
     * @since 1.7
     */
    public static Vector2f rotate(Vector2f toRotate, float angle, Vector2f dest)
    {
        if (angle == 0f)
        {
            return dest.set(toRotate);
        }

        angle = (float) Math.toRadians(angle);
        float cos = (float) FastTrig.cos(angle), sin = (float) FastTrig.sin(angle);
        dest.set((toRotate.x * cos) - (toRotate.y * sin),
                (toRotate.x * sin) + (toRotate.y * cos));
        return dest;
    }

    /**
     * Rotates a {@link Vector2f} by a specified amount around a pivot point.
     *
     * @param toRotate   The {@link Vector2f} to rotate.
     * @param pivotPoint The central point to pivot around.
     * @param angle      How much to rotate {@code toRotate}, in degrees.
     * @param dest       The destination {@link Vector2f}. Can be
     *                   {@code toRotate}.
     * <p>
     * @return A rotated version of {@code toRotate} placed in {@code dest}.
     * <p>
     * @since 1.7
     */
    public static Vector2f rotateAroundPivot(Vector2f toRotate, Vector2f pivotPoint,
            float angle, Vector2f dest)
    {
        if (angle == 0f)
        {
            return dest.set(toRotate);
        }

        Vector2f.sub(toRotate, pivotPoint, TEMP_VECTOR);
        rotate(TEMP_VECTOR, angle, TEMP_VECTOR);
        Vector2f.add(TEMP_VECTOR, pivotPoint, dest);
        return dest;
    }

    private VectorUtils()
    {
    }
}
