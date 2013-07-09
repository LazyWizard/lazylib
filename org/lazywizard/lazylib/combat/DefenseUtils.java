package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods to make working with a ship's armor grid and defenses easier.
 *
 * @author LazyWizard
 * @since 1.5
 */
// TODO: Add JavaDoc to this class
public class DefenseUtils
{
    public static final float NOT_IN_GRID = -12345.9876f;

    // FIXME: there's an off-by-one error somewhere in here
    // TODO: probably need to rewrite most of this
    public static int[] getArmorCellAtWorldCoord(ShipAPI ship, Vector2f loc)
    {
        // TODO: replace with check if point is within gridData
        if (!CollisionUtils.isPointWithinBounds(loc, ship))
        {
            return null;
        }

        ArmorGridAPI grid = ship.getArmorGrid();
        float[][] gridData = grid.getGrid();

        // Find the offset of the center point of the armor grid
        float xOffset = grid.getCellSize() * (gridData.length / 2f),
                yOffset = grid.getCellSize() * (gridData[0].length / 2f);

        // Translate the ship's location to 0,0 and facing to -90 (upwards)
        Vector2f subLoc = new Vector2f();
        Vector2f.sub(loc, ship.getLocation(), subLoc);
        subLoc = MathUtils.getPointOnCircumference(null, subLoc.length(),
                MathUtils.getFacing(subLoc) - ship.getFacing() - 90f);

        // Get the cells at this location
        int cellX = (int) (subLoc.x / grid.getCellSize() + xOffset);
        int cellY = (int) (subLoc.y / grid.getCellSize() + yOffset);

        return new int[]
        {
            cellX, cellY
        };
    }

    public static float getArmorValue(ShipAPI ship, Vector2f loc)
    {
        int[] cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return ship.getArmorGrid().getArmorValue(cell[0], cell[1]);
    }

    public static float getArmorDamage(ShipAPI ship, Vector2f loc)
    {
        int[] cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getMaxArmorInCell()
                - ship.getArmorGrid().getArmorValue(cell[0], cell[1]));
    }

    public static float getArmorLevel(ShipAPI ship, Vector2f loc)
    {
        int[] cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getArmorFraction(cell[0], cell[1]));
    }

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
