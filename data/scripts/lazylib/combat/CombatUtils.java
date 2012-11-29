package data.scripts.lazylib.combat;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
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

    // TODO
    public static SegmentAPI getCollisionPoint(CombatEntityAPI target, Line firingLine)
    {
        BoundsAPI bounds = target.getExactBounds();

        // TODO: add code for entities that lack bounds (using collision circle)
        if (bounds == null)
        {
            return null;
        }

        return null;
    }

    public static DefenseType getDefenseAimedAt(ShipAPI threatened, WeaponAPI weapon)
    {
        // TODO: Replace with weapon.getOrigin() equivalent, if/when added
        //Line weaponFire = new Line(weapon., 3);

        // TODO: filter out weapons that can't hit the target (CollisionClass)

        ShipSystemAPI cloak = (ShipSystemAPI) threatened.getPhaseCloak();
        if (cloak != null && cloak.isActive())
        {
            return DefenseType.PHASE;
        }

        ShieldAPI shield = threatened.getShield();
        if (shield != null && shield.isOn())
        {
            // TODO: Shield collision checks
            return DefenseType.SHIELD;
        }

        // TODO: Armor checks

        return DefenseType.MISS;
    }

    public static float getTimeToAim(ShipAPI ship, WeaponAPI weapon, Vector2f aimAt)
    {
        // TODO: add current turn velocity and acceleration to equation
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
