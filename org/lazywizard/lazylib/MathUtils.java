package org.lazywizard.lazylib;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.util.*;
import org.lazywizard.lazylib.geom.FastTrig;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with vectors, angles, distances, circles and bounds
 *
 * @author LazyWizard
 */
public class MathUtils
{
    /**
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(SectorEntityToken token1, SectorEntityToken token2)
    {
        return getDistance(token1.getLocation(), token2.getLocation());
    }

    /**
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return getDistance(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * @see MathUtils#getDistance(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistance(CombatEntityAPI entity, Vector2f vector)
    {
        return getDistance(entity.getLocation(), vector);
    }

    /**
     * Returns the distance between two vectors.
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
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(SectorEntityToken token1, SectorEntityToken token2)
    {
        return getDistanceSquared(token1.getLocation(), token2.getLocation());
    }

    /**
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return getDistanceSquared(obj1.getLocation(), obj2.getLocation());
    }

    /**
     * @see MathUtils#getDistanceSquared(org.lwjgl.util.vector.Vector2f, org.lwjgl.util.vector.Vector2f)
     */
    public static float getDistanceSquared(CombatEntityAPI entity, Vector2f vector)
    {
        return getDistanceSquared(entity.getLocation(), vector);
    }

    /**
     * Returns the distance squared between two vectors (avoids a costly sqrt()).
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

    public static Vector2f getDirectionalVector(Vector2f source, Vector2f destination)
    {
        Vector2f dir = Vector2f.sub(destination, source, null);

        if (dir.x == 0 && dir.y == 0)
        {
            return dir;
        }

        return dir.normalise(null);
    }

    public static Vector2f getDirectionalVector(CombatEntityAPI source, Vector2f destination)
    {
        return getDirectionalVector(source.getLocation(), destination);
    }

    public static Vector2f getDirectionalVector(CombatEntityAPI source, CombatEntityAPI destination)
    {
        return getDirectionalVector(source.getLocation(), destination.getLocation());
    }

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

    public static float getAngle(Vector2f from, Vector2f to)
    {
        return getFacing(getDirectionalVector(from, to));
    }

    public static Vector2f getPointOnCircumference(Vector2f center, float radius, float angle)
    {
        double rad = Math.toRadians(angle);

        return new Vector2f((float) FastTrig.cos(rad) * radius + center.x,
                (float) FastTrig.sin(rad) * radius + center.y);
    }

    public static Vector2f getRandomPointOnCircumference(Vector2f center, float radius)
    {
        return getPointOnCircumference(center, radius, (float) Math.random() * 360);
    }

    public static Vector2f getRandomPointInCircle(Vector2f center, float radius)
    {
        double t = 2 * Math.PI * Math.random(),
                u = Math.random() + Math.random(),
                r = (u > 1 ? 2 - u : u);
        return new Vector2f((float) (r * FastTrig.cos(t)) * radius + center.x,
                (float) (r * FastTrig.sin(t)) * radius + center.y);
        //return getRandomPointOnCircumference(center, (float) (radius * Math.random()));
    }

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

    public static boolean isPointWithinCircle(Vector2f point, Vector2f center, float radius)
    {
        float a = point.x - center.x, b = point.y - center.y;
        return (a * a) + (b * b) < (radius * radius);
    }

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
            /*CombatUtils.getCombatEngine().addHitParticle(
             segments.get(x).getP1(), entity.getVelocity(),
             5f, 1f, 2.5f, Color.YELLOW);*/

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
    }

    private MathUtils()
    {
    }
}
