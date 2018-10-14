package org.lazywizard.lazylib;

import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

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
     * Returns the facing (angle) of a {@link Vector2f}.
     *
     * @param vector The vector to get the facing of.
     *
     * @return The facing (angle) of {@code vector} in degrees, or 0 if the
     *         vector has no length.
     *
     * @since 1.7
     */
    public static float getFacing(Vector2f vector)
    {
        if (vector.lengthSquared() == 0f)
        {
            return 0f;
        }

        return MathUtils.clampAngle((float) Math.toDegrees(Math.atan2(vector.y, vector.x)));
    }

    /**
     * Returns the angle between two {@link Vector2f}s.
     *
     * @param from The source {@link Vector2f}.
     * @param to   The {@link Vector2f} to get the angle to.
     *
     * @return The angle pointing from {@code from} to {@code to}.
     *
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
     *
     * @return A normalized {@link Vector2f} pointing at {@code destination}.
     *
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
     *
     * @return The cross product of the two vectors.
     *
     * @since 1.7
     */
    public static float getCrossProduct(Vector2f vector1, Vector2f vector2)
    {
        return (vector1.x * vector2.y) - (vector1.y * vector2.x);
    }

    /**
     * Scales a vector to the requested length.
     *
     * <b>Note:</b> <i>if the vector's length is 0, no scaling will be done.</i>
     *
     * @param vector The vector to be modified.
     * @param length The new total length of {@code vector}.
     *
     * @return The modified {@code vector}, returned for easier chaining of methods.
     *
     * @since 2.3
     */
    public static Vector2f setLength(Vector2f vector, float length)
    {
        if (vector.lengthSquared() == 0f) return vector;

        vector.scale(length / vector.length());
        return vector;
    }

    /**
     * Reduces a vector's length if it is higher than the passed in argument.
     *
     * @param vector    The vector whose length should be clamped.
     * @param maxLength The maximum length of {@code vector}. If its current length is higher, it will be reduced to
     *                  this amount.
     *
     * @return The modified {@code vector}, returned for easier chaining of methods.
     *
     * @since 2.3
     */
    public static Vector2f clampLength(Vector2f vector, float maxLength)
    {
        if (maxLength > 0f && vector.lengthSquared() > (maxLength * maxLength))
        {
            setLength(vector, maxLength);
        }

        return vector;
    }

    /**
     * Rotates a {@link Vector2f} by a specified amount.
     *
     * @param toRotate The {@link Vector2f} to rotate.
     * @param angle    How much to rotate {@code toRotate}, in degrees.
     * @param dest     The destination {@link Vector2f}. Can be
     *                 {@code toRotate}.
     *
     * @return A rotated version of {@code toRotate} placed in {@code dest}.
     *
     * @since 1.7
     */
    public static Vector2f rotate(Vector2f toRotate, float angle, Vector2f dest)
    {
        if (angle == 0f)
        {
            return dest.set(toRotate);
        }

        angle = (float) Math.toRadians(angle);
        final float cos = (float) FastTrig.cos(angle), sin = (float) FastTrig.sin(angle);
        dest.set((toRotate.x * cos) - (toRotate.y * sin),
                (toRotate.x * sin) + (toRotate.y * cos));
        return dest;
    }

    /**
     * Rotates a {@link List} of {@link Vector2f}s by a specified amount. Much
     * more efficient than rotating each point individually.
     *
     * @param toRotate The {@link List} of {@link Vector2f}s to rotate.
     * @param angle    How much to rotate {@code toRotate}, in degrees.
     *
     * @return A {@link List} of {@link Vector2f}s that have been rotated.
     *
     * @since 2.0
     */
    public static List<Vector2f> rotate(List<Vector2f> toRotate, float angle)
    {
        if (angle == 0f)
        {
            return new ArrayList<>(toRotate);
        }

        angle = (float) Math.toRadians(angle);
        final float cos = (float) FastTrig.cos(angle), sin = (float) FastTrig.sin(angle);
        final List<Vector2f> rotated = new ArrayList<>(toRotate.size());
        for (Vector2f point : toRotate)
        {
            rotated.add(new Vector2f((point.x * cos) - (point.y * sin),
                    (point.x * sin) + (point.y * cos)));
        }

        return rotated;
    }

    /**
     * Rotates a {@link Vector2f} by a specified amount around a pivot point.
     *
     * @param toRotate   The {@link Vector2f} to rotate.
     * @param pivotPoint The central point to pivot around.
     * @param angle      How much to rotate {@code toRotate}, in degrees.
     * @param dest       The destination {@link Vector2f}. Can be
     *                   {@code toRotate}.
     *
     * @return A rotated version of {@code toRotate} placed in {@code dest}.
     *
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

    /**
     * Rotates a {@link List} of {@link Vector2f}s by a specified amount around
     * a pivot point. Much more efficient than rotating each point individually.
     *
     * @param toRotate   The {@link List} of {@link Vector2f}s to rotate.
     * @param pivotPoint The central point to pivot around.
     * @param angle      How much to rotate {@code toRotate}, in degrees.
     *
     * @return A {@link List} of {@link Vector2f}s that have been rotated around
     *         {@code pivotPoint}.
     *
     * @since 2.0
     */
    public static List<Vector2f> rotateAroundPivot(List<Vector2f> toRotate,
                                                   Vector2f pivotPoint, float angle)
    {
        List<Vector2f> rotated = new ArrayList<>(toRotate.size());
        if (angle == 0f)
        {
            for (Vector2f point : toRotate)
            {
                rotated.add(new Vector2f(point));
            }

            return rotated;
        }

        angle = (float) Math.toRadians(angle);
        final float cos = (float) FastTrig.cos(angle), sin = (float) FastTrig.sin(angle);
        for (Vector2f point : toRotate)
        {
            Vector2f.sub(point, pivotPoint, TEMP_VECTOR);
            TEMP_VECTOR.set((TEMP_VECTOR.x * cos) - (TEMP_VECTOR.y * sin),
                    (TEMP_VECTOR.x * sin) + (TEMP_VECTOR.y * cos));
            rotated.add(Vector2f.add(TEMP_VECTOR, pivotPoint, null));
        }

        return rotated;
    }

    private VectorUtils()
    {
    }
}
