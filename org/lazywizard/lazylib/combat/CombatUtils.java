package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class CombatUtils implements EveryFrameCombatPlugin
{
    private static CombatEngineAPI engine;
    private static float combatTime = 0f;

    public static enum DefenseType
    {
        HULL,
        ARMOR,
        SHIELD,
        PHASE,
        MISS
    }

    public static CombatEngineAPI getCombatEngine()
    {
        return engine;
    }

    public static float getElapsedCombatTime()
    {
        return combatTime;
    }

    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location, float range)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList<DamagingProjectileAPI>();

        for (DamagingProjectileAPI tmp : engine.getProjectiles())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                projectiles.add(tmp);
            }
        }

        return projectiles;
    }

    public static List<MissileAPI> getMissilesWithinRange(Vector2f location, float range)
    {
        List<MissileAPI> missiles = new ArrayList<MissileAPI>();

        for (MissileAPI tmp : engine.getMissiles())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                missiles.add(tmp);
            }
        }

        return missiles;
    }

    public static List<ShipAPI> getShipsWithinRange(Vector2f location, float range)
    {
        List<ShipAPI> ships = new ArrayList<ShipAPI>();

        for (ShipAPI tmp : engine.getShips())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                ships.add(tmp);
            }
        }

        return ships;
    }

    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location, float range)
    {
        List<CombatEntityAPI> asteroids = new ArrayList<CombatEntityAPI>();

        for (CombatEntityAPI tmp : engine.getAsteroids())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                asteroids.add(tmp);
            }
        }

        return asteroids;
    }

    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location, float range)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList<BattleObjectiveAPI>();

        for (BattleObjectiveAPI tmp : engine.getObjectives())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                objectives.add(tmp);
            }
        }

        return objectives;
    }

    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location, float range)
    {
        List<CombatEntityAPI> entities = new ArrayList<CombatEntityAPI>();

        for (CombatEntityAPI tmp : engine.getShips())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : engine.getProjectiles())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : engine.getAsteroids())
        {
            if (MathUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        return entities;
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events)
    {
        combatTime += amount;
    }

    @Override
    public void init(CombatEngineAPI engine)
    {
        CombatUtils.engine = engine;
        CombatUtils.combatTime = 0f;
    }
}
