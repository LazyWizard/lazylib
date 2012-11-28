package data.scripts.lazylib.combat;

import com.fs.starfarer.api.combat.ShipAPI;
import org.lwjgl.util.vector.Vector2f;

public class CombatUtils
{
    public static float getDistance(Vector2f vector1, Vector2f vector2)
    {
        float a = vector1.x - vector2.x;
        float b = vector1.y - vector2.y;

        return (float) Math.hypot(a, b);
    }

    public static float getDistance(ShipAPI ship1, ShipAPI ship2)
    {
        return getDistance(ship1.getLocation(), ship2.getLocation());
    }
}
