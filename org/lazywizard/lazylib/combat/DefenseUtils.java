package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import java.awt.Color;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods to make working with a ship's armor grid and defenses easier.
 *
 * @author LazyWizard
 * @since 1.5
 */
public class DefenseUtils
{
    /** A constant that represents a point not in a ship's armor grid. */
    public static final float NOT_IN_GRID = -12345.9876f;

    /**
     * Get the {@link ArmorGridAPI} coordinate of a worldspace {@link Vector2f}.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc The world coordinates to be translated to armor grid coordinates.
     * @return A {@link Vector2f} containing the {@link ArmorGridAPI} coordinates
     * equivalent to {@code loc}, or {@code null} if {@code loc} isn't in the
     * grid's bounds.
     * @since 1.5
     */
    public static Vector2f getArmorCellAtWorldCoord(ShipAPI ship, Vector2f loc)
    {
        // Analyze armor grid
        ArmorGridAPI grid = ship.getArmorGrid();
        float sizeX = grid.getGrid().length * grid.getCellSize(),
                sizeY = grid.getGrid()[0].length * grid.getCellSize();
        System.out.println("Grid is " + grid.getGrid().length + "x"
                + grid.getGrid()[0].length + " (" + sizeX + "x" + sizeY
                + " su), " + grid.getCellSize() + " su per cell");
        System.out.println("Location (raw): " + loc);

        // Rotate location to adjust for ship facing
        Vector2f cell = new Vector2f(loc);
        float angle = MathUtils.getAngle(ship.getLocation(), cell)
                - (ship.getFacing() - 90f);
        System.out.println("Rotating from "
                + MathUtils.getAngle(ship.getLocation(), cell) + " to " + angle);
        cell = MathUtils.getPointOnCircumference(ship.getLocation(),
                MathUtils.getDistance(cell, ship.getLocation()), angle);
        CombatUtils.getCombatEngine().addHitParticle(cell, ship.getVelocity(),
                5f, .1f, 1f, Color.RED);
        System.out.println("Location (rotated): " + cell);

        // Translate coordinate to be relative to the armor grid
        cell.x -= ship.getLocation().x;
        cell.y -= ship.getLocation().y;
        cell.x += (sizeX / 2f);
        cell.y += (sizeY / 2f);
        cell.scale(1f / grid.getCellSize());
        System.out.println("Location in grid (scaled): " + cell);

        // Check that point is inside armor grid
        if (cell.x < 0f || cell.y < 0f || cell.x > sizeX || cell.y > sizeY)
        {
            System.out.println("Not within armor grid: " + cell);
            return null;
        }

        // Return integer result
        cell.set((int) cell.x, (int) cell.y);
        System.out.println("In grid: " + cell);
        return cell;
    }

    /**
     * Get the armor value of a {@link ShipAPI} at a location. Equivalent
     * to {@link ArmorGridAPI#getArmorValue(int, int)}, but using
     * world-space coordinates.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc The world location we will be checking the armor value at.
     * @return The armor value at {@code loc}, or {@link DefenseUtils#NOT_IN_GRID}
     * if the point isn't within {@code ship}'s {@link ArmorGridAPI}.
     * @since 1.5
     */
    public static float getArmorValue(ShipAPI ship, Vector2f loc)
    {
        Vector2f cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return ship.getArmorGrid().getArmorValue((int) cell.x, (int) cell.y);
    }

    /**
     * Get the total damage taken by a {@link ShipAPI}'s armor at a location.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc The world location we will be checking the armor damage at.
     * @return The armor damage taken at {@code loc}, or
     * {@link DefenseUtils#NOT_IN_GRID} if the point isn't within
     * {@code ship}'s {@link ArmorGridAPI}.
     * @since 1.5
     */
    public static float getArmorDamage(ShipAPI ship, Vector2f loc)
    {
        Vector2f cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getMaxArmorInCell()
                - ship.getArmorGrid().getArmorValue((int) cell.x, (int) cell.y));
    }

    /**
     * Get the armor level of a {@link ShipAPI} at a location. Equivalent
     * to {@link ArmorGridAPI#getArmorLevel(int, int)}, but using
     * world-space coordinates.
     *
     * @param ship The {@link ShipAPI} whose {@link ArmorGridAPI} we will use.
     * @param loc The world location we will be checking the armor level at.
     * @return The armor level at {@code loc}, or {@link DefenseUtils#NOT_IN_GRID}
     * if the point isn't within {@code ship}'s {@link ArmorGridAPI}.
     * @since 1.5
     */
    public static float getArmorLevel(ShipAPI ship, Vector2f loc)
    {
        Vector2f cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getArmorFraction((int) cell.x, (int) cell.y));
    }

    /**
     * Determine what {@link DefenseType} is present at a specific location
     * on a {@link ShipAPI}.
     *
     * @param ship The {@link ShipAPI} to examine.
     * @param loc The location to check at.
     * @return The {@link DefenseType} present at {@code loc}.
     * @since 1.5
     */
    public static DefenseType getDefenseAtPoint(ShipAPI ship, Vector2f loc)
    {
        // Point is not in bounds or ship's phase cloak is active
        if (!CollisionUtils.isPointWithinBounds(loc, ship)
                || (ship.getPhaseCloak() != null && ship.getPhaseCloak().isActive()))
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