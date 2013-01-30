package org.lazywizard.lazylib;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import java.util.*;
import org.lazywizard.lazylib.geom.FastTrig;
import org.lazywizard.lazylib.geom.Polygon;
import org.lwjgl.util.vector.Vector2f;

public class MathUtils
{
    public static float getDistance(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return getDistance(obj1.getLocation(), obj2.getLocation());
    }

    public static float getDistance(CombatEntityAPI entity, Vector2f vector)
    {
        return getDistance(entity.getLocation(), vector);
    }

    public static float getDistance(Vector2f vector1, Vector2f vector2)
    {
        float a = vector1.x - vector2.x;
        float b = vector1.y - vector2.y;
        return (float) Math.hypot(a, b);
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
        return new Vector2f((float) (r * FastTrig.cos(t) + center.x),
                (float) (r * FastTrig.sin(t) + center.y));
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
        return Math.pow(point.x - center.x, 2) + Math.pow(point.y - center.y, 2)
                < Math.pow(radius, 2);
    }

    /*public static boolean isCircleWithinArc(Vector2f circleOrigin, float circleRadius, Vector2f arcOrigin, float arcAngle, float arcSize)
     {
     // TODO
     }*/
    public static void main(String[] args)
    {
        Vector2f vec1 = new Vector2f(0, 0);
        Vector2f vec2 = new Vector2f(0, 1);
        System.out.println("Angle between vectors: " + getAngle(vec1, vec2));
    }

    private MathUtils()
    {
    }
}
