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
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Level;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.CollectionUtils.SortEntitiesByDistance;
import org.lazywizard.lazylib.LazyLib;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with the battle map in general.
 *
 * @author LazyWizard
 * @since 1.0
 */
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
     * Returns all projectiles in range of a given location, excluding missiles.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link DamagingProjectileAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location, float range)
    {
        List<DamagingProjectileAPI> projectiles = new ArrayList<>();

        for (DamagingProjectileAPI tmp : Global.getCombatEngine().getProjectiles())
        {
            if (tmp instanceof MissileAPI)
            {
                continue;
            }

            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                projectiles.add(tmp);
            }
        }

        return projectiles;
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
     * @since 1.0
     */
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location, float range)
    {
        List<MissileAPI> missiles = new ArrayList<>();

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
            {
                missiles.add(tmp);
            }
        }

        return missiles;
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
     * @since 1.0
     */
    public static List<ShipAPI> getShipsWithinRange(Vector2f location, float range)
    {
        List<ShipAPI> ships = new ArrayList<>();

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp.isShuttlePod())
            {
                continue;
            }

            if (MathUtils.isWithinRange(tmp, location, range))
            {
                ships.add(tmp);
            }
        }

        return ships;
    }

    /**
     * Returns all asteroids in range of a given location.
     *
     * @param location The location to search around.
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of asteroids within range of {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location, float range)
    {
        List<CombatEntityAPI> asteroids = new ArrayList<>();

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                asteroids.add(tmp);
            }
        }

        return asteroids;
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
     * @since 1.0
     */
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range)
    {
        List<BattleObjectiveAPI> objectives = new ArrayList<>();

        for (BattleObjectiveAPI tmp : Global.getCombatEngine().getObjectives())
        {
            if (MathUtils.isWithinRange(tmp.getLocation(), location, range))
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
     * @param range    How far around {@code location} to search.
     * <p>
     * @return A {@link List} of {@link CombatEntityAPI}s within range of
     *         {@code location}.
     * <p>
     * @since 1.0
     */
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location, float range)
    {
        List<CombatEntityAPI> entities = new ArrayList<>();

        for (CombatEntityAPI tmp : Global.getCombatEngine().getShips())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                entities.add(tmp);
            }
        }

        // This also includes missiles
        for (CombatEntityAPI tmp : Global.getCombatEngine().getProjectiles())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                entities.add(tmp);
            }
        }

        for (CombatEntityAPI tmp : Global.getCombatEngine().getAsteroids())
        {
            if (MathUtils.isWithinRange(tmp, location, range))
            {
                entities.add(tmp);
            }
        }

        return entities;
    }

    /**
     * Apply force to an object. Remember Newton's Second Law.
     *
     * Force is multiplied by 100 to avoid requiring ridiculous force amounts.
     *
     * @param entity    The {@link CombatEntityAPI} to apply the force to.
     * @param direction The directional vector of the force (this will
     *                  automatically be normalized).
     * @param force     How much force to apply. Unit is how much it takes
     *                  to modify a 100 weight object's velocity by 1 su/sec.
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
        // Don't bother going over Starsector's speed cap
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
     * Force is multiplied by 100 to avoid requiring ridiculous force amounts.
     *
     * @param entity    The {@link CombatEntityAPI} to apply the force to.
     * @param direction The angle the force will be applied towards.
     * @param force     How much force to apply. Unit is how much it takes
     *                  to modify a 100 weight object's velocity by 1 su/sec.
     * <p>
     * @since 1.2
     */
    public static void applyForce(CombatEntityAPI entity, float direction, float force)
    {
        applyForce(entity, MathUtils.getPointOnCircumference(new Vector2f(0, 0),
                1f, direction), force);
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<DamagingProjectileAPI> getProjectilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<DamagingProjectileAPI> projectiles = getProjectilesWithinRange(location, range);

        if (sortByDistance)
        {
            Collections.sort(projectiles, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return projectiles;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<MissileAPI> getMissilesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<MissileAPI> missiles = getMissilesWithinRange(location, range);

        if (sortByDistance)
        {
            Collections.sort(missiles, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return missiles;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<ShipAPI> getShipsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<ShipAPI> ships = getShipsWithinRange(location, range);

        if (sortByDistance)
        {
            Collections.sort(ships, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return ships;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<CombatEntityAPI> getAsteroidsWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<CombatEntityAPI> asteroids = new ArrayList<>();

        if (sortByDistance)
        {
            Collections.sort(asteroids, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return asteroids;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<BattleObjectiveAPI> getObjectivesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<BattleObjectiveAPI> objectives = getObjectivesWithinRange(location, range);

        if (sortByDistance)
        {
            Collections.sort(objectives,
                    new CollectionUtils.SortEntitiesByDistance(location, false));
        }

        return objectives;
    }

    /**
     * @deprecated Use the normal version of this method and call
     * {@link Collections#sort(List, Comparator)} using a
     * {@link SortEntitiesByDistance} as the {@link Comparator}.
     * @since 1.1
     */
    @Deprecated
    public static List<CombatEntityAPI> getEntitiesWithinRange(Vector2f location,
            float range, boolean sortByDistance)
    {
        LazyLib.onDeprecatedMethodUsage();

        List<CombatEntityAPI> entities = getEntitiesWithinRange(location, range);

        if (sortByDistance)
        {
            Collections.sort(entities, new CollectionUtils.SortEntitiesByDistance(location));
        }

        return entities;
    }

    /**
     * @deprecated Use {@link Global#getCombatEngine()} instead.
     * @since 1.0
     */
    @Deprecated
    public static CombatEngineAPI getCombatEngine()
    {
        LazyLib.onDeprecatedMethodUsage();

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
        LazyLib.onDeprecatedMethodUsage();

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
        LazyLib.onDeprecatedMethodUsage();

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
        LazyLib.onDeprecatedMethodUsage();

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
