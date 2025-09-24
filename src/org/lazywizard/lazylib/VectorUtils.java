package org.lazywizard.lazylib;

import org.lwjgl.util.vector.Vector2f;

import java.nio.FloatBuffer;
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
     * Returns the facing (angle) of a {@link Vector2f} in degrees. Accurate to within ~0.29 degrees. If you need
     * more accuracy, use {@link VectorUtils#getFacingStrict(Vector2f)}.
     *
     * @param vector The vector to get the facing of.
     *
     * @return The facing (angle) of {@code vector} in degrees, or 0 if the
     *         vector has no length.
     *
     * @see VectorUtils#getFacingStrict(Vector2f)
     * @since 1.7
     */
    public static float getFacing(Vector2f vector)
    {
        if (isZeroVector(vector))
        {
            return 0f;
        }

        return MathUtils.clampAngle((float) Math.toDegrees(FastTrig.atan2(vector.y, vector.x)));
    }

    /**
     * Returns the facing (angle) of a {@link Vector2f} in degrees. This method uses the slower but more accurate
     * behavior of pre-2.3 {@link VectorUtils#getFacing(Vector2f)}.
     *
     * @param vector The vector to get the facing of.
     *
     * @return The facing (angle) of {@code vector} in degrees, or 0 if the vector has no length.
     *
     * @since 2.3
     */
    public static float getFacingStrict(Vector2f vector)
    {
        if (isZeroVector(vector))
        {
            return 0f;
        }

        return MathUtils.clampAngle((float) Math.toDegrees(Math.atan2(vector.y, vector.x)));
    }

    /**
     * Returns the angle between two {@link Vector2f}s in degrees. Accurate to within ~0.29 degrees. If you need
     * more accuracy, use {@link VectorUtils#getAngleStrict(Vector2f, Vector2f)}.
     *
     * @param from The source {@link Vector2f}.
     * @param to   The {@link Vector2f} to get the angle to.
     *
     * @return The angle pointing from {@code from} to {@code to}, in degrees.
     *
     * @see VectorUtils#getAngleStrict(Vector2f, Vector2f)
     * @since 1.7
     */
    public static float getAngle(Vector2f from, Vector2f to)
    {
        return getFacing(Vector2f.sub(to, from, null));
    }

    /**
     * Returns the angle between two {@link Vector2f}s in degrees. This method uses the slower but more accurate
     * behavior of pre-2.3 {@link VectorUtils#getAngle(Vector2f, Vector2f)}.
     *
     * @param from The source {@link Vector2f}.
     * @param to   The {@link Vector2f} to get the angle to.
     *
     * @return The angle pointing from {@code from} to {@code to}.
     *
     * @since 2.3
     */
    public static float getAngleStrict(Vector2f from, Vector2f to)
    {
        return getFacingStrict(Vector2f.sub(to, from, null));
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
        if (isZeroVector(dir))
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
     * Tests whether a vector is a zero vector (coordinates of {0, 0}).
     *
     * @param vector The vector to test.
     *
     * @return {@code true} if both of {@code vector}'s coordinates are 0, {@code false} otherwise.
     *
     * @since 2.3
     */
    public static boolean isZeroVector(Vector2f vector)
    {
        return (vector.x == 0 && vector.y == 0);
    }

    /**
     * Scales a vector to the requested length and stores the result in a destination vector.
     * <p>
     * <b>Note:</b> <i>if the vector's length is 0, no scaling will be performed.</i>
     *
     * @param vector The vector to be resized. Will not be modified; instead the result will be placed in {@code dest}.
     * @param length The new total length of the destination vector.
     * @param dest   The destination {@link Vector2f}. Can be {@code vector}.
     *
     * @return {@code dest}, scaled based on {@code vector}, returned for easier chaining of methods.
     *
     * @since 2.3
     */
    public static Vector2f resize(Vector2f vector, float length, Vector2f dest)
    {
        if (dest != vector) dest.set(vector);
        if (!isZeroVector(dest)) dest.scale(length / dest.length());
        return dest;
    }

    /**
     * Scales a vector to the requested length.
     * <p>
     * <b>Note:</b> <i>if the vector's length is 0, no scaling will be performed.</i>
     *
     * @param vector The vector to be modified.
     * @param length The new total length of {@code vector}.
     *
     * @return The modified {@code vector}, returned for easier chaining of methods.
     *
     * @see VectorUtils#resize(Vector2f, float, Vector2f)
     * @since 2.3
     */
    public static Vector2f resize(Vector2f vector, float length)
    {
        return resize(vector, length, vector);
    }

    /**
     * Reduces a vector's length if it is higher than the passed in argument and stores the result in a destination
     * vector.
     *
     * @param toClamp   The vector whose length should be clamped. Will not be modified; instead the result will be
     *                  placed in {@code dest}.
     * @param maxLength The maximum length of the destination vector. If its current length is higher, it will be
     *                  reduced to this amount.
     * @param dest      The destination {@link Vector2f}. Can be {@code toClamp}.
     *
     * @return {@code dest}, clamped based on {@code toClamp}, returned for easier chaining of methods.
     *
     * @since 2.3
     */
    public static Vector2f clampLength(Vector2f toClamp, float maxLength, Vector2f dest)
    {
        if (dest != toClamp) dest.set(toClamp);
        if (dest.lengthSquared() > (maxLength * maxLength))
        {
            resize(toClamp, maxLength, dest);
        }

        return dest;
    }

    /**
     * Reduces a vector's length if it is higher than the passed in argument.
     *
     * @param toClamp   The vector whose length should be clamped.
     * @param maxLength The maximum length of {@code toClamp}. If its current length is higher, it will be reduced to
     *                  this amount.
     *
     * @return The modified {@code toClamp}, returned for easier chaining of methods.
     *
     * @see VectorUtils#clampLength(Vector2f, float, Vector2f)
     * @since 2.3
     */
    public static Vector2f clampLength(Vector2f toClamp, float maxLength)
    {
        return clampLength(toClamp, maxLength, toClamp);
    }

    /**
     * Ensures a vector's length is within the given parameters and stores the result in a destination vector.
     *
     * @param toClamp   The vector whose length should be clamped. Will not be modified; instead the result will be
     *                  placed in {@code dest}.
     * @param minLength The minimum length of the destination vector. If its current length is lower, it will be
     *                  increased to this amount.
     * @param maxLength The maximum length of the destination vector. If its current length is higher, it will be
     *                  reduced to this amount.
     * @param dest      The destination {@link Vector2f}. Can be {@code toClamp}.
     *
     * @return {@code dest}, clamped based on {@code toClamp}, returned for easier chaining of methods.
     *
     * @since 2.3
     */
    public static Vector2f clampLength(Vector2f toClamp, float minLength, float maxLength, Vector2f dest)
    {
        resize(toClamp, MathUtils.clamp(toClamp.length(), minLength, maxLength), dest);
        return dest;
    }

    /**
     * Ensures a vector's length is within the given parameters and stores the result in a destination vector.
     *
     * @param toClamp   The vector whose length should be clamped. Will not be modified; instead the result will be
     *                  placed in {@code dest}.
     * @param minLength The minimum length of {@code toClamp}. If its current length is lower, it will be increased to
     *                  this amount.
     * @param maxLength The maximum length of {@code toClamp}. If its current length is higher, it will be reduced to
     *                  this amount.
     *
     * @return The modified {@code toClamp}, returned for easier chaining of methods.
     *
     * @see VectorUtils#clampLength(Vector2f, float, float, Vector2f)
     * @since 2.3
     */
    public static Vector2f clampLength(Vector2f toClamp, float minLength, float maxLength)
    {
        return clampLength(toClamp, minLength, maxLength, toClamp);
    }

    /**
     * Rotates a {@link Vector2f} by a specified amount and stores the result in a destination vector.
     *
     * @param toRotate The {@link Vector2f} to rotate. Will not be modified; instead the result will be placed in {@code
     *                 dest}.
     * @param angle    How much to rotate the destination vector, in degrees.
     * @param dest     The destination {@link Vector2f}. Can be {@code toRotate}.
     *
     * @return {@code dest}, rotated based on {@code toRotate}, returned for easier chaining of methods.
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
     * Rotates a {@link Vector2f} by a specified amount.
     *
     * @param toRotate The {@link Vector2f} to rotate.
     * @param angle    How much to rotate {@code toRotate}, in degrees.
     *
     * @return The modified {@code toRotate}, returned for easier chaining of methods.
     *
     * @see VectorUtils#rotate(Vector2f, float, Vector2f)
     * @since 2.3
     */
    public static Vector2f rotate(Vector2f toRotate, float angle)
    {
        return rotate(toRotate, angle, toRotate);
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
     * Rotates a {@link Vector2f} by a specified amount around a pivot point and stores the result in a destination
     * vector.
     *
     * @param toRotate   The {@link Vector2f} to rotate. Will not be modified; instead the result will be placed in
     *                   {@code dest}.
     * @param pivotPoint The central point to pivot around.
     * @param angle      How much to rotate the destination vector, in degrees.
     * @param dest       The destination {@link Vector2f}. Can be {@code toRotate}.
     *
     * @return {@code dest}, rotated based on {@code toRotate} around {@code pivotPoint}, returned for easier chaining
     *         of methods.
     *
     * @since 1.7
     */
    public static Vector2f rotateAroundPivot(Vector2f toRotate, Vector2f pivotPoint, float angle, Vector2f dest)
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
     * Rotates a {@link Vector2f} by a specified amount around a pivot point.
     *
     * @param toRotate   The {@link Vector2f} to rotate.
     * @param pivotPoint The central point to pivot around.
     * @param angle      How much to rotate {@code toRotate}, in degrees.
     *
     * @return The modified {@code toRotate}, returned for easier chaining of methods.
     *
     * @see VectorUtils#rotateAroundPivot(Vector2f, Vector2f, float, Vector2f)
     * @since 2.3
     */
    public static Vector2f rotateAroundPivot(Vector2f toRotate, Vector2f pivotPoint, float angle)
    {
        return rotateAroundPivot(toRotate, pivotPoint, angle, toRotate);
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
    public static List<Vector2f> rotateAroundPivot(List<Vector2f> toRotate, Vector2f pivotPoint, float angle)
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

    /**
     * Converts a float array of vertices into a {@link List} of {@link Vector2f}s.
     *
     * @param coordinates An array of floats representing a list of x and y coordinates. The length of
     *                    {@code coordinates} must be even or this method will throw a {@link RuntimeException}!.
     *
     * @return A {@link List} of {@link Vector2f}s that contains all coordinates passed in.
     *
     * @since 3.0
     */
    public static List<Vector2f> toVectorList(float[] coordinates)
    {
        if (coordinates.length % 2 != 0)
        {
            throw new RuntimeException("Coordinates must be in pairs!");
        }

        final List<Vector2f> vertices = new ArrayList<>(coordinates.length / 2);
        boolean isX = false;
        final Vector2f vertex = new Vector2f();
        for (float part : coordinates)
        {
            isX = !isX;
            if (isX)
            {
                vertex.x = part;
            }
            else
            {
                vertex.y = part;
                vertices.add(new Vector2f(vertex));
            }
        }

        return vertices;
    }

    /**
     * Converts a {@link List} of {@link Vector2f}s into a float array of vertices for
     * use with {@link FloatBuffer}s in OpenGL.
     *
     * @param vectors A {@link List} of {@link Vector2f}s to convert into a float array.
     *
     * @return A float array containing the coordinates contained in {@code vectors}.
     *
     * @since 3.0
     */
    public static float[] toFloatArray(List<Vector2f> vectors)
    {
        final float[] coordinates = new float[vectors.size() * 2];
        for (int i = 0; i < vectors.size(); i++)
        {
            final Vector2f vector = vectors.get(i);
            coordinates[i * 2] = vector.x;
            coordinates[i * 2 + 1] = vector.y;
        }

        return coordinates;
    }

    private VectorUtils()
    {
    }
}
