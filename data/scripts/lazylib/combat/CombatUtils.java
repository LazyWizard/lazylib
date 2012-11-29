package data.scripts.lazylib.combat;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.geom.Line;

public class CombatUtils
{
    public static enum DefenseType
    {
        HULL,
        ARMOR,
        SHIELD,
        PHASE,
        MISS
    }

    public static DefenseType getDefenseAimedAt(ShipAPI threatened, WeaponAPI weapon)
    {
        // TODO: Replace with weapon.getOrigin() equivalent, if/when added
        Line weaponFire = new Line(weapon., 3);

        ShipSystemAPI cloak = (ShipSystemAPI) threatened.getPhaseCloak();
        if (cloak != null && cloak.isActive())
        {
            return DefenseType.PHASE;
        }

        ShieldAPI shield = threatened.getShield();
        if (shield != null && shield.isOn())
        {

            return DefenseType.SHIELD;
        }

        return DefenseType.HULL;
    }

    public static float getTimeToAim(ShipAPI ship, WeaponAPI weapon, Vector2f aimAt)
    {
        // Placeholder - add current turn velocity and acceleration later
        float time = weapon.distanceFromArc(aimAt)
                / ship.getMutableStats().getMaxTurnRate().getModifiedValue();

        // Divide by zero - ship can't turn, only a threat if already aimed
        if (Float.isNaN(time))
        {
            if (weapon.distanceFromArc(aimAt) == 0)
            {
                return 0f;
            }
            else
            {
                return Float.MAX_VALUE;
            }
        }

        return time;
    }

    public static float getDistance(Vector2f vector1, Vector2f vector2)
    {
        float a = vector1.x - vector2.x;
        float b = vector1.y - vector2.y;

        return (float) Math.hypot(a, b);
    }

    public static float getDistance(CombatEntityAPI obj1, CombatEntityAPI obj2)
    {
        return getDistance(obj1.getLocation(), obj2.getLocation());
    }
}
