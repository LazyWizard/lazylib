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
public class MathUtils
{
    // This is the internal RNG used by all randomized LazyLib methods
    private static final Random rng = new Random();

    /**
     * Returns the distance between two {@link SectorEntityToken}s,
     * including interaction radii.
     *
     * @see MathUtils#getDistance(Vector2f, Vector2f)
     * @since 1.0
     */
    public static float getDistance(SectorEntityToken token1, SectorEntityToken token2)
    {
        return Math.max(0f, getDistance(token1.getLocation(), token2.getLocation())
                - (token1.getRadius() + token2.getRadius()));
    }

    /**
     * Returns the distance between a {@link SectorEntityToken} and a
     * {@link Vector2f}, including interaction radius.
     *
     * @see MathUtils#getDistance(Vector2f, Vector2f)
     * @since 1.1
     */
    public static float getDistance(SectorEntityToken token, Vector2f loc)
    {
        return Math.max(0f, getDistance(token.getLocation(), loc) - token.getRadius());
    }

    /**
     * Returns the distance between two {@link CombatEntityAPI}s, including
     * collision radii.
     *
     * @see MathUtils#getDistance(Vector2f, Vector2f)
     * @since 1.0
     */
    public static float getDistance(CombatEntityAPI entity1, CombatEntityAPI entity2)
    {
        return Math.max(0f, getDistance(entity1.getLocation(), entity2.getLocation())
                - (entity1.getCollisionRadius() + entity2.getCollisionRadius()));
    }

    /**
     * Returns the distance between a {@link CombatEntityAPI} and a
     * {@link Vector2f}, including collision radius.
     *
     * @see MathUtils#getDistance(Vector2f, Vector2f)
     * @since 1.0
     */
    public static float getDistance(CombatEntityAPI entity, Vector2f loc)
    {
        return Math.max(0f, getDistance(entity.getLocation(), loc)
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
    public static float getDistance(Vector2f loc1, Vector2f loc2)
    {
        final float a = loc1.x - loc2.x, b = loc1.y - loc2.y;
        return (float) Math.hypot(a, b);
    }

    /**
     * Returns the distance squared between two {@link SectorEntityToken}s,
     * including interaction radii.
     *
     * With the addition of collision radius checking, there's no way to avoid
     * calculating the square root.
     * {@link MathUtils#getDistance(SectorEntityToken, SectorEntityToken)}
     * will be just as efficient.
     *
     * @see MathUtils#getDistanceSquared(Vector2f, Vector2f)
     * @since 1.0
     */
    public static float getDistanceSquared(SectorEntityToken token1, SectorEntityToken token2)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        final float distance = getDistance(token1, token2);
        return distance * distance;
        //return getDistanceSquared(token1.getLocation(), token2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link SectorEntityToken} and
     * a {@link Vector2f}, including interaction radius.
     * <p>
     * With the addition of collision radius checking, there's no way to avoid
     * calculating the square root.
     * {@link MathUtils#getDistance(SectorEntityToken, Vector2f)} will be just
     * as efficient.
     *
     * @see MathUtils#getDistanceSquared(Vector2f, Vector2f)
     * @since 1.1
     */
    public static float getDistanceSquared(SectorEntityToken token, Vector2f loc)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        final float distance = getDistance(token, loc);
        return distance * distance;
        //return getDistanceSquared(token.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link CombatEntityAPI}s,
     * including collision radii.
     *
     * With the addition of collision radius checking, there's no way to avoid
     * calculating the square root.
     * {@link MathUtils#getDistance(CombatEntityAPI, CombatEntityAPI)} will be
     * just as efficient.
     *
     * @see MathUtils#getDistanceSquared(Vector2f, Vector2f)
     * @since 1.0
     */
    public static float getDistanceSquared(CombatEntityAPI entity1, CombatEntityAPI entity2)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        final float distance = getDistance(entity1, entity2);
        return distance * distance;
        //return getDistanceSquared(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link CombatEntityAPI} and a
     * {@link Vector2f} (includes collision radius).
     * <p>
     * With the addition of collision radius checking, there's no way to avoid
     * calculating the square root.
     * {@link MathUtils#getDistance(CombatEntityAPI, Vector2f)} will be just
     * as efficient.
     *
     * @see MathUtils#getDistanceSquared(Vector2f, Vector2f)
     * @since 1.0
     */
    public static float getDistanceSquared(CombatEntityAPI entity, Vector2f loc)
    {
        // There's no way to do this while avoiding a sqrt, so might as well...
        final float distance = getDistance(entity, loc);
        return distance * distance;
        //return getDistanceSquared(entity.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link Vector2f}s (avoids a
     * costly sqrt()).
     *
     * When comparing distances, use this function instead of
     * {@link MathUtils#getDistance(Vector2f, Vector2f)}.
     *
     * @return The distance squared between the two vectors.
     * <p>
     * @since 1.0
     */
    public static float getDistanceSquared(Vector2f loc1, Vector2f loc2)
    {
        final float a = loc1.x - loc2.x, b = loc1.y - loc2.y;
        return (a * a) + (b * b);
    }

    /**
     * Check if two {@link SectorEntityToken}s are within a certain distance of
     * each other, including interaction radii.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(SectorEntityToken, SectorEntityToken)},
     * and should be used whenever possible.
     *
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param token2 The second {@link SectorEntityToken} to check.
     * @param range  The minimum distance between {@code token1} and
     *               {@code token2}.
     * <p>
     * @return Whether {@code token1} is within {@code range} su of
     *         {@code token2}.
     * <p>
     * @since 1.8
     */
    public static boolean isWithinRange(SectorEntityToken token1, SectorEntityToken token2, float range)
    {
        return isWithinRange(token1.getLocation(), token2.getLocation(),
                (range + token1.getRadius() + token2.getRadius()));
    }

    /**
     * Check if a {@link SectorEntityToken} is within a certain distance of a
     * location, including interaction radius.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(SectorEntityToken, Vector2f)},
     * and should be used whenever possible.
     *
     * @param token The {@link SectorEntityToken} to check.
     * @param loc   The {@link Vector2f} to check.
     * @param range The minimum distance between {@code token} and {@code loc}.
     * <p>
     * @return Whether {@code token} is within {@code range} su of {@code loc}.
     * <p>
     * @since 1.8
     */
    public static boolean isWithinRange(SectorEntityToken token, Vector2f loc, float range)
    {
        return isWithinRange(token.getLocation(), loc, (range + token.getRadius()));
    }

    /**
     * Check if two {@link CombatEntityAPI}s are within a certain distance of
     * each other, including collision radii.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(CombatEntityAPI, CombatEntityAPI)},
     * and should be used whenever possible.
     *
     * @param entity1 The first {@link CombatEntityAPI} to check.
     * @param entity2 The second {@link CombatEntityAPI} to check.
     * @param range   The minimum distance between {@code entity1} and
     *                {@code entity2}.
     * <p>
     * @return Whether {@code entity1} is within {@code range} su of
     *         {@code entity2}.
     * <p>
     * @since 1.8
     */
    public static boolean isWithinRange(CombatEntityAPI entity1, CombatEntityAPI entity2, float range)
    {
        return isWithinRange(entity1.getLocation(), entity2.getLocation(),
                (range + entity1.getCollisionRadius() + entity2.getCollisionRadius()));
    }

    /**
     * Check if a {@link CombatEntityAPI} is within a certain distance of a
     * location, including collision radius.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(CombatEntityAPI, Vector2f)},
     * and should be used whenever possible.
     *
     * @param entity The {@link CombatEntityAPI} to check.
     * @param loc    The {@link Vector2f} to check.
     * @param range  The minimum distance between {@code entity} and
     *               {@code loc}.
     * <p>
     * @return Whether {@code entity} is within {@code range} su of {@code loc}.
     * <p>
     * @since 1.8
     */
    public static boolean isWithinRange(CombatEntityAPI entity, Vector2f loc, float range)
    {
        return isWithinRange(entity.getLocation(), loc,
                (range + entity.getCollisionRadius()));
    }

    /**
     * Check if two objects are within a certain distance of each other.
     *
     * @param loc1  The first {@link Vector2f}.
     * @param loc2  The second {@link Vector2f}.
     * @param range The minimum distance between {@code loc1} and {@code loc2}.
     * <p>
     * @return Whether {@code loc1} is within {@code range} su of {@code loc2}.
     * <p>
     * @since 1.8
     */
    public static boolean isWithinRange(Vector2f loc1, Vector2f loc2, float range)
    {
        return (getDistanceSquared(loc1, loc2) <= (range * range));
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
     * Returns the point exactly between two other points.
     * <p>
     * @param point1 The first point.
     * @param point2 The second point.
     * <p>
     * @return A {@link Vector2f} at the midpoint of the line between
     *         {@code point1} and {@code point2}.
     * <p>
     * @since 1.9
     */
    public static Vector2f getMidpoint(Vector2f point1, Vector2f point2)
    {
        return new Vector2f((point1.x + point2.x) / 2f,
                (point1.y + point2.y) / 2f);
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
        if (angle == 0f)
        {
            return new Vector2f((center == null ? 0f : center.x) + radius,
                    (center == null ? 0f : center.y));
        }
        if (angle == 90f)
        {
            return new Vector2f((center == null ? 0f : center.x),
                    (center == null ? 0f : center.y) + radius);
        }
        if (angle == 180f)
        {
            return new Vector2f((center == null ? 0f : center.x) - radius,
                    (center == null ? 0f : center.y));
        }
        if (angle == 270f)
        {
            return new Vector2f((center == null ? 0f : center.x),
                    (center == null ? 0f : center.y) - radius);
        }

        final double rad = Math.toRadians(angle);
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
     * @see MathUtils#getPointOnCircumference(Vector2f, float, float)
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
        final double t = 2 * Math.PI * rng.nextDouble(),
                u = rng.nextDouble() + rng.nextDouble(),
                r = (u > 1 ? 2 - u : u);
        return new Vector2f((float) (r * FastTrig.cos(t)) * radius
                + (center == null ? 0f : center.x),
                (float) (r * FastTrig.sin(t)) * radius
                + (center == null ? 0f : center.y));
        //return getRandomPointOnCircumference(center, radius * rng.nextFloat());
    }

    // TODO: Javadoc this
    public static Vector2f getPointOnEllipse(Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset, float angle)
    {
        ellipseAngleOffset = (float) Math.toRadians(ellipseAngleOffset);
        angle = (float) Math.toRadians(angle);
        float sin = (float) FastTrig.sin(angle),
                cos = (float) FastTrig.cos(angle);

        // Get point on unrotated ellipse around origin (0, 0)
        final float x = ellipseWidth * cos;
        final float y = ellipseHeight * sin;

        // Rotate point to match ellipses rotation
        sin = (float) FastTrig.sin(ellipseAngleOffset);
        cos = (float) FastTrig.cos(ellipseAngleOffset);
        return new Vector2f((x * cos) - (y * sin) + ellipseCenter.x,
                (x * sin) + (y * cos) + ellipseCenter.y);
    }

    // TODO
    private static void getRandomPointOnEllipse(Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
    }

    // TODO
    private static void getRandomPointInEllipse(Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
    }

    /**
     * Returns a random point inside of a circular sector (2d cone) with uniform
     * distribution.
     *
     * @param center   The center point of the cone (can be null for a 0, 0
     *                 origin).
     * @param radius   The radius of the cone.
     * @param minAngle The minimum angular bounds.
     * @param maxAngle The maximum angular bounds.
     * <p>
     * @return A random point inside of the given circular sector.
     * <p>
     * @since 1.7
     */
    public static Vector2f getRandomPointInCone(Vector2f center, float radius,
            float minAngle, float maxAngle)
    {
        final double t = Math.toRadians(MathUtils.getRandomNumberInRange(minAngle, maxAngle)),
                u = rng.nextDouble() + rng.nextDouble(),
                r = (u > 1 ? 2 - u : u);
        return new Vector2f((float) (r * FastTrig.cos(t)) * radius
                + (center == null ? 0f : center.x),
                (float) (r * FastTrig.sin(t)) * radius
                + (center == null ? 0f : center.y));
    }

    /**
     * Returns a random point along the line between two {@link Vector2f}s.
     *
     * @param lineStart The starting point of the line.
     * @param lineEnd   The end point of the line.
     * <p>
     * @return A random {@link Vector2f} along the line between
     *         {@code lineStart} and {@code lineEnd}.
     * <p>
     * @since 1.6
     */
    public static Vector2f getRandomPointOnLine(Vector2f lineStart, Vector2f lineEnd)
    {
        final float t = rng.nextFloat();
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

        final List<Vector2f> points = new ArrayList<>(numPoints);
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
        final float a = point.x - (center == null ? 0f : center.x),
                b = point.y - (center == null ? 0f : center.y);
        return (a * a) + (b * b) <= (radius * radius);
    }

    // Also returns true if the point is ON the ellipse
    // TODO: Javadoc this
    public static boolean isPointWithinEllipse(Vector2f point, Vector2f ellipseCenter,
            float ellipseWidth, float ellipseHeight, float ellipseAngleOffset)
    {
        // Move relative to 0, 0 and rotate to match ellipse offset
        Vector2f origin = Vector2f.sub(point, ellipseCenter, null);
        VectorUtils.rotate(origin, -ellipseAngleOffset, origin);

        final float x = (origin.x * origin.x) / (ellipseWidth * ellipseWidth),
                y = (origin.y * origin.y) / (ellipseHeight * ellipseHeight);
        return ((x + y) <= 1.0001f);
    }

    /**
     * Check if a point is along the line between two {@link Vector2f}s.
     * Accurate to within 1/100 of a unit.
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
                lineEnd.x, lineEnd.y, point.x, point.y) <= 0.0001);
    }

    /**
     * Returns a {@link List} of evenly spaced {@link Vector2f}s inside a
     * circle.
     * <p>
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
        final float size = radius / spaceBetweenPoints;
        final int toAllocate = (int) ((size * size * Math.PI) + 0.5f);
        List<Vector2f> points = new ArrayList<>(toAllocate);

        // Support for null vectors
        final float centerX = (center == null ? 0f : center.x);
        final float centerY = (center == null ? 0f : center.y);

        // For every point, check if it is inside the given circle
        float a, b;
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
     * @deprecated Use {@link VectorUtils#getFacing(Vector2f)} instead.
     * @since 1.0
     */
    @Deprecated
    public static float getFacing(Vector2f vector)
    {
        LazyLib.onDeprecatedMethodUsage();

        return VectorUtils.getFacing(vector);
    }

    /**
     * @deprecated Use {@link VectorUtils#getAngle(Vector2f, Vector2f)} instead.
     * @since 1.0
     */
    public static float getAngle(Vector2f from, Vector2f to)
    {
        LazyLib.onDeprecatedMethodUsage();

        return VectorUtils.getAngle(from, to);
    }

    /**
     * @deprecated Use
     * {@link VectorUtils#getDirectionalVector(Vector2f, Vector2f)} instead.
     * @since 1.0
     */
    @Deprecated
    public static Vector2f getDirectionalVector(CombatEntityAPI source, Vector2f destination)
    {
        LazyLib.onDeprecatedMethodUsage();

        return VectorUtils.getDirectionalVector(source.getLocation(), destination);
    }

    /**
     * @deprecated Use
     * {@link VectorUtils#getDirectionalVector(Vector2f, Vector2f)} instead.
     * @since 1.0
     */
    @Deprecated
    public static Vector2f getDirectionalVector(CombatEntityAPI source,
            CombatEntityAPI destination)
    {
        LazyLib.onDeprecatedMethodUsage();

        return VectorUtils.getDirectionalVector(source.getLocation(),
                destination.getLocation());
    }

    /**
     * @deprecated Use
     * {@link VectorUtils#getDirectionalVector(Vector2f, Vector2f)} instead.
     * @since 1.0
     */
    @Deprecated
    public static Vector2f getDirectionalVector(Vector2f source, Vector2f destination)
    {
        LazyLib.onDeprecatedMethodUsage();

        return VectorUtils.getDirectionalVector(source, destination);
    }

    /**
     * @deprecated Use {@link
     * CollisionUtils#isPointWithinBounds(Vector2f, CombatEntityAPI)} instead.
     * @since 1.0
     */
    @Deprecated
    public static boolean isPointWithinBounds(Vector2f point, CombatEntityAPI entity)
    {
        LazyLib.onDeprecatedMethodUsage();

        return CollisionUtils.isPointWithinBounds(point, entity);
    }

    private MathUtils()
    {
    }
}
