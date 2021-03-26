package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.util.vector.Vector2f;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public static final float FPI = (float) Math.PI;
    public static final float FTAU = (float) (Math.PI * 2.0);

    /**
     * Returns the distance between two {@link SectorEntityToken}s,
     * taking interaction radii into account.
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
     * {@link Vector2f}, taking interaction radius into account.
     *
     * @see MathUtils#getDistance(Vector2f, Vector2f)
     * @since 1.1
     */
    public static float getDistance(SectorEntityToken token, Vector2f loc)
    {
        return Math.max(0f, getDistance(token.getLocation(), loc) - token.getRadius());
    }

    /**
     * Returns the distance between two {@link CombatEntityAPI}s, taking
     * collision radii into account.
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
     * {@link Vector2f}, taking collision radius into account.
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
     * <p>
     * For comparing distances, it is <i>vastly</i> more efficient to use
     * {@link MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f,
     * org.lwjgl.util.vector.Vector2f)}.
     *
     * @return The distance between the two vectors.
     *
     * @since 1.0
     */
    public static float getDistance(Vector2f loc1, Vector2f loc2)
    {
        final float a = loc1.x - loc2.x, b = loc1.y - loc2.y;
        return (float) Math.hypot(a, b);
    }

    /**
     * Returns the distance squared between two {@link SectorEntityToken}s,
     * taking interaction radii into account.
     * <p>
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
     * a {@link Vector2f}, taking interaction radius into account.
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
     * taking collision radii into account.
     * <p>
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
     * {@link Vector2f}, taking collision radius into account.
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
     * <p>
     * When comparing distances, use this function instead of
     * {@link MathUtils#getDistance(Vector2f, Vector2f)}.
     *
     * @return The distance squared between the two vectors.
     *
     * @since 1.0
     */
    public static float getDistanceSquared(Vector2f loc1, Vector2f loc2)
    {
        final float a = loc1.x - loc2.x, b = loc1.y - loc2.y;
        return (a * a) + (b * b);
    }

    /**
     * Check if two {@link SectorEntityToken}s are within a certain distance of
     * each other, taking interaction radii into account.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(SectorEntityToken, SectorEntityToken)},
     * and should be used whenever possible.
     *
     * @param token1 The first {@link SectorEntityToken} to check.
     * @param token2 The second {@link SectorEntityToken} to check.
     * @param range  The minimum distance between {@code token1} and
     *               {@code token2}.
     *
     * @return Whether {@code token1} is within {@code range} su of
     *         {@code token2}.
     *
     * @since 1.8
     */
    public static boolean isWithinRange(SectorEntityToken token1, SectorEntityToken token2, float range)
    {
        return isWithinRange(token1.getLocation(), token2.getLocation(),
                (range + token1.getRadius() + token2.getRadius()));
    }

    /**
     * Check if a {@link SectorEntityToken} is within a certain distance of a
     * location, taking interaction radius into account.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(SectorEntityToken, Vector2f)},
     * and should be used whenever possible.
     *
     * @param token The {@link SectorEntityToken} to check.
     * @param loc   The {@link Vector2f} to check.
     * @param range The minimum distance between {@code token} and {@code loc}.
     *
     * @return Whether {@code token} is within {@code range} su of {@code loc}.
     *
     * @since 1.8
     */
    public static boolean isWithinRange(SectorEntityToken token, Vector2f loc, float range)
    {
        return isWithinRange(token.getLocation(), loc, (range + token.getRadius()));
    }

    /**
     * Check if two {@link CombatEntityAPI}s are within a certain distance of
     * each other, taking collision radii into account.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(CombatEntityAPI, CombatEntityAPI)},
     * and should be used whenever possible.
     *
     * @param entity1 The first {@link CombatEntityAPI} to check.
     * @param entity2 The second {@link CombatEntityAPI} to check.
     * @param range   The minimum distance between {@code entity1} and
     *                {@code entity2}.
     *
     * @return Whether {@code entity1} is within {@code range} su of
     *         {@code entity2}.
     *
     * @since 1.8
     */
    public static boolean isWithinRange(CombatEntityAPI entity1, CombatEntityAPI entity2, float range)
    {
        return isWithinRange(entity1.getLocation(), entity2.getLocation(),
                (range + entity1.getCollisionRadius() + entity2.getCollisionRadius()));
    }

    /**
     * Check if a {@link CombatEntityAPI} is within a certain distance of a
     * location, taking collision radius into account.
     * <p>
     * This is <i>vastly</i> more efficient than comparisons using
     * {@link MathUtils#getDistance(CombatEntityAPI, Vector2f)},
     * and should be used whenever possible.
     *
     * @param entity The {@link CombatEntityAPI} to check.
     * @param loc    The {@link Vector2f} to check.
     * @param range  The minimum distance between {@code entity} and
     *               {@code loc}.
     *
     * @return Whether {@code entity} is within {@code range} su of {@code loc}.
     *
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
     *
     * @return Whether {@code loc1} is within {@code range} su of {@code loc2}.
     *
     * @since 1.8
     */
    public static boolean isWithinRange(Vector2f loc1, Vector2f loc2, float range)
    {
        return (getDistanceSquared(loc1, loc2) <= (range * range));
    }

    // TODO: Test, rename, Javadoc, add to changelog
    static float renormalize(float num, float min, float max, float oldMin, float oldMax)
    {
        return ((max - min) / (oldMax - oldMin)) * (num - oldMin) + min;
    }

    // TODO: Test, rename, Javadoc, add to changelog
    static float renormalize(float num, float min, float max)
    {
        return (max - min) * num + min;
    }

    /**
     * Clamps an angle within 360 degrees (ex: 400 degrees becomes 40 degrees).
     *
     * @param angle The angle to be clamped.
     *
     * @return A value between 0 and 360 degrees.
     *
     * @since 1.2
     */
    public static float clampAngle(float angle)
    {
        if ((angle < -360) || (angle >= 360))
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
     * Clamps a {@link Float}, ensuring it falls within a given range.
     *
     * @param toClamp The {@link Float} to be clamped.
     * @param min     The minimum value you wish the return value to hold.
     * @param max     The maximum value you wish the return value to hold.
     *
     * @return {@code toClamp} if it falls between {@code min} and {@code max},
     *         {@code min} if it falls below, or {@code max} if it falls above.
     *
     * @since 2.3
     */
    public static float clamp(float toClamp, float min, float max)
    {
        if (max < min) return clamp(toClamp, max, min);
        return Math.max(min, Math.min(max, toClamp));
    }

    /**
     * Clamps an {@link Integer}, ensuring it falls within a given range.
     *
     * @param toClamp The {@link Integer} to be clamped.
     * @param min     The minimum value you wish the return value to hold.
     * @param max     The maximum value you wish the return value to hold.
     *
     * @return {@code toClamp} if it falls between {@code min} and {@code max},
     *         {@code min} if it falls below, or {@code max} if it falls above.
     *
     * @since 2.3
     */
    public static int clamp(int toClamp, int min, int max)
    {
        if (max < min) return clamp(toClamp, max, min);
        return Math.max(min, Math.min(max, toClamp));
    }

    /**
     * Returns the direction and length of the quickest rotation between two
     * angles.
     *
     * @param currAngle The current facing.
     * @param destAngle The facing to rotate towards.
     *
     * @return The shortest distance between the two angles, in degrees.
     *
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
     *
     * @param point1 The first point.
     * @param point2 The second point.
     *
     * @return A {@link Vector2f} at the midpoint of the line between
     *         {@code point1} and {@code point2}.
     *
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
     *
     * @return A {@link Vector2f} at [@code angle} degrees along the
     *         circumference of the given circle.
     *
     * @since 1.0
     */
    public static Vector2f getPointOnCircumference(@Nullable Vector2f center, float radius, float angle)
    {
        if (radius == 0f)
        {
            return (center == null ? new Vector2f(0f, 0f) : new Vector2f(center));
        }

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
     * Returns a point along the circumference of a circle at the given angle. Provided as an alias for
     * {@link MathUtils#getPointOnCircumference(Vector2f, float, float)}.
     *
     * @param center The center point of the circle (can be null for a 0, 0 origin).
     * @param radius The radius of the circle.
     * @param angle  The angle, in degrees, to get the point at.
     *
     * @return A {@link Vector2f} at {@code angle} degrees along the
     *         circumference of the given circle.
     *
     * @see MathUtils#getPointOnCircumference(Vector2f, float, float)
     * @since 2.0
     */
    public static Vector2f getPoint(@Nullable Vector2f center, float radius, float angle)
    {
        return getPointOnCircumference(center, radius, angle);
    }

    /**
     * Returns a random point along the circumference of a circle.
     *
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     *
     * @return A random point along the circumference of the given circle.
     *
     * @see MathUtils#getPointOnCircumference(Vector2f, float, float)
     * @since 1.0
     */
    public static Vector2f getRandomPointOnCircumference(@Nullable Vector2f center, float radius)
    {
        return getPointOnCircumference(center, radius, rng.nextFloat() * 360f);
    }

    /**
     * Returns a random point inside of a circle with uniform distribution.
     *
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     *
     * @return A random point inside of the given circle.
     *
     * @since 1.0
     */
    public static Vector2f getRandomPointInCircle(@Nullable Vector2f center, float radius)
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

    /**
     * Returns a random point inside of a circular sector (2d cone) with uniform
     * distribution.
     *
     * @param center   The center point of the cone (can be null for a 0, 0
     *                 origin).
     * @param radius   The radius of the cone.
     * @param minAngle The minimum angular bounds.
     * @param maxAngle The maximum angular bounds.
     *
     * @return A random point inside of the given circular sector.
     *
     * @since 1.7
     */
    public static Vector2f getRandomPointInCone(@Nullable Vector2f center, float radius,
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
     *
     * @return A random {@link Vector2f} along the line between
     *         {@code lineStart} and {@code lineEnd}.
     *
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
     *
     * @return A {@link List} of {@link Vector2f}s that are evenly distributed
     *         along the circle's circumference.
     *
     * @since 1.0
     */
    public static List<Vector2f> getPointsAlongCircumference(@Nullable Vector2f center,
                                                             float radius, int numPoints, float angleOffset)
    {
        angleOffset = (float) Math.toRadians(angleOffset);

        // Precalculate the sine and cosine
        // Instead of recalculating sin/cos for each line segment,
        // this algorithm rotates the line around the center point
        final float theta = 2f * 3.1415926f / (float) numPoints;
        final float cos = (float) FastTrig.cos(theta);
        final float sin = (float) FastTrig.sin(theta);

        final float centerX = (center == null ? 0f : center.x);
        final float centerY = (center == null ? 0f : center.y);

        // Offset starting angle
        float x = (float) (radius * FastTrig.cos(angleOffset));
        float y = (float) (radius * FastTrig.sin(angleOffset));
        float tmp;

        List<Vector2f> points = new ArrayList<>();
        for (int i = 0; i < numPoints - 1; i++)
        {
            points.add(new Vector2f(x + centerX, y + centerY));

            // Apply the rotation matrix
            tmp = x;
            x = (cos * x) - (sin * y);
            y = (sin * tmp) + (cos * y);
        }
        points.add(new Vector2f(x + centerX, y + centerY));
        return points;
    }

    /**
     * Checks whether a point is on or within the bounds of a circle.
     *
     * @param point  The {@link Vector2f} to check.
     * @param center The center point of the circle (can be null for a 0, 0
     *               origin).
     * @param radius The radius of the circle.
     *
     * @return {@code true} if {@code point} is on or within the circle,
     *         {@code false} otherwise.
     *
     * @since 1.0
     */
    public static boolean isPointWithinCircle(Vector2f point, @Nullable Vector2f center, float radius)
    {
        final float a = point.x - (center == null ? 0f : center.x),
                b = point.y - (center == null ? 0f : center.y);
        return (a * a) + (b * b) <= (radius * radius);
    }

    /**
     * Check if a point is along the line between two {@link Vector2f}s.
     * Accurate to within 1/100 of a unit.
     *
     * @param point     The point to check.
     * @param lineStart The starting point of the line.
     * @param lineEnd   The end point of the line.
     *
     * @return {@code true} if the point is along the line, {@code false}
     *         otherwise.
     *
     * @since 1.6
     */
    public static boolean isPointOnLine(Vector2f point, Vector2f lineStart, Vector2f lineEnd)
    {
        return (Line2D.Float.ptSegDistSq(lineStart.x, lineStart.y,
                lineEnd.x, lineEnd.y, point.x, point.y) <= 0.0001);
    }

    /**
     * Given a point and a line, returns the nearest point on said line to that point.
     *
     * @param source    The point to test distance from.
     * @param lineStart The start point of the line to check.
     * @param lineEnd   The end point of the line to check.
     *
     * @return The point on the line between {@code lineStart} and {@code lineEnd} nearest to {@code source}.
     *
     * @author Alex Mosolov (http://fractalsoftworks.com/forum/index.php?topic=5061.msg229605#msg229605)
     * @since 2.3
     */
    public static Vector2f getNearestPointOnLine(Vector2f source, Vector2f lineStart, Vector2f lineEnd)
    {
        float u = (source.x - lineStart.x) * (lineEnd.x - lineStart.x)
                + (source.y - lineStart.y) * (lineEnd.y - lineStart.y);
        float denom = Vector2f.sub(lineEnd, lineStart, new Vector2f()).length();
        denom *= denom;

        u /= denom;

        // if closest point on line is outside the segment, clamp to on the segment
        if (u < 0) u = 0;
        if (u > 1) u = 1;

        Vector2f i = new Vector2f();
        i.x = lineStart.x + u * (lineEnd.x - lineStart.x);
        i.y = lineStart.y + u * (lineEnd.y - lineStart.y);
        return i;
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
     *
     * @return A {@link List} of evenly spaced {@link Vector2f}s inside a
     *         circle.
     *
     * @since 1.4
     */
    public static List<Vector2f> getEquidistantPointsInsideCircle(@Nullable Vector2f center, float radius, float spaceBetweenPoints)
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
     * Returns a random float within a given range.
     *
     * @param min The minimum value to select.
     * @param max The maximum value to select.
     *
     * @return A random {@link Float} between {@code min} and {@code max}.
     *
     * @since 1.4
     */
    public static float getRandomNumberInRange(float min, float max)
    {
        return rng.nextFloat() * (max - min) + min;
    }

    /**
     * Returns a random integer within a given range.
     *
     * @param min The minimum value to select.
     * @param max The maximum value to select (inclusive).
     *
     * @return A random {@link Integer} between {@code min} and {@code max},
     *         inclusive.
     *
     * @since 2.0
     */
    public static int getRandomNumberInRange(int min, int max)
    {
        if (min >= max)
        {
            if (min == max)
            {
                return min;
            }

            return rng.nextInt((min - max) + 1) + max;
        }

        return rng.nextInt((max - min) + 1) + min;
    }

    /**
     * Tests for near-equality of floating point numbers.
     *
     * @param a The first float to compare.
     * @param b The second float to compare.
     *
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
     *
     * @since 1.5
     */
    public static Random getRandom()
    {
        return rng;
    }

    private MathUtils()
    {
    }
}
