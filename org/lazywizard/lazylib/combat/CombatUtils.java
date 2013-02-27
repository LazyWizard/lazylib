package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with the battle map in general.
 * @author LazyWizard
 */
public class CombatUtils implements EveryFrameCombatPlugin
{
    private static WeakReference<CombatEngineAPI> engine;
    private static float combatTime = 0f;

    /**
     * Returns the currently used {@link CombatEngineAPI}.
     *
     * @return The {@link CombatEngineAPI} used by the current battle.
     */
    public static CombatEngineAPI getCombatEngine()
    {
        return engine.get();
    }

    /**
     * Returns the length of the current battle.
     *
     * @return The total elapsed time for this combat encounter, in seconds.
     */
    public static float getElapsedCombatTime()
    {
        return combatTime;
    }

    /**
     * Returns all projectiles in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of {@code location}.
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList();
        range *= range;

        for (DamagingProjectileAPI tmp : getCombatEngine().getProjectiles())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                projectiles.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(projectiles, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return projectiles;
    }

    /**
     * Returns all projectiles in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of {@code location}.
     * @see CombatUtils#getProjectilesWithinRange(org.lwjgl.util.vector.Vector2f, float, boolean)
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location, float range)
    {
        return getProjectilesWithinRange(location, range, false);
    }

    /**
     * Returns all missiles in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link MissileAPI}s within range of {@code location}.
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<MissileAPI> missiles = new ArrayList();
        range *= range;

        for (MissileAPI tmp : getCombatEngine().getMissiles())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                missiles.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(missiles, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return missiles;
    }

    /**
     * Returns all missiles in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @return A {@link List} of {@link MissileAPI}s within range of {@code location}.
     * @see CombatUtils#getMissilesWithinRange(org.lwjgl.util.vector.Vector2f, float, boolean)
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location, float range)
    {
        return getMissilesWithinRange(location, range, false);
    }

    /**
     * Returns all ships in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link ShipAPI}s within range of {@code location}.
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> ships = new ArrayList();
        range *= range;

        for (ShipAPI tmp : getCombatEngine().getShips())
        {
            if (tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                ships.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(ships, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return ships;
    }

    /**
     * Returns all ships in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @return A {@link List} of {@link ShipAPI}s within range of {@code location}.
     * @see CombatUtils#getShipsWithinRange(org.lwjgl.util.vector.Vector2f, float, boolean)
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location, float range)
    {
        return getShipsWithinRange(location, range, false);
    }

    /**
     * Returns all asteroids in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of asteroids within range of {@code location}.
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<CombatEntityAPI> asteroids = new ArrayList();
        range *= range;

        for (CombatEntityAPI tmp : getCombatEngine().getAsteroids())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                asteroids.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(asteroids, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return asteroids;
    }

    /**
     * Returns all asteroids in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @return A {@link List} of asteroids within range of {@code location}.
     * @see CombatUtils#getAsteroidsWithinRange(org.lwjgl.util.vector.Vector2f, float, boolean)
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location, float range)
    {
        return getAsteroidsWithinRange(location, range, false);
    }

    /**
     * Returns all objectives in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link BattleObjectiveAPI}s within range of {@code location}.
     */
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList();
        range *= range;

        for (BattleObjectiveAPI tmp : getCombatEngine().getObjectives())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                objectives.add(tmp);
            }
        }

        return objectives;
    }

    /**
     * Returns all entities in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link CombatEntityAPI}s within range of {@code location}.
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<CombatEntityAPI> entities = new ArrayList();
        range *= range;

        for (CombatEntityAPI tmp : getCombatEngine().getShips())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : getCombatEngine().getProjectiles())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : getCombatEngine().getAsteroids())
        {
            if (MathUtils.getDistanceSquared(location, tmp.getLocation()) <= range)
            {
                entities.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(entities, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return entities;
    }

    /**
     * Returns all entities in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @return A {@link List} of {@link CombatEntityAPI}s within range of {@code location}.
     * @see CombatUtils#getEntitiesWithinRange(org.lwjgl.util.vector.Vector2f, float, boolean)
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location, float range)
    {
        return getEntitiesWithinRange(location, range, false);
    }

    /**
     * Automatically called by the game. Don't call this manually.
     */
    @Override
    public void advance(float amount, List<InputEventAPI> events)
    {
        combatTime += amount;
    }

    /**
     * Automatically called by the game. Don't call this manually.
     */
    @Override
    public void init(CombatEngineAPI engine)
    {
        CombatUtils.engine = new WeakReference(engine);
        CombatUtils.combatTime = 0f;
    }

    protected CombatUtils()
    {
    }
}
