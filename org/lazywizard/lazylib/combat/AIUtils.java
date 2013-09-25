package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods that deal with a single combat entity.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class AIUtils
{
    // Not public as it's so simple it's mostly a convenience tweak
    private static boolean isVisibleToSide(CombatEntityAPI entity, int side)
    {
        if (entity.getOwner() == side)
        {
            return true;
        }

        return Global.getCombatEngine().getFogOfWar(side).isVisible(entity.getLocation());
    }

    /**
     * Find the closest {@link BattleObjectiveAPI} to an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The {@link BattleObjectiveAPI} closest to {@code entity}.
     * @since 1.0
     */
    public static BattleObjectiveAPI getNearestObjective(CombatEntityAPI entity)
    {
        BattleObjectiveAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (BattleObjectiveAPI tmp : Global.getCombatEngine().getObjectives())
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
     * @since 1.0
     */
    public static ShipAPI getNearestEnemy(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp.getOwner() == entity.getOwner()
                    || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            if (!isVisibleToSide(tmp, entity.getOwner()))
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
     * @since 1.0
     */
    public static ShipAPI getNearestAlly(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
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
     * @since 1.0
     */
    public static ShipAPI getNearestShip(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp == entity || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            if (!isVisibleToSide(tmp, entity.getOwner()))
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
     * Find the closest missile near entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The {@link MissileAPI} closest to {@code entity}.
     * @since 1.4
     */
    public static MissileAPI getNearestMissile(CombatEntityAPI entity)
    {
        MissileAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (tmp == entity)
            {
                continue;
            }

            if (!isVisibleToSide(tmp, entity.getOwner()))
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
     * @since 1.1
     */
    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<ShipAPI> enemies = new ArrayList();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp.getOwner() != entity.getOwner() && !tmp.isHulk()
                    && !tmp.isShuttlePod() && isVisibleToSide(tmp, entity.getOwner()))
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> enemies = new ArrayList();

        for (ShipAPI enemy : getEnemiesOnMap(entity))
        {
            if (isVisibleToSide(enemy, entity.getOwner())
                    && MathUtils.getDistance(entity, enemy) <= range)
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<ShipAPI> getAlliesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<ShipAPI> allies = new ArrayList();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> allies = new ArrayList();
        for (ShipAPI ally : getAlliesOnMap(entity))
        {
            if (MathUtils.getDistance(entity, ally) <= range)
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
     * @since 1.0
     */
    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity, float range)
    {
        return getNearbyAllies(entity, range, false);
    }

    /**
     * Find the closest enemy missile near an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return The enemy {@link MissileAPI} closest to {@code entity}.
     * @since 1.4
     */
    public static MissileAPI getNearestEnemyMissile(CombatEntityAPI entity)
    {
        MissileAPI closest = null;
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (tmp.getOwner() == entity.getOwner())
            {
                continue;
            }

            if (!isVisibleToSide(tmp, entity.getOwner()))
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
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<MissileAPI> enemies = new ArrayList();

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (tmp.getOwner() != entity.getOwner()
                    && isVisibleToSide(tmp, entity.getOwner()))
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
     * Find all present enemy missiles of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * @return All enemy {@link MissileAPI}s of {@code entity} on the battle map.
     * @see AIUtils#getEnemyMissilesOnMap(com.fs.starfarer.api.combat.CombatEntityAPI, boolean)
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesOnMap(CombatEntityAPI entity)
    {
        return getEnemyMissilesOnMap(entity, false);
    }

    /**
     * Finds all enemy missiles within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code entity}.
     * @return A {@link List} containing all enemy missiles within range.
     * @since 1.4
     */
    public static List<MissileAPI> getNearbyEnemyMissiles(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<MissileAPI> enemies = new ArrayList();

        for (MissileAPI enemy : getEnemyMissilesOnMap(entity))
        {
            if (isVisibleToSide(enemy, entity.getOwner())
                    && MathUtils.getDistance(entity, enemy) <= range)
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
     * Finds all enemy missiles within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range How far around {@code entity} to search.
     * @return A {@link List} containing all enemy missiles within range.
     * @see AIUtils#getNearbyEnemyMissiles(com.fs.starfarer.api.combat.CombatEntityAPI, float, boolean)
     * @since 1.4
     */
    public static List<MissileAPI> getNearbyEnemyMissiles(CombatEntityAPI entity, float range)
    {
        return getNearbyEnemyMissiles(entity, range, false);
    }

    /**
     * Check if a ship's system can be toggled this frame.
     * This still returns true if the shipsystem is already on!
     *
     * @param ship The ship to check the system of.
     * @return {@code true} if {@code ship} can use its system, {@code false} otherwise.
     * @since 1.0
     */
    public static boolean canUseSystemThisFrame(ShipAPI ship)
    {
        FluxTrackerAPI flux = ship.getFluxTracker();
        ShipSystemAPI system = ship.getSystem();

        // No system, overloading/venting, out of ammo
        if (system == null || flux.isOverloadedOrVenting() || system.isOutOfAmmo()
                // In use
                || (system.isOn() && system.getCooldownRemaining() > 0f)
                // In chargedown, in cooldown
                || (system.isActive() && !system.isOn())|| system.getCooldownRemaining() > 0f
                // Not enough flux
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
