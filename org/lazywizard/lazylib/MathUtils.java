package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with vectors, angles, distances, and circles.
 *
 * @author LazyWizard
 * @since 1.0
 */
// TODO: add getRandomPointInCone()
public class MathUtils
{
    private static final Random rng = new Random();

    /**
     * Returns the distance between two {@link SectorEntityToken}s,
     * including interaction radius.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistance(SectorEntityToken token1, SectorEntityToken token2)
    {
        return Math.max(0f, getDistance(token1.getLocation(), token2.getLocation())
                - (token1.getRadius() + token2.getRadius()));
    }

    /**
     * Returns the distance between a {@link SectorEntityToken} and a
     * {@link Vector2f} (includes interaction radius).
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.1
     */
    public static float getDistance(SectorEntityToken token, Vector2f vector)
    {
        return Math.max(0f, getDistance(token.getLocation(), vector) - token.getRadius());
    }

    /**
     * Returns the distance between two {@link CombatEntityAPI}s, including
     * collision radius.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistance(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return Math.max(0f, getDistance(obj1.getLocation(), obj2.getLocation())
                - (obj1.getCollisionRadius() + obj2.getCollisionRadius()));
    }

    /**
     * Returns the distance between a {@link CombatEntityAPI} and a
     * {@link Vector2f} (includes collision radius).
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistance(CombatEntityAPI entity, Vector2f vector)
    {
        return Math.max(0f, getDistance(entity.getLocation(), vector)
                - entity.getCollisionRadius());
    }

    /**
     * Returns the distance between two {@link Vector2f}s.
     *
     * For comparing distances, it is <i>vastly</i> more efficient to use
     * {@link MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)}.
     *
     * @return The distance between the two vectors.
     * <p>
     * @since 1.0
     */
    public static float getDistance(Vector2f vector1, Vector2f vector2)
    {
        float a = vector1.x - vector2.x;
        float b = vector1.y - vector2.y;
        return (float) Math.hypot(a, b);
    }

    /**
     * Returns the distance squared between two {@link SectorEntityToken}s.
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistanceSquared(SectorEntityToken token1, SectorEntityToken token2)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        float distance = getDistance(token1, token2);
        return distance * distance;
        //return getDistanceSquared(token1.getLocation(), token2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link SectorEntityToken} and
     * a {@link Vector2f}.
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.1
     */
    public static float getDistanceSquared(SectorEntityToken token, Vector2f vector)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        float distance = getDistance(token, vector);
        return distance * distance;
        //return getDistanceSquared(token.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link CombatEntityAPI}s
     * (includes collision radii). With the addition of collision radius
     * checking, there's no way to avoid calculating the square root.
     * {@link MathUtils#getDistance(com.fs.starfarer.api.combat.CombatEntityAPI,
     * com.fs.starfarer.api.combat.CombatEntityAPI)} will be just as efficient.
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistanceSquared(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        float distance = getDistance(obj1, obj2);
        return distance * distance;
        //return getDistanceSquared(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link CombatEntityAPI} and a
     * {@link Vector2f} (includes collision radius). With the addition of
     * collision radius checking, there's no way to avoid calculating the square
     * root.
     * {@link MathUtils#getDistance(com.fs.starfarer.api.combat.CombatEntityAPI,
     * org.lwjgl.util.vector.Vector2f)} will be just as efficient.
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistanceSquared(CombatEntityAPI entity, Vector2f vector)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        float distance = getDistance(entity, vector);
        return distance * distance;
        //return getDistanceSquared(entity.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link Vector2f}s (avoids a
     * costly sqrt()).
     *
     * When comparing distances, use this function instead of
     * {@link MathUtils#getDistance(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)}.
     *
     * @return The distance squared between the two vectors.
     * <p>
     * @since 1.0
     */
    public static float getDistanceSquared(Vector2f vector1, Vector2f vector2)
    {
        float a = vector1.x - vector2.x, b = vector1.y - vector2.y;
        return (a * a) + (b * b);
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
     * @since 1.0
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
     * Returns a normalized {@link Vector2f} pointing from {@code source}
     * to {@code destination}.
     *
     * @see MathUtils#getDirectionalVector(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static Vector2f getDirectionalVector(CombatEntityAPI source, Vector2f destination)
    {
        return getDirectionalVector(source.getLocation(), destination);
    }

    /**
     * Returns a normalized {@link Vector2f} pointing from {@code source}
     * to {@code destination}.
     *
     * @see MathUtils#getDirectionalVector(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static Vector2f getDirectionalVector(CombatEntityAPI source,
            CombatEntityAPI destination)
    {
        return getDirectionalVector(source.getLocation(), destination.getLocation());
    }

    /**
     * Clamps an angle within 360 degrees (ex: 400 degrees becomes 40 degrees).
     *
     * @param angle The angle to be clamped.
     * <p>
     * @return A value between 0 and 360 degrees.
     * <p>
     * @since 1.2
     */
    public static float clampAngle(float angle)
    {
        if ((angle < -360) || (angle > 360))
        {
            angle %= 360;
        }

        if (angle < 0)
        {
            angle += 360;
        }

        return angle;
    }

    /**
     * Returns the facing of a {@link Vector2f}.
     *
     * @param vector The vector to get the facing of.
     * <p>
     * @return The facing (angle) of {@code vector}.
     * <p>
     * @since 1.0
     */
    public static float getFacing(Vector2f vector)
    {
        return clampAngle((float) Math.toDegrees(Math.atan2(vector.y, vector.x)));
    }

    /**
     * Returns the angle between two {@link Vector2f}s.
     *
     * @param from The source {@link Vector2f}.
     * @param to   The {@link Vector2f} to get the angle to.
     * <p>
     * @return The angle from {@code from} to {@code to}.
     * <p>
     * @since 1.0
     */
    public static float getAngle(Vector2f from, Vector2f to)
    {
        return getFacing(getDirectionalVector(from, to));
    }

    /**
     * Returns the direction and length of the quickest rotation between two
     * angles.
     *
     * @param currAngle The current facing.
     * @param destAngle The facing to rotate towards.
     * <p>
     * @return The shortest distance between the two angles, in degrees.
     * <p>
     * @since 1.7
     */
    public static float getShortestRotation(float currAngle, float destAngle)
    {
        float distance = (destAngle - currAngle) + 180f;
        distance = (distance / 360.0f);
        distance = ((distance - (float) Math.floor(distance)) * 360f) - 180f;
        return distance;
    }

    /**
     * Returns a point along the circumference of a circle at the given angle.
     *
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     * @param angle  The angle, in degrees, to get the point at.
     * <p>
     * @return A {@link Vector2f} at [@code angle} degrees along the
     *         circumference of the given circle.
     * <p>
     * @since 1.0
     */
    public static Vector2f getPointOnCircumference(Vector2f center, float radius, float angle)
    {
        angle = clampAngle(angle);

        // Bypass relatively expensive trig operations whenever possible
        if (equals(angle, 0f))
        {
            return new Vector2f((center == null ? 0f : center.x) + radius,
                    (center == null ? 0f : center.y));
        }
        if (equals(angle, 90f))
        {
            return new Vector2f((center == null ? 0f : center.x),
                    (center == null ? 0f : center.y) + radius);
        }
        if (equals(angle, 180f))
        {
            return new Vector2f((center == null ? 0f : center.x) - radius,
                    (center == null ? 0f : center.y));
        }
        if (equals(angle, 270f))
        {
            return new Vector2f((center == null ? 0f : center.x),
                    (center == null ? 0f : center.y) - radius);
        }

        double rad = Math.toRadians(angle);
        return new Vector2f((float) FastTrig.cos(rad) * radius
                + (center == null ? 0f : center.x),
                (float) FastTrig.sin(rad) * radius
                + (center == null ? 0f : center.y));
    }

    /**
     * Returns a random point along the circumference of a circle.
     *
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     * <p>
     * @return A random point along the circumference of the given circle.
     * <p>
     * @see MathUtils#getPointOnCircumference(org.lwjgl.util.vector.Vector2f,
     * float, float)
     * @since 1.0
     */
    public static Vector2f getRandomPointOnCircumference(Vector2f center, float radius)
    {
        return getPointOnCircumference(center, radius, rng.nextFloat() * 360f);
    }

    /**
     * Returns a random point inside of a circle with uniform distribution.
     *
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     * <p>
     * @return A random point inside of the given circle.
     * <p>
     * @since 1.0
     */
    public static Vector2f getRandomPointInCircle(Vector2f center, float radius)
    {
        double t = 2 * Math.PI * rng.nextDouble(),
                u = rng.nextDouble() + rng.nextDouble(),
                r = (u > 1 ? 2 - u : u);
        return new Vector2f((float) (r * FastTrig.cos(t)) * radius
                + (center == null ? 0f : center.x),
                (float) (r * FastTrig.sin(t)) * radius
                + (center == null ? 0f : center.y));
        //return getRandomPointOnCircumference(center, radius * rng.nextFloat());
    }

    /**
     * Returns a random point along the line between two {@link Vector2f}s.
     *
     * @param lineStart The starting point of the line.
     * @param lineEnd   The end point of the line.
     * <p>
     * @return A random {@link Vector2f} along the line between
     *         {@code lineStart}
     *         and {@code lineEnd}.
     * <p>
     * @since 1.6
     */
    public static Vector2f getRandomPointOnLine(Vector2f lineStart, Vector2f lineEnd)
    {
        float t = rng.nextFloat();
        return new Vector2f(lineStart.x + t * (lineEnd.x - lineStart.x),
                lineStart.y + t * (lineEnd.y - lineStart.y));
    }

    /**
     * Returns an evenly distributed {@link List} of points along a circle's
     * circumference.
     *
     * @param center      The center point of the circle (can be null for a 0, 0
     *                    origin).
     * @param radius      The radius of the circle.
     * @param numPoints   How many points to generate.
     * @param angleOffset The offset angle of the initial point.
     * <p>
     * @return A {@link List} of {@link Vector2f}s that are evenly distributed
     *         along the circle's circumference.
     * <p>
     * @since 1.0
     */
    public static List<Vector2f> getPointsAlongCircumference(Vector2f center,
            float radius, int numPoints, float angleOffset)
    {
        angleOffset %= 360;

        List<Vector2f> points = new ArrayList<Vector2f>(numPoints);
        for (int x = 0; x < numPoints; x++)
        {
            points.add(getPointOnCircumference(center, radius, angleOffset));
            angleOffset += (360f / numPoints);
            angleOffset %= 360;
        }

        return points;
    }

    /**
     * Returns whether a point is within the bounds of a circle or not.
     *
     * @param point  The {@link Vector2f} to check.
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     * <p>
     * @return {@code true} if {@code point} is within the circle,
     *         {@code false} otherwise.
     * <p>
     * @since 1.0
     */
    public static boolean isPointWithinCircle(Vector2f point, Vector2f center, float radius)
    {
        float a = point.x - (center == null ? 0f : center.x),
                b = point.y - (center == null ? 0f : center.y);
        return (a * a) + (b * b) < (radius * radius);
    }

    /**
     * Check if a point is along the line between two {@link Vector2f}s.
     *
     * @param point     The point to check.
     * @param lineStart The starting point of the line.
     * @param lineEnd   The end point of the line.
     * <p>
     * @return {@code true} if the point is along the line, {@code false}
     *         otherwise.
     * <p>
     * @since 1.6
     */
    public static boolean isPointOnLine(Vector2f point, Vector2f lineStart, Vector2f lineEnd)
    {
        return (Line2D.Float.ptSegDistSq(lineStart.x, lineStart.y,
                lineEnd.x, lineEnd.y, point.x, point.y) <= 0.00001);
    }

    /**
     * Returns a {@link List} of evenly spaced {@link Vector2f}s inside a
     * circle.
     *
     * WARNING: be VERY conservative using this method - a radius of 250 and a
     * spacing
     * of 5 will result in 10,000 circle checks and 7,825 {@link Vector2f}s
     * created!
     *
     * @param center             The center point of the circle (can be null for
     *                           a 0, 0 origin).
     * @param radius             The radius of the circle.
     * @param spaceBetweenPoints How much space should be between each point.
     * <p>
     * @return A {@link List} of evenly spaced {@link Vector2f}s inside a
     *         circle.
     * <p>
     * @since 1.4
     */
    public static List<Vector2f> getEquidistantPointsInsideCircle(Vector2f center,
            float radius, float spaceBetweenPoints)
    {
        // Avoid infinite loops
        if (spaceBetweenPoints <= 0f)
        {
            throw new RuntimeException("Space between points must be a positive number!");
        }

        // Calculate the rough number of results we will have and allocate memory for them
        float size = radius / spaceBetweenPoints;
        int toAllocate = (int) ((size * size * Math.PI) + 0.5f);
        List<Vector2f> points = new ArrayList<Vector2f>(toAllocate);

        // Support for null vectors
        float centerX = (center == null ? 0f : center.x);
        float centerY = (center == null ? 0f : center.y);
        float a, b;

        // For every point, check if it is inside the given circle
        for (float x = centerX - radius; x < centerY + radius; x += spaceBetweenPoints)
        {
            for (float y = centerX - radius; y < centerY + radius; y += spaceBetweenPoints)
            {
                a = x - centerX;
                b = y - centerY;

                // Point is in circle
                if ((a * a) + (b * b) < (radius * radius))
                {
                    points.add(new Vector2f(x, y));
                }
            }
        }

        return points;
    }

    /**
     * Returns a random number within a given range.
     *
     * @param min The minimum value to select.
     * @param max The maximum value to select.
     * <p>
     * @return A random {@link Float} between {@code min} and {@code max}.
     * <p>
     * @since 1.4
     */
    public static float getRandomNumberInRange(float min, float max)
    {
        return rng.nextFloat() * (max - min) + min;
    }

    /**
     * Tests for near-equality of floating point numbers.
     *
     * @param a The first float to compare.
     * @param b The second float to compare.
     * <p>
     * @return {@code true} if {@code a} and {@code b} are within 99.99999%
     *         of each other, {@code false} otherwise.
     */
    public static boolean equals(float a, float b)
    {
        return ((a == b) || (a >= b * (.9999999f) && a <= b * (1.0000001f)));
    }

    /**
     * Returns the random number generator used by LazyLib's methods. Useful
     * for seed manipulation or to avoid instantiating your own.
     *
     * @return The {@link Random} instance used by LazyLib.
     * <p>
     * @since 1.5
     */
    public static Random getRandom()
    {
        return rng;
    }

    /**
     * @deprecated Use {@link
     * CollisionUtils#isPointWithinBounds(org.lwjgl.util.vector.Vector2f,
     * com.fs.starfarer.api.combat.CombatEntityAPI)} instead.
     * @since 1.0
     */
    @Deprecated
    public static boolean isPointWithinBounds(Vector2f point, CombatEntityAPI entity)
    {
        LazyLib.onDeprecatedMethodUsage(MathUtils.class,
                "isPointWithinBounds(Vector2f point, CombatEntityAPI entity)");

        return CollisionUtils.isPointWithinBounds(point, entity);
    }

    private MathUtils()
    {
    }
}
