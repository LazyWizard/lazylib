package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lazywizard.lazylib.CollisionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods to make working with a ship's armor grid less of a headache.
 *
 * @author LazyWizard
 * @since 1.5
 */
public class ArmorUtils
{
    public static final float NOT_IN_GRID = -123.45f;

    public static int[] getArmorCellAtWorldCoord(ShipAPI ship, Vector2f loc)
    {
        // I'm aware that armor cells go beyond ship bounds, but
        // this check solves far more problems than it creates
        if (!CollisionUtils.isPointWithinBounds(loc, ship))
        {
            return null;
        }

        ArmorGridAPI grid = ship.getArmorGrid();
        float[][] gridData = grid.getGrid();

        // Find the offset of the center point of the armor grid
        float xOffset = grid.getCellSize() * (gridData.length / 2f),
                yOffset = grid.getCellSize() * (gridData[0].length / 2f);

        // Treat the ship as being at location 0,0 and angle 0 for easier math
        Vector2f subLoc = new Vector2f();
        Vector2f.sub(loc, ship.getLocation(), subLoc);
        subLoc = MathUtils.getPointOnCircumference(null, subLoc.length(),
                MathUtils.getFacing(subLoc) - ship.getFacing());

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

    private ArmorUtils()
    {
    }
}
