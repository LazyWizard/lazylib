package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.ArmorGridAPI;
import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.Color;
import java.util.List;
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
    /** A constant that represents a point not in a ship's armor grid. */
    public static final float NOT_IN_GRID = -12345.9876f;

    public static void main(String[] args)
    {
        System.out.println("{");
        for (int x = 0; x <= 9; x++)
        {
            for (int y = 0; y <= 4; y++)
            {
                System.out.print((y == 0 ? "{ " : "")
                        + x + "." + y + "f"
                        + (y == 4 ? " },\n" : ", "));
            }
        }
        System.out.println("}\n\n");

        System.out.println(getArmorCellAtWorldCoord(
                new TestShip(new Vector2f(5f, 10f), 50f),
                new Vector2f(0f, 3f)));
    }

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
        // FIXME: Rotation is incorrect
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

    public static float getArmorValue(ShipAPI ship, Vector2f loc)
    {
        Vector2f cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return ship.getArmorGrid().getArmorValue((int) cell.x, (int) cell.y);
    }

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

    public static float getArmorLevel(ShipAPI ship, Vector2f loc)
    {
        Vector2f cell = getArmorCellAtWorldCoord(ship, loc);

        if (cell == null)
        {
            return NOT_IN_GRID;
        }

        return (ship.getArmorGrid().getArmorFraction((int) cell.x, (int) cell.y));
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

    private static class TestShip implements ShipAPI
    {
        private float facing;
        private Vector2f location;
        private ArmorGridAPI grid = new TestArmorAPI();

        public TestShip(Vector2f location, float facing)
        {
            this.location = location;
            this.facing = facing;
        }

        @Override
        public ArmorGridAPI getArmorGrid()
        {
            return grid;
        }

        @Override
        public Vector2f getLocation()
        {
            return location;
        }

        @Override
        public float getFacing()
        {
            return facing;
        }

        @Override
        public BoundsAPI getExactBounds()
        {
            return null;
        }

        @Override
        public float getCollisionRadius()
        {
            return 30f;
        }

        //<editor-fold defaultstate="collapsed" desc="Unimplemented">
        @Override
        public String getFleetMemberId()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Vector2f getMouseTarget()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isShuttlePod()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isDrone()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isFighter()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isFrigate()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isDestroyer()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isCruiser()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isCapital()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public HullSize getHullSize()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipAPI getShipTarget()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getOriginalOwner()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public MutableShipStatsAPI getMutableStats()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isHulk()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<WeaponAPI> getAllWeapons()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipSystemAPI getPhaseCloak()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipSystemAPI getSystem()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setShield(ShieldAPI.ShieldType type, float shieldUpkeep, float shieldEfficiency, float arc)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipHullSpecAPI getHullSpec()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipVariantAPI getVariant()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void useSystem()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public FluxTrackerAPI getFluxTracker()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<ShipAPI> getWingMembers()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipAPI getWingLeader()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean isWingLeader()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<ShipAPI> getDeployedDrones()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShipAPI getDroneSource()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getWingToken()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Vector2f getVelocity()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setFacing(float facing)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getAngularVelocity()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setAngularVelocity(float angVel)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getOwner()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setOwner(int owner)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CollisionClass getCollisionClass()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setCollisionClass(CollisionClass collisionClass)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getMass()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setMass(float mass)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ShieldAPI getShield()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getHullLevel()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getHitpoints()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float getMaxHitpoints()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        //</editor-fold>

        public class TestArmorAPI implements ArmorGridAPI
        {
            float[][] armorGrid =
            {
                {
                    0.0f, 0.1f, 0.2f, 0.3f, 0.4f
                },
                {
                    1.0f, 1.1f, 1.2f, 1.3f, 1.4f
                },
                {
                    2.0f, 2.1f, 2.2f, 2.3f, 2.4f
                },
                {
                    3.0f, 3.1f, 3.2f, 3.3f, 3.4f
                },
                {
                    4.0f, 4.1f, 4.2f, 4.3f, 4.4f
                },
                {
                    5.0f, 5.1f, 5.2f, 5.3f, 5.4f
                },
                {
                    6.0f, 6.1f, 6.2f, 6.3f, 6.4f
                },
                {
                    7.0f, 7.1f, 7.2f, 7.3f, 7.4f
                },
                {
                    8.0f, 8.1f, 8.2f, 8.3f, 8.4f
                },
                {
                    9.0f, 9.1f, 9.2f, 9.3f, 9.4f
                },
            };

            @Override
            public float getArmorRating()
            {
                return 400f;
            }

            @Override
            public float getMaxArmorInCell()
            {
                return 400f;
            }

            @Override
            public float getArmorFraction(int cellX, int cellY)
            {
                return armorGrid[cellX][cellY] / getMaxArmorInCell();
            }

            @Override
            public float getArmorValue(int cellX, int cellY)
            {
                if (cellX < 0 || cellY >= armorGrid.length
                        || cellY < 0 || cellY >= armorGrid[0].length)
                {
                    return DefenseUtils.NOT_IN_GRID;
                }

                return armorGrid[cellX][cellY];
            }

            @Override
            public float[][] getGrid()
            {
                return armorGrid;
            }

            @Override
            public float getCellSize()
            {
                return 2f;
            }
        }
    }
}