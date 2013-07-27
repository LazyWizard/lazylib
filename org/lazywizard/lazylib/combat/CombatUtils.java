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
 *
 * @author LazyWizard
 * @since 1.0
 */
public class CombatUtils implements EveryFrameCombatPlugin
{
    private static WeakReference<CombatEngineAPI> engine;
    private static float timeSinceLastFrame = 0f, combatTime = 0f,
            combatTimeIncludingPaused = 0f;

    /**
     * Returns the currently used {@link CombatEngineAPI}.
     *
     * @return The {@link CombatEngineAPI} used by the current battle.
     * @since 1.0
     */
    public static CombatEngineAPI getCombatEngine()
    {
        return engine.get();
    }

    /**
     * Returns the length of the current battle, including time spent paused.
     *
     * @return The total elapsed time (including time spent paused) for this
     * combat encounter, in seconds.
     * @since 1.2
     */
    public static float getElapsedCombatTimeIncludingPaused()
    {
        return combatTimeIncludingPaused;
    }

    /**
     * Returns the length of the current battle.
     *
     * @return The total elapsed time for this combat encounter, in seconds.
     * @since 1.0
     */
    public static float getElapsedCombatTime()
    {
        return combatTime;
    }

    /**
     * Returns the time since the last frame.
     *
     * @return The time since the last frame, in seconds.
     * @since 1.4
     */
    public static float getTimeSinceLastFrame()
    {
        return timeSinceLastFrame;
    }

    /**
     * Returns all projectiles in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of {@code location}.
     * @since 1.1
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList();
        range *= range;

        for (DamagingProjectileAPI tmp : getCombatEngine().getProjectiles())
        {
            if (MathUtils.getDistanceSquared(tmp.getLocation(), location) <= range)
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<MissileAPI> missiles = new ArrayList();
        range *= range;

        for (MissileAPI tmp : getCombatEngine().getMissiles())
        {
            if (MathUtils.getDistanceSquared(tmp.getLocation(), location) <= range)
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> ships = new ArrayList();

        for (ShipAPI tmp : getCombatEngine().getShips())
        {
            if (tmp.isShuttlePod())
            {
                continue;
            }

            if (MathUtils.getDistance(tmp, location) <= range)
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<CombatEntityAPI> asteroids = new ArrayList();

        for (CombatEntityAPI tmp : getCombatEngine().getAsteroids())
        {
            if (MathUtils.getDistance(tmp, location) <= range)
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
     * @since 1.0
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
     * @since 1.1
     */
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList();
        range *= range;

        for (BattleObjectiveAPI tmp : getCombatEngine().getObjectives())
        {
            if (MathUtils.getDistanceSquared(tmp.getLocation(), location) <= range)
            {
                objectives.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(objectives,
                    new CollectionUtils.SortObjectivesByDistance(location));
        }

        return objectives;
    }

    /**
     * Returns all objectives in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @return A {@link List} of {@link BattleObjectiveAPI}s within range of {@code location}.
     * @see CombatUtils#getObjectivesWithinRange(org.lwjgl.util.vector.Vector2f, float, boolean)
     * @since 1.0
     */
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range)
    {
        return getObjectivesWithinRange(location, range, false);
    }

    /**
     * Returns all entities in range of a given location.
     *
     * @param location The location to search around.
     * @param range How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from {@code location}.
     * @return A {@link List} of {@link CombatEntityAPI}s within range of {@code location}.
     * @since 1.1
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<CombatEntityAPI> entities = new ArrayList();
        range *= range;

        for (CombatEntityAPI tmp : getCombatEngine().getShips())
        {
            if (MathUtils.getDistanceSquared(tmp, location) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : getCombatEngine().getProjectiles())
        {
            if (MathUtils.getDistanceSquared(tmp, location) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : getCombatEngine().getAsteroids())
        {
            if (MathUtils.getDistanceSquared(tmp, location) <= range)
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
     * @since 1.0
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location, float range)
    {
        return getEntitiesWithinRange(location, range, false);
    }

    /**
     * Apply force to an object. Remember Newton's Second Law.
     *
     * Force is multiplied by 100 to avoid requiring ridiculous force amounts.
     *
     * @param entity The {@link CombatEntityAPI} to apply the force to.
     * @param direction The directional vector of the force (this will automatically be normalized).
     * @param force How much force to apply.
     * @since 1.2
     */
    public static void applyForce(CombatEntityAPI entity, Vector2f direction, float force)
    {
        // Filter out forces without a direction
        if (direction.lengthSquared() == 0)
        {
            return;
        }

        // Force is far too weak otherwise
        force *= 100f;

        // Avoid divide-by-zero errors...
        float mass = Math.max(1f, entity.getMass());
        // Calculate the velocity change and its resulting vector
        float velChange = Math.min(1250f, force / mass);
        Vector2f dir = new Vector2f();
        direction.normalise(dir);
        dir.scale(velChange);
        // Apply our velocity change
        Vector2f.add(dir, entity.getVelocity(), entity.getVelocity());
    }

    /**
     * Apply force to an object. Remember Newton's Second Law.
     *
     * @param entity The {@link CombatEntityAPI} to apply the force to.
     * @param direction The angle the force will be applied towards.
     * @param force How much force to apply.
     * @since 1.2
     */
    public static void applyForce(CombatEntityAPI entity, float direction, float force)
    {
        applyForce(entity, MathUtils.getPointOnCircumference(new Vector2f(0, 0),
                1f, direction), force);
    }

    /**
     * Automatically called by the game. Don't call this manually.
     */
    @Override
    public void advance(float amount, List<InputEventAPI> events)
    {
        timeSinceLastFrame = amount;
        combatTimeIncludingPaused += amount;

        if (!getCombatEngine().isPaused())
        {
            combatTime += amount;
        }
    }

    /**
     * Automatically called by the game. Don't call this manually.
     */
    @Override
    public void init(CombatEngineAPI engine)
    {
        CombatUtils.engine = new WeakReference(engine);
        CombatUtils.timeSinceLastFrame = 0f;
        CombatUtils.combatTime = 0f;
        CombatUtils.combatTimeIncludingPaused = 0f;
    }

    protected CombatUtils()
    {
    }
}
