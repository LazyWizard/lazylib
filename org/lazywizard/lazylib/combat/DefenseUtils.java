package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lazywizard.lazylib.CollisionUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods to make working with a ship's armor grid and defenses
 * easier.
 *
 * @author LazyWizard
 * @since 1.5
 */
public class DefenseUtils
{
    /**
     * A constant that represents a point not in a ship's armor grid.
     */
    public static final float NOT_IN_GRID = -12345.9876f;

    /**
     * Get the armor value of a {@link ShipAPI} at a location. Equivalent
     * to {@link ArmorGridAPI#getArmorValue(int, int)}, but using
     * world-space coordinates.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc  The world location we will be checking the armor value at.
     * <p>
     * @return The armor value at {@code loc}, or
     *         {@link DefenseUtils#NOT_IN_GRID}
     *         if the point isn't within {@code ship}'s {@link ArmorGridAPI}.
     * <p>
     * @since 1.5
     */
    public static float getArmorValue(ShipAPI ship, Vector2f loc)
    {
        int[] cell = ship.getArmorGrid().getCellAtLocation(loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return ship.getArmorGrid().getArmorValue(cell[0], cell[1]);
    }

    /**
     * Get the total damage taken by a {@link ShipAPI}'s armor at a location.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc  The world location we will be checking the armor damage at.
     * <p>
     * @return The armor damage taken at {@code loc}, or
     *         {@link DefenseUtils#NOT_IN_GRID} if the point isn't within
     *         {@code ship}'s {@link ArmorGridAPI}.
     * <p>
     * @since 1.5
     */
    public static float getArmorDamage(ShipAPI ship, Vector2f loc)
    {
        int[] cell = ship.getArmorGrid().getCellAtLocation(loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getMaxArmorInCell()
                - ship.getArmorGrid().getArmorValue(cell[0], cell[1]));
    }

    /**
     * Get the armor level of a {@link ShipAPI} at a location. Equivalent
     * to {@link ArmorGridAPI#getArmorFraction(int, int)}, but using
     * world-space coordinates.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc  The world location we will be checking the armor level at.
     * <p>
     * @return The armor level at {@code loc}, or
     *         {@link DefenseUtils#NOT_IN_GRID}
     *         if the point isn't within {@code ship}'s {@link ArmorGridAPI}.
     * <p>
     * @since 1.5
     */
    public static float getArmorLevel(ShipAPI ship, Vector2f loc)
    {
        int[] cell = ship.getArmorGrid().getCellAtLocation(loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getArmorFraction(cell[0], cell[1]));
    }

    /**
     * Determine what {@link DefenseType} is present at a specific location
     * on a {@link ShipAPI}. This method will consider points not inside of the
     * ship's bounds as a miss even if said point is still within the armor
     * grid.
     *
     * @param ship The {@link ShipAPI} to examine.
     * @param loc  The location to check at.
     * <p>
     * @return The {@link DefenseType} present at {@code loc}.
     * <p>
     * @since 1.5
     */
    public static DefenseType getDefenseAtPoint(ShipAPI ship, Vector2f loc)
    {
        // Point is not in bounds or ship's phase cloak is active
        if ((ship.getPhaseCloak() != null && ship.getPhaseCloak().isActive())
                || !CollisionUtils.isPointWithinBounds(loc, ship))
        {
            return DefenseType.PHASE_OR_MISS;
        }

        // Shield is active and point is in arc
        ShieldAPI shield = ship.getShield();
        if (shield != null && shield.isOn() && shield.isWithinArc(loc))
        {
            return DefenseType.SHIELD;
        }

        // Armor value at this point is more than 0
        if (getArmorValue(ship, loc) > 0f)
        {
            return DefenseType.ARMOR;
        }

        // No defenses present, just bare hull
        return DefenseType.HULL;
    }

    private DefenseUtils()
    {
    }
}
