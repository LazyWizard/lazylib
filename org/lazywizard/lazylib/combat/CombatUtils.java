package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.BaseUtils;
import org.lazywizard.lazylib.Line;
import org.lwjgl.util.vector.Vector2f;

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

    public static Line convertSegmentToLine(SegmentAPI segment)
    {
        return new Line(segment.getP1(), segment.getP2());
    }

    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location, float range, CombatEngineAPI engine)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList<DamagingProjectileAPI>();

        for (DamagingProjectileAPI tmp : engine.getProjectiles())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                projectiles.add(tmp);
            }
        }

        return projectiles;
    }

    public static List<ShipAPI> getShipsWithinRange(Vector2f location, float range, CombatEngineAPI engine)
    {
        List<ShipAPI> ships = new ArrayList<ShipAPI>();

        for (ShipAPI tmp : engine.getShips())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                ships.add(tmp);
            }
        }

        return ships;
    }

    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location, float range, CombatEngineAPI engine)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList<BattleObjectiveAPI>();

        for (BattleObjectiveAPI tmp : engine.getObjectives())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                objectives.add(tmp);
            }
        }

        return objectives;
    }

    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location, float range, CombatEngineAPI engine)
    {
        List<CombatEntityAPI> entities = new ArrayList<CombatEntityAPI>();

        for (CombatEntityAPI tmp : engine.getShips())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : engine.getProjectiles())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : engine.getMissiles())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : engine.getAsteroids())
        {
            if (BaseUtils.getDistance(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        return entities;
    }
}
