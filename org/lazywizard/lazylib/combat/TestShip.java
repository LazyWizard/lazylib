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
import java.util.List;
import org.lazywizard.lazylib.combat.entities.SimpleEntity;
import org.lwjgl.util.vector.Vector2f;

public class TestShip implements ShipAPI
{
    private ArmorGridAPI grid = new TestArmorAPI();

    @Override
    public ArmorGridAPI getArmorGrid()
    {
        return new TestArmorAPI();
    }

    @Override
    public Vector2f getLocation()
    {
        return new Vector2f(0f, 0f);
    }

    @Override
    public float getFacing()
    {
        return 0f;
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
        System.out.println("}");
    }
}
