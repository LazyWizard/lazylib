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
import java.util.Comparator;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.CollectionUtils.SortEntitiesByDistance;
import org.lazywizard.lazylib.MathUtils;

/**
 * Contains methods that deal with a single combat entity and how it views the
 * battle map.
 *
 * @author LazyWizard
 * @since 1.0
 */
// TODO: Add onDeprecatedMethodUsage() calls to deprecated methods
public class AIUtils
{
    /**
     * Find the closest {@link BattleObjectiveAPI} to an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return The {@link BattleObjectiveAPI} closest to {@code entity}.
     * <p>
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
     * <p>
     * @return The enemy closest to {@code entity}.
     * <p>
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

            if (!CombatUtils.isVisibleToSide(tmp, entity.getOwner()))
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
     * <p>
     * @return The ally closest to {@code entity}.
     * <p>
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
     * <p>
     * @return The ship closest to {@code entity}.
     * <p>
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

            if (!CombatUtils.isVisibleToSide(tmp, entity.getOwner()))
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
     * <p>
     * @return The {@link MissileAPI} closest to {@code entity}.
     * <p>
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

            if (!CombatUtils.isVisibleToSide(tmp, entity.getOwner()))
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
     * <p>
     * @return All enemies of {@code entity} on the battle map.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity)
    {
        List<ShipAPI> enemies = new ArrayList<ShipAPI>();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp.getOwner() != entity.getOwner()
                    && !tmp.isHulk() && !tmp.isShuttlePod()
                    && CombatUtils.isVisibleToSide(tmp, entity.getOwner()))
            {
                enemies.add(tmp);
            }
        }

        return enemies;
    }

    /**
     * Finds all enemies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range  How far around {@code entity} to search.
     * <p>
     * @return A {@link List} containing all enemy ships within range.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity, float range)
    {
        List<ShipAPI> enemies = new ArrayList<ShipAPI>();

        for (ShipAPI enemy : getEnemiesOnMap(entity))
        {
            if (CombatUtils.isVisibleToSide(enemy, entity.getOwner())
                    && MathUtils.getDistance(entity, enemy) <= range)
            {
                enemies.add(enemy);
            }
        }

        return enemies;
    }

    /**
     * Find all present allies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return All allies of {@code entity} on the battle map.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getAlliesOnMap(CombatEntityAPI entity)
    {
        List<ShipAPI> allies = new ArrayList<ShipAPI>();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp != entity && tmp.getOwner() == entity.getOwner()
                    && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                allies.add(tmp);
            }
        }

        return allies;
    }

    /**
     * Finds all allies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range  How far around {@code entity} to search.
     * <p>
     * @return A {@link List} containing all allied ships within range.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity, float range)
    {
        List<ShipAPI> allies = new ArrayList<ShipAPI>();
        for (ShipAPI ally : getAlliesOnMap(entity))
        {
            if (MathUtils.getDistance(entity, ally) <= range)
            {
                allies.add(ally);
            }
        }

        return allies;
    }

    /**
     * Find the closest enemy missile near an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return The enemy {@link MissileAPI} closest to {@code entity}.
     * <p>
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

            if (!CombatUtils.isVisibleToSide(tmp, entity.getOwner()))
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
     * Find all present enemy missiles of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return All enemy {@link MissileAPI}s of {@code entity} on the battle
     *         map.
     * <p>
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesOnMap(CombatEntityAPI entity)
    {
        List<MissileAPI> missiles = new ArrayList<MissileAPI>();

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (tmp.getOwner() != entity.getOwner()
                    && CombatUtils.isVisibleToSide(tmp, entity.getOwner()))
            {
                missiles.add(tmp);
            }
        }

        return missiles;
    }

    /**
     * Finds all enemy missiles within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range  How far around {@code entity} to search.
     * <p>
     * @return A {@link List} containing all enemy missiles within range.
     * <p>
     * @since 1.4
     */
    public static List<MissileAPI> getNearbyEnemyMissiles(CombatEntityAPI entity, float range)
    {
        List<MissileAPI> missiles = new ArrayList<MissileAPI>();

        for (MissileAPI enemy : getEnemyMissilesOnMap(entity))
        {
            if (CombatUtils.isVisibleToSide(enemy, entity.getOwner())
                    && MathUtils.getDistance(entity, enemy) <= range)
            {
                missiles.add(enemy);
            }
        }

        return missiles;
    }

    /**
     * Check if a ship's system can be used/toggled this frame.
     * This still returns true if the shipsystem is already on!
     *
     * @param ship The ship to check the system of.
     * <p>
     * @return {@code true} if {@code ship} can use its system, {@code false}
     *         otherwise.
     * <p>
     * @since 1.0
     */
    public static boolean canUseSystemThisFrame(ShipAPI ship)
    {
        FluxTrackerAPI flux = ship.getFluxTracker();
        ShipSystemAPI system = ship.getSystem();

        // No system, overloading/venting, out of ammo
        return !(system == null || flux.isOverloadedOrVenting() || system.isOutOfAmmo()
                // In use but can't be toggled off right away
                || (system.isOn() && system.getCooldownRemaining() > 0f)
                // In chargedown, in cooldown
                || (system.isActive() && !system.isOn()) || system.getCooldownRemaining() > 0f
                // Not enough flux
                || system.getFluxPerUse() > (flux.getMaxFlux() - flux.getCurrFlux()));
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<ShipAPI> enemies = getEnemiesOnMap(entity);

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return enemies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> enemies = getNearbyEnemies(entity, range);

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return enemies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<ShipAPI> getAlliesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<ShipAPI> allies = getAlliesOnMap(entity);

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return allies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<ShipAPI> getNearbyAllies(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> allies = getNearbyAllies(entity, range);

        if (sortByDistance)
        {
            Collections.sort(allies,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return allies;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.4
     */
    @Deprecated
    public static List<MissileAPI> getEnemyMissilesOnMap(CombatEntityAPI entity,
            boolean sortByDistance)
    {
        List<MissileAPI> missiles = getEnemyMissilesOnMap(entity);

        if (sortByDistance)
        {
            Collections.sort(missiles,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return missiles;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.4
     */
    @Deprecated
    public static List<MissileAPI> getNearbyEnemyMissiles(CombatEntityAPI entity,
            float range, boolean sortByDistance)
    {
        List<MissileAPI> missiles = getNearbyEnemyMissiles(entity, range);

        if (sortByDistance)
        {
            Collections.sort(missiles,
                    new CollectionUtils.SortEntitiesByDistance(entity.getLocation()));
        }

        return missiles;

    }

    private AIUtils()
    {
    }
}
