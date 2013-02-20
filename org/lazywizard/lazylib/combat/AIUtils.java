package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;

public class AIUtils
{
    public static BattleObjectiveAPI getNearestObjective(CombatEntityAPI entity)
    {
        BattleObjectiveAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (BattleObjectiveAPI tmp : CombatUtils.getCombatEngine().getObjectives())
        {
            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    entity.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    public static ShipAPI getNearestEnemy(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getOwner() == entity.getOwner() || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    entity.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    public static ShipAPI getNearestAlly(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp == entity || tmp.getOwner() != entity.getOwner() || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    entity.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    public static ShipAPI getNearestShip(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp == entity || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    entity.getLocation());

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity)
    {
        List<ShipAPI> enemies = new ArrayList();

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getOwner() != entity.getOwner() && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity, float range)
    {
        List<ShipAPI> enemies = new ArrayList();
        range *= range;

        for (ShipAPI enemy : getEnemiesOnMap(entity))
        {
            if (MathUtils.getDistanceSquared(entity, enemy) <= range)
            {
                enemies.add(enemy);
            }
        }

        return enemies;
    }

        public static List<ShipAPI> getAlliesOnMap(CombatEntityAPI entity)
    {
        List<ShipAPI> enemies = new ArrayList();

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp != entity && tmp.getOwner() == entity.getOwner() && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity, float range)
    {
        List<ShipAPI> allies = new ArrayList();
        range *= range;

        for (ShipAPI ally : getAlliesOnMap(entity))
        {
            if (MathUtils.getDistanceSquared(entity, ally) <= range)
            {
                allies.add(ally);
            }
        }

        return allies;
    }

    public static boolean canUseSystemThisFrame(ShipAPI ship)
    {
        FluxTrackerAPI flux = ship.getFluxTracker();
        if (flux.isOverloadedOrVenting())
        {
            return false;
        }

        ShipSystemAPI system = ship.getSystem();
        if (system == null || system.isOutOfAmmo()
                || system.getCooldownRemaining() > 0f
                || system.getFluxPerUse() > (flux.getMaxFlux() - flux.getCurrFlux()))
        {
            return false;
        }

        return true;
    }

    private AIUtils()
    {
    }
}
