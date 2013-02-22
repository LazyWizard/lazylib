package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.util.*;
import org.lazywizard.lazylib.geom.FastTrig;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with vectors, angles, distances, circles and bounds.
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
     * Returns the distance between two {@link CombatEntityAPI}s.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return getDistance(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * Returns the distance between a {@link CombatEntityAPI} and a {@link Vector2f}.
     *
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(CombatEntityAPI entity, Vector2f vector)
    {
        return getDistance(entity.getLocation(), vector);
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
     * Returns the distance squared between two {@link CombatEntityAPI}s.
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return getDistanceSquared(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * Returns the distance squared between a {@link CombatEntityAPI} and a {@link Vector2f}.
     *
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(CombatEntityAPI entity, Vector2f vector)
    {
        return getDistanceSquared(entity.getLocation(), vector);
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
     * Returns the facing of a {@link Vector2f}.
     *
     * @param vector The vector to get the facing of.
     * @return The facing (angle) of {@code vector}.
     */
    public static float getFacing(Vector2f vector)
    {
        double angle = Math.toDegrees(Math.atan2(vector.y, vector.x));

        if ((angle < -360) || (angle > 360))
        {
            angle = angle % 360;
        }

        if (angle < 0)
        {
            angle = 360 + angle;
        }

        return (float) angle;
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
        double rad = Math.toRadians(angle);

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

        List<Vector2f> points = new ArrayList();
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
     * Checks if a point is inside of the bounds of a {@link CombatEntityAPI}.
     *
     * @param point The {@link Vector2f} to check.
     * @param entity The {@link CombatEntityAPI} whose {@link BoundsAPI} we are checking against.
     * @return {@code true} if {@link point} is within the bounds of {@code entity}, {@code false} otherwise.
     */
    public static boolean isPointWithinBounds(Vector2f point, CombatEntityAPI entity)
    {
        if (entity.getExactBounds() == null)
        {
            return isPointWithinCircle(point, entity.getLocation(),
                    entity.getCollisionRadius());
        }
        BoundsAPI bounds = entity.getExactBounds();
        bounds.update(entity.getLocation(), entity.getFacing());
        //Polygon poly = Convert.boundsToPolygon(bounds);
        //return poly.contains(point.x, point.y);

        // TODO: Test this thoroughly!
        List<SegmentAPI> segments = bounds.getSegments();
        List<Vector2f> points = new ArrayList();
        for (int x = 0; x < segments.size(); x++)
        {
            points.add(segments.get(x).getP1());

            if (x == (segments.size() - 1))
            {
                points.add(segments.get(x).getP2());
            }
        }

        int i, j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++)
        {
            if ((points.get(i).y > point.y) != (points.get(j).y > point.y)
                    && (point.x < (points.get(j).x - points.get(i).x)
                    * (point.y - points.get(i).y)
                    / (points.get(j).y - points.get(i).y) + points.get(i).x))
            {
                result = !result;
            }
        }
        return result;
    }

    public static void main(String[] args)
    {
        for (int x = 0; x < 50; x++)
        {
            if (x % 5 == 0)
            {
                System.out.println();
            }

            System.out.print(MathUtils.getRandomPointInCircle(new Vector2f(100, 0),
                    50).toString() + " ");
        }

        System.out.println("\n");

        Vector2f v1 = new Vector2f(5, 15), v2 = new Vector2f(0,-3.5f);
        System.out.println("Distance: " + getDistance(v1, v2) + " | " + getDistance(v2, v1));
        System.out.println("Distance squared: " + getDistanceSquared(v1, v2) + " | " + getDistanceSquared(v2, v1));
    }

    private MathUtils()
    {
    }
}
