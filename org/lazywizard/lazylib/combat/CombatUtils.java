package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.FogOfWarAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Level;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.LazyLib;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with the battle map in general.
 *
 * @author LazyWizard
 * @since 1.0
 */
// TODO: Deprecate all methods with a sortByDistance parameter
public class CombatUtils
{
    /**
     * Find a {@link ShipAPI}'s corresponding {@link FleetMemberAPI}.
     *
     * @param ship The {@link ShipAPI} whose corresponding
     *             {@link FleetMemberAPI} we are trying to find.
     * <p>
     * @return The {@link FleetMemberAPI} that represents this {@link ShipAPI}
     *         in the campaign, or {@code null} if no match is found.
     * <p>
     * @since 1.5
     */
    public static FleetMemberAPI getFleetMember(ShipAPI ship)
    {
        DeployedFleetMemberAPI dfm = Global.getCombatEngine()
                .getFleetManager(ship.getOriginalOwner())
                .getDeployedFleetMember(ship);

        // Compatibility fix for main menu battles
        if (dfm == null)
        {
            return null;
        }

        return dfm.getMember();
    }

    /**
     * Checks if a {@link CombatEntityAPI} is visible to a side of battle.
     * Note1: Allied and neutral entities are always visible.
     * Note2: All {@link AIUtils} methods already filter by visibility, so use
     * of this method is not necessary on their results.
     *
     * @param entity The {@link CombatEntityAPI} to check visibility of.
     * @param side   The side whose fog of war will be tested.
     * <p>
     * @return {@code true} if {@code entity} is visible to {@code side},
     *         {@code false} otherwise.
     * <p>
     * @since 1.7
     */
    public static boolean isVisibleToSide(CombatEntityAPI entity, int side)
    {
        // This is a VERY fast check for neutral/allied status
        // Player+enemy (0+1) is the only combination that will return 1
        // Always visible: neutrals (side 100) and allies (0+0 or 1+1)
        // Warning: this optimization will cause bugs if SS ever
        // adds support for more than two sides in a battle!
        if (side + entity.getOwner() != 1)
        {
            Global.getLogger(AIUtils.class).log(Level.DEBUG,
                    "Always visible: " + entity.toString());
            return true;
        }

        // There have been reports of null pointer exceptions in this method
        // If this block is ever tripped, it's a vanilla bug
        FogOfWarAPI fog = Global.getCombatEngine().getFogOfWar(side);
        if (fog == null)
        {
            Global.getLogger(AIUtils.class).log(Level.ERROR,
                    "Fog of war not found for side " + side
                    + ", defaulting to visible!");
            return true;
        }

        return fog.isVisible(entity.getLocation());
    }

    /**
     * Returns all projectiles in range of a given location.
     *
     * @param location       The location to search around.
     * @param range          How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code location}.
     * <p>
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.1
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList<DamagingProjectileAPI>();
        range *= range;

        for (DamagingProjectileAPI tmp : Global.getCombatEngine().getProjectiles())
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
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of
     *         {@code location}.
     * <p>
     * @see
     * CombatUtils#getProjectilesWithinRange(org.lwjgl.util.vector.Vector2f,
     * float, boolean)
     * @since 1.0
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location, float range)
    {
        return getProjectilesWithinRange(location, range, false);
    }

    /**
     * Returns all missiles in range of a given location.
     *
     * @param location       The location to search around.
     * @param range          How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code location}.
     * <p>
     * @return A {@link List} of {@link MissileAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.1
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<MissileAPI> missiles = new ArrayList<MissileAPI>();
        range *= range;

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
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
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link MissileAPI}s within range of
     *         {@code location}.
     * <p>
     * @see CombatUtils#getMissilesWithinRange(org.lwjgl.util.vector.Vector2f,
     * float, boolean)
     * @since 1.0
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location, float range)
    {
        return getMissilesWithinRange(location, range, false);
    }

    /**
     * Returns all ships in range of a given location.
     *
     * @param location       The location to search around.
     * @param range          How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code location}.
     * <p>
     * @return A {@link List} of {@link ShipAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.1
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<ShipAPI> ships = new ArrayList<ShipAPI>();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
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
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link ShipAPI}s within range of
     *         {@code location}.
     * <p>
     * @see CombatUtils#getShipsWithinRange(org.lwjgl.util.vector.Vector2f,
     * float, boolean)
     * @since 1.0
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location, float range)
    {
        return getShipsWithinRange(location, range, false);
    }

    /**
     * Returns all asteroids in range of a given location.
     *
     * @param location       The location to search around.
     * @param range          How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code location}.
     * <p>
     * @return A {@link List} of asteroids within range of {@code location}.
     * <p>
     * @since 1.1
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<CombatEntityAPI> asteroids = new ArrayList<CombatEntityAPI>();

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
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
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of asteroids within range of {@code location}.
     * <p>
     * @see CombatUtils#getAsteroidsWithinRange(org.lwjgl.util.vector.Vector2f,
     * float, boolean)
     * @since 1.0
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location, float range)
    {
        return getAsteroidsWithinRange(location, range, false);
    }

    /**
     * Returns all objectives in range of a given location.
     *
     * @param location       The location to search around.
     * @param range          How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code location}.
     * <p>
     * @return A {@link List} of {@link BattleObjectiveAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.1
     */
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList<BattleObjectiveAPI>();
        range *= range;

        for (BattleObjectiveAPI tmp : Global.getCombatEngine().getObjectives())
        {
            if (MathUtils.getDistanceSquared(tmp.getLocation(), location) <= range)
            {
                objectives.add(tmp);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(objectives,
                    new CollectionUtils.SortEntitiesByDistance(location, false));
        }

        return objectives;
    }

    /**
     * Returns all objectives in range of a given location.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link BattleObjectiveAPI}s within range of
     *         {@code location}.
     * <p>
     * @see CombatUtils#getObjectivesWithinRange(org.lwjgl.util.vector.Vector2f,
     * float, boolean)
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
     * @param location       The location to search around.
     * @param range          How far around {@code location} to search.
     * @param sortByDistance Whether to sort the results by distance from
     *                       {@code location}.
     * <p>
     * @return A {@link List} of {@link CombatEntityAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.1
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        List<CombatEntityAPI> entities = new ArrayList<CombatEntityAPI>();
        range *= range;

        for (CombatEntityAPI tmp : Global.getCombatEngine().getShips())
        {
            if (MathUtils.getDistanceSquared(tmp, location) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : Global.getCombatEngine().getProjectiles())
        {
            if (MathUtils.getDistanceSquared(tmp, location) <= range)
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
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
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link CombatEntityAPI}s within range of
     *         {@code location}.
     * <p>
     * @see CombatUtils#getEntitiesWithinRange(org.lwjgl.util.vector.Vector2f,
     * float, boolean)
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
     * @param entity    The {@link CombatEntityAPI} to apply the force to.
     * @param direction The directional vector of the force (this will
     *                  automatically be normalized).
     * @param force     How much force to apply.
     * <p>
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
     * @param entity    The {@link CombatEntityAPI} to apply the force to.
     * @param direction The angle the force will be applied towards.
     * @param force     How much force to apply.
     * <p>
     * @since 1.2
     */
    public static void applyForce(CombatEntityAPI entity, float direction, float force)
    {
        applyForce(entity, MathUtils.getPointOnCircumference(new Vector2f(0, 0),
                1f, direction), force);
    }

    /**
     * @deprecated Use {@link Global#getCombatEngine()} instead.
     * @since 1.0
     */
    @Deprecated
    public static CombatEngineAPI getCombatEngine()
    {
        LazyLib.onDeprecatedMethodUsage(CombatUtils.class,
                "getCombatEngine()");

        return Global.getCombatEngine();
    }

    /**
     * @deprecated Use {@link CombatEngineAPI#getTotalElapsedTime(boolean)}
     * instead.
     * @since 1.2
     */
    @Deprecated
    public static float getElapsedCombatTimeIncludingPaused()
    {
        LazyLib.onDeprecatedMethodUsage(CombatUtils.class,
                "getElapsedCombatTimeIncludingPaused()");

        if (Global.getCombatEngine() == null)
        {
            return 0f;
        }

        return Global.getCombatEngine().getTotalElapsedTime(true);
    }

    /**
     * @deprecated Use {@link CombatEngineAPI#getTotalElapsedTime(boolean)}
     * instead.
     * @since 1.0
     */
    @Deprecated
    public static float getElapsedCombatTime()
    {
        LazyLib.onDeprecatedMethodUsage(CombatUtils.class,
                "getElapsedCombatTime()");

        if (Global.getCombatEngine() == null)
        {
            return 0f;
        }

        return Global.getCombatEngine().getTotalElapsedTime(false);
    }

    /**
     * @deprecated Use {@link CombatEngineAPI#getElapsedInLastFrame()} instead.
     * @since 1.4
     */
    @Deprecated
    public static float getTimeSinceLastFrame()
    {
        LazyLib.onDeprecatedMethodUsage(CombatUtils.class,
                "getTimeSinceLastFrame()");

        if (Global.getCombatEngine() == null)
        {
            return 0f;
        }

        return Global.getCombatEngine().getElapsedInLastFrame();
    }

    private CombatUtils()
    {
    }
}
