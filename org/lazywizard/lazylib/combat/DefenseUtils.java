package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods for working with a ship's defenses.
 *
 * @author LazyWizard
 * @since 1.5
 */
// TODO: Add JavaDoc to this class
public class DefenseUtils
{
    public static DefenseType getDefenseAtPoint(ShipAPI ship, Vector2f loc)
    {
        if (!CollisionUtils.isPointWithinBounds(loc, ship)
                || (ship.getPhaseCloak() != null && ship.getPhaseCloak().isActive()))
        {
            return DefenseType.PHASE_OR_MISS;
        }

        ShieldAPI shield = ship.getShield();
        if (shield != null && shield.isOn() && shield.isWithinArc(loc))
        {
            return DefenseType.SHIELD;
        }

        if (getArmorValue(ship, loc) > 0f)
        {
            return DefenseType.ARMOR;
        }

        return DefenseType.HULL;
    }

    private DefenseUtils()
    {
    }
}
