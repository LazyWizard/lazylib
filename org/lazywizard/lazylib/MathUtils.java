package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with vectors, angles, distances, and circles.
 *
 * @author LazyWizard
 */
public class MathUtils
{
    /**
     * Returns the distance between two {@link SectorEntityToken}s.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(SectorEntityToken token1, SectorEntityToken token2)
    {
        return getDistance(token1.getLocation(), token2.getLocation());
    }

    /**
     * Returns the distance between a {@link SectorEntityToken} and a {@link Vector2f).
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(SectorEntityToken token, Vector2f vector)
    {
        return getDistance(token.getLocation(), vector);
    }

    /**
     * Returns the distance between two {@link CombatEntityAPI}s, including collision radius.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
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
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(SectorEntityToken token1, SectorEntityToken token2)
    {
        return getDistanceSquared(token1.getLocation(), token2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link SectorEntityToken} and a {@link Vector2f).
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(SectorEntityToken token, Vector2f vector)
    {
        return getDistanceSquared(token.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two {@link CombatEntityAPI}s (includes collision radii).
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     * @deprecated Use {@link MathUtils#getDistance(com.fs.starfarer.api.combat.CombatEntityAPI,
     * com.fs.starfarer.api.combat.CombatEntityAPI)} instead. With the addition of collision
     * radius checking, there's no way to avoid calculating the square root.
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
     * Returns the distance squared between a {@link CombatEntityAPI} and a {@link Vector2f} (includes collision radius).
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     * @deprecated Use {@link MathUtils#getDistance(com.fs.starfarer.api.combat.CombatEntityAPI,
     * org.lwjgl.util.vector.Vector2f)} instead. With the addition of collision
     * radius checking, there's no way to avoid calculating the square root.
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
     */
    public static Vector2f getDirectionalVector(Vector2f source, Vector2f destination)
    {
        Vector2f dir = Vector2f.sub(destination, source, null);

        if (dir.x == 0 && dir.y == 0)
        {
            return dir;
        }

        return dir.normalise(null);
    }

    /**
     * Returns a normalized {@link Vector2f} pointing from {@code source} to {@code destination}.
     *
     * @see MathUtils#getDirectionalVector(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static Vector2f getDirectionalVector(CombatEntityAPI source, Vector2f destination)
    {
        return getDirectionalVector(source.getLocation(), destination);
    }

    /**
     * Returns a normalized {@link Vector2f} pointing from {@code source} to {@code destination}.
     *
     * @see MathUtils#getDirectionalVector(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static Vector2f getDirectionalVector(CombatEntityAPI source, CombatEntityAPI destination)
    {
        return getDirectionalVector(source.getLocation(), destination.getLocation());
    }

    /**
     * Clamps an angle within 360 degrees (ex: 400 degrees becomes 40 degrees).
     *
     * @param angle The angle to be clamped.
     * @return A value between 0 and 360 degrees.
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
     */
    public static float getAngle(Vector2f from, Vector2f to)
    {
        return getFacing(getDirectionalVector(from, to));
    }

    /**
     * Returns a point along the circumference of a circle at the given angle.
     *
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @param angle The angle, in degrees, to get the point at.
     * @return A {@link Vector2f} at [@code angle} degrees along the circumference of the given circle.
     */
    public static Vector2f getPointOnCircumference(Vector2f center, float radius, float angle)
    {
        double rad = Math.toRadians(clampAngle(angle));

        return new Vector2f((float) FastTrig.cos(rad) * radius + center.x,
                (float) FastTrig.sin(rad) * radius + center.y);
    }

    /**
     * Returns a random point along the circumference of a circle.
     *
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @return A random point along the circumference of the given circle.
     * @see MathUtils#getPointOnCircumference(org.lwjgl.util.vector.Vector2f, float, float)
     */
    public static Vector2f getRandomPointOnCircumference(Vector2f center, float radius)
    {
        return getPointOnCircumference(center, radius, (float) Math.random() * 360);
    }

    /**
     * Returns a random point inside of a circle with uniform distribution.
     *
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @return A random point inside of the given circle.
     */
    public static Vector2f getRandomPointInCircle(Vector2f center, float radius)
    {
        double t = 2 * Math.PI * Math.random(),
                u = Math.random() + Math.random(),
                r = (u > 1 ? 2 - u : u);
        return new Vector2f((float) (r * FastTrig.cos(t)) * radius + center.x,
                (float) (r * FastTrig.sin(t)) * radius + center.y);
        //return getRandomPointOnCircumference(center, (float) (radius * Math.random()));
    }

    /**
     * Returns an evenly distributed {@link List} of points along a circle's circumference.
     *
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @param numPoints How many points to generate.
     * @param angleOffset The offset angle of the initial point.
     * @return A {@link List} of {@link Vector2f}s that are evenly distributed along the circle's circumference.
     */
    public static List<Vector2f> getPointsAlongCircumference(Vector2f center, float radius, int numPoints, float angleOffset)
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
     * @param center The center point of the circle.
     * @param radius The radius of the circle.
     * @return {@code true} if {@link point} is within the circle, {@code false} otherwise.
     */
    public static boolean isPointWithinCircle(Vector2f point, Vector2f center, float radius)
    {
        float a = point.x - center.x, b = point.y - center.y;
        return (a * a) + (b * b) < (radius * radius);
    }

    /**
     * @deprecated Use {@link CollisionUtils#isPointWithinBounds(org.lwjgl.util.vector.Vector2f, com.fs.starfarer.api.combat.CombatEntityAPI)} instead.
     */
    @Deprecated
    public static boolean isPointWithinBounds(Vector2f point, CombatEntityAPI entity)
    {
        return CollisionUtils.isPointWithinBounds(point, entity);
    }

    private MathUtils()
    {
    }
}
