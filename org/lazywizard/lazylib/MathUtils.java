package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.CombatEntityAPI;
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
public class MathUtils
{
    private static final Random rng = new Random();

    /**
     * Returns the distance between two {@link SectorEntityToken}s.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistance(SectorEntityToken token1, SectorEntityToken token2)
    {
        return getDistance(token1.getLocation(), token2.getLocation());
    }

    /**
     * Returns the distance between a {@link SectorEntityToken} and a {@link Vector2f).
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     * @since 1.1
     */
    public static float getDistance(SectorEntityToken token, Vector2f vector)
    {
        return getDistance(token.getLocation(), vector);
    }

    /**
     * Returns the distance between two {@link CombatEntityAPI}s, including collision radius.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     * @since 1.0
     */
    public static float getDistance(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return Math.max(0f, getDistance(obj1.getLocation(), obj2.getLocation())
                - (obj1.getCollisionRadius() + obj2.getCollisionRadius()));
    }

    /**
     * Returns the distance between a {@link CombatEntityAPI} and a {@link Vector2f} (includes collision radius).
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
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
        return getDistanceSquared(token1.getLocation(), token2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link SectorEntityToken} and
     * a {@link Vector2f).
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @since 1.1
     */
    public static float getDistanceSquared(SectorEntityToken token, Vector2f vector)
    {
        return getDistanceSquared(token.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link CombatEntityAPI}s
     * (includes collision radii).
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @deprecated Use {@link
     * MathUtils#getDistance(com.fs.starfarer.api.combat.CombatEntityAPI,
     * com.fs.starfarer.api.combat.CombatEntityAPI)} instead. With the addition
     * of collision radius checking, there's no way to avoid calculating the
     * square root.
     * @since 1.0
     */
    @Deprecated
    public static float getDistanceSquared(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        float distance = getDistance(obj1, obj2);
        return distance * distance;
        //return getDistanceSquared(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link CombatEntityAPI} and a
     * {@link Vector2f} (includes collision radius).
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)
     * @deprecated Use {@link
     * MathUtils#getDistance(com.fs.starfarer.api.combat.CombatEntityAPI,
     * org.lwjgl.util.vector.Vector2f)} instead. With the addition of collision
     * radius checking, there's no way to avoid calculating the square root.
     * @since 1.0
     */
    @Deprecated
    public static float getDistanceSquared(CombatEntityAPI entity, Vector2f vector)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        float distance = getDistance(entity, vector);
        return distance * distance;
        //return getDistanceSquared(entity.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link Vector2f}s (avoids a costly sqrt()).
     *
     * When comparing distances, use this function instead of
     * {@link MathUtils#getDistance(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)}.
     *
     * @return The distance squared between the two vectors.
     * @since 1.0
     */
    public static float getDistanceSquared(Vector2f vector1, Vector2f vector2)
    {
        float a = vector1.x - vector2.x, b = vector1.y - vector2.y;
        return (a * a) + (b * b);
    }

    /**
     * Returns a normalized {@link Vector2f} pointing from {@code source} to {@code destination}.
     *
     * @param source The origin of the vector.
     * @param destination The location to point at.
     * @return A normalized {@link Vector2f} pointing at {@code destination}.
     * @since 1.0
     */
    public static Vector2f getDirectionalVector(Vector2f source, Vector2f destination)
    {
        Vector2f dir = Vector2f.sub(destination, source, null);

        // Avoid crash with identical vectors
        if (dir.x == 0 && dir.y == 0)
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
     * @return A value between 0 and 360 degrees.
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
     * @return The facing (angle) of {@code vector}.
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
     * @param to The {@link Vector2f} to get the angle to.
     * @return The angle from {@code from} to {@code to}.
     * @since 1.0
     */
    public static float getAngle(Vector2f from, Vector2f to)
    {
        return getFacing(getDirectionalVector(from, to));
    }

    /**
     * Returns a point along the circumference of a circle at the given angle.
     *
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @param angle The angle, in degrees, to get the point at.
     * @return A {@link Vector2f} at [@code angle} degrees along the
     * circumference of the given circle.
     * @since 1.0
     */
    public static Vector2f getPointOnCircumference(Vector2f center, float radius, float angle)
    {
        angle = clampAngle(angle);

        // Bypass relatively expensive trig operations whenever possible
        if (angle == 0f)
        {
            return new Vector2f(center.x + radius, center.y);
        }
        if (angle == 90f)
        {
            return new Vector2f(center.x, center.y - radius);
        }
        if (angle == 180f)
        {
            return new Vector2f(center.x - radius, center.y);
        }
        if (angle == 270f)
        {
            return new Vector2f(center.x, center.y + radius);
        }

        double rad = Math.toRadians(angle);
        return new Vector2f((float) FastTrig.cos(rad) * radius
                + (center == null ? 0f : center.x),
                (float) FastTrig.sin(rad) * radius
                + (center == null ? 0 : center.y));
    }

    /**
     * Returns a random point along the circumference of a circle.
     *
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @return A random point along the circumference of the given circle.
     * @see MathUtils#getPointOnCircumference(org.lwjgl.util.vector.Vector2f,
     * float, float)
     * @since 1.0
     */
    public static Vector2f getRandomPointOnCircumference(Vector2f center, float radius)
    {
        return getPointOnCircumference(center, radius, (float) Math.random() * 360f);
    }

    /**
     * Returns a random point inside of a circle with uniform distribution.
     *
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @return A random point inside of the given circle.
     * @since 1.0
     */
    public static Vector2f getRandomPointInCircle(Vector2f center, float radius)
    {
        double t = 2 * Math.PI * Math.random(),
                u = Math.random() + Math.random(),
                r = (u > 1 ? 2 - u : u);
        return new Vector2f((float) (r * FastTrig.cos(t)) * radius
                + (center == null ? 0f : center.x),
                (float) (r * FastTrig.sin(t)) * radius
                + (center == null ? 0f : center.y));
        //return getRandomPointOnCircumference(center, (float) (radius * Math.random()));
    }

    /**
     * Returns an evenly distributed {@link List} of points along a circle's circumference.
     *
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @param numPoints How many points to generate.
     * @param angleOffset The offset angle of the initial point.
     * @return A {@link List} of {@link Vector2f}s that are evenly distributed
     * along the circle's circumference.
     * @since 1.0
     */
    public static List<Vector2f> getPointsAlongCircumference(Vector2f center,
            float radius, int numPoints, float angleOffset)
    {
        angleOffset %= 360;

        List<Vector2f> points = new ArrayList(numPoints);
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
     * @param point The {@link Vector2f} to check.
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @return {@code true} if {@link point} is within the circle,
     * {@code false} otherwise.
     * @since 1.0
     */
    public static boolean isPointWithinCircle(Vector2f point, Vector2f center, float radius)
    {
        float a = point.x - (center == null ? 0f : center.x),
                b = point.y - (center == null ? 0f : center.y);
        return (a * a) + (b * b) < (radius * radius);
    }

    /**
     * Returns a {@link List} of evenly spaced {@link Vector2f}s inside a circle.
     *
     * WARNING: be VERY conservative using this method - a radius of 250 and a spacing
     * of 5 will result in 10,000 circle checks and 7,825 {@link Vector2f}s created!
     *
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @param spaceBetweenPoints How much space should be between each point.
     * @return A {@link List} of evenly spaced {@link Vector2f}s inside a circle.
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
        List<Vector2f> points = new ArrayList(toAllocate);

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
     * @return A random {@link Float} between {@code min} and {@code max}.
     * @since 1.4
     */
    public static float getRandomNumberInRange(float min, float max)
    {
        if (min > max)
        {
            float tmp = min;
            min = max;
            max = tmp;
        }

        return rng.nextFloat() * (max - min) + min;
    }

    /**
     * Tests for near-equality of floating point numbers.
     *
     * @param a The first float to compare.
     * @param b The second float to compare.
     * @return {@code true} if {@code a} and {@code b} are within 99.99999%
     * of eachother, {@code false} otherwise.
     */
    public static boolean equals(float a, float b)
    {
        if (a == b)
        {
            return true;
        }

        return (a >= b * (.9999999f) && a <= b * (1.0000001f));
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
        /*System.out.println("LazyLib - using deprecated method "
         + MathUtils.class.getCanonicalName()
         + ":isPointWithinBounds(Vector2f point, CombatEntityAPI entity)");*/
        return CollisionUtils.isPointWithinBounds(point, entity);
    }

    private MathUtils()
    {
    }
}
