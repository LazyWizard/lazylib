package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods that deal with a single combat entity.
 * @author LazyWizard
 */
public class AIUtils
{
    /**
     * Find the closest {@link BattleObjectiveAPI} to an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The {@link BattleObjectiveAPI} closest to {@code entity}.
     */
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

    /**
     * Find the closest enemy of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The enemy closest to {@code entity}.
     */
    public static ShipAPI getNearestEnemy(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getOwner() == entity.getOwner()
                    || tmp.isHulk() || tmp.isShuttlePod())
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

    /**
     * Find the closest ally of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The ally closest to {@code entity}.
     */
    public static ShipAPI getNearestAlly(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp == entity || tmp.getOwner() != entity.getOwner()
                    || tmp.isHulk() || tmp.isShuttlePod())
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

    /**
     * Find the closest ship near entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The ship closest to {@code entity}.
     */
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

    /**
     * Find all present enemies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return All enemies of {@code entity} on the battle map.
     */
    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<ShipAPI> enemies = new ArrayList();

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getOwner() != entity.getOwner() && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                enemies.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return enemies;
    }

    /**
     * Find all present enemies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return All enemies of {@code entity} on the battle map.
     * @see AIUtils#getEnemiesOnMap(com.fs.starfarer.api.combat.CombatEntityAPI, boolean)
     */
    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity)
    {
        return getEnemiesOnMap(entity, false);
    }

    /**
     * Finds all enemies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return A {@link List} containing all enemy ships within range.
     */
    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity,
            float range, boolean sortByDistance)
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

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return enemies;
    }

    /**
     * Finds all enemies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @return A {@link List} containing all enemy ships within range.
     * @see AIUtils#getNearbyEnemies(com.fs.starfarer.api.combat.CombatEntityAPI, float, boolean)
     */
    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity, float range)
    {
        return getNearbyEnemies(entity, range, false);
    }

    /**
     * Find all present allies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return All allies of {@code entity} on the battle map.
     */
    public static List<ShipAPI> getAlliesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<ShipAPI> allies = new ArrayList();

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp != entity && tmp.getOwner() == entity.getOwner()
                    && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                allies.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return allies;
    }

    /**
     * Find all present allies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return All allies of {@code entity} on the battle map.
     * @see AIUtils#getAlliesOnMap(com.fs.starfarer.api.combat.CombatEntityAPI, boolean)
     */
    public static List<ShipAPI> getAlliesOnMap(CombatEntityAPI entity)
    {
        return getAlliesOnMap(entity, false);
    }

    /**
     * Finds all allies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return A {@link List} containing all allied ships within range.
     */
    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity,
            float range, boolean sortByDistance)
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

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return allies;
    }

    /**
     * Finds all allies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @return A {@link List} containing all allied ships within range.
     * @see AIUtils#getNearbyAllies(com.fs.starfarer.api.combat.CombatEntityAPI, float, boolean)
     */
    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity, float range)
    {
        return getNearbyAllies(entity, range, false);
    }

    /**
     * Check if a ship's system can be used this frame.
     * This still returns true if the shipsystem is already on!
     *
     * @param ship The ship to check the system of.
     * @return {@code true} if {@code ship} can use its system, {@code false} otherwise.
     */
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
