package org.lazywizard.lazylib;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import org.lazywizard.lazylib.geom.FastTrig;
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

        if (!(dir.x == 0 && dir.y == 0))
        {
            dir = dir.normalise(null);
        }

        return dir;
    }

    public static Vector2f getDirectionalVector(CombatEntityAPI source, Vector2f destination)
    {
        return getDirectionalVector(source.getLocation(), destination);
    }

    public static Vector2f getDirectionalVector(CombatEntityAPI source, CombatEntityAPI destination)
    {
        return getDirectionalVector(source.getLocation(), destination.getLocation());
    }

    public static Vector2f getPointOnCircumference(Vector2f center, float radius, float angle)
    {
        float rad = (float) Math.toRadians(angle);

        return new Vector2f((float) FastTrig.cos(rad) * radius + center.x,
                (float) FastTrig.sin(rad) * radius + center.y);
    }

    public static Vector2f getRandomPointOnCircumference(Vector2f center, float radius)
    {
        return getPointOnCircumference(center, radius, (float) Math.random() * 360);
    }

    public static Vector2f getRandomPointInCircle(Vector2f center, float radius)
    {
        // TODO: choose a more uniform distribution method
        return getRandomPointOnCircumference(center,
                (float) (radius * Math.random()));
    }
}
