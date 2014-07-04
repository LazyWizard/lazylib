package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.LazyLib;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with a single combat entity and how it views the
 * battle map. These methods respect the fog of war, unlike those in
 * {@link CombatUtils}.
 *
 * @author LazyWizard
 * @since 1.0
 */
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
     * Find the closest visible enemy of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return The enemy closest to {@code entity} who can be seen within the
     *         fog of war.
     * <p>
     * @since 1.0
     */
    public static ShipAPI getNearestEnemy(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (ShipAPI tmp : getEnemiesOnMap(entity))
        {
            distance = MathUtils.getDistance(tmp, entity.getLocation());
            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
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
        float distance, closestDistance = Float.MAX_VALUE;

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp == entity || tmp.getOwner() != entity.getOwner()
                    || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp, entity.getLocation());
            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Find the closest visible ship near an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return The ship closest to {@code entity} that can be seen within the
     *         fog of war.
     * <p>
     * @since 1.0
     */
    public static ShipAPI getNearestShip(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

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

            distance = MathUtils.getDistance(tmp, entity.getLocation());
            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Find the closest visible missile near entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return The {@link MissileAPI} closest to {@code entity} that can be seen
     *         within the fog of war.
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
     * Find all present and visible enemies of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return All enemies of {@code entity} on the battle map that can be seen
     *         within the fog of war.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity)
    {
        if (LazyLib.isCachingEnabled())
        {
            return CombatCache.getCachedVisibleEnemies(entity.getOwner());
        }

        List<ShipAPI> ships = Global.getCombatEngine().getShips();
        List<ShipAPI> enemies = new ArrayList<>((ships.size() / 2) + 1);

        for (ShipAPI tmp : ships)
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
     * Finds all visible enemies within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range  How far around {@code entity} to search.
     * <p>
     * @return A {@link List} containing all enemy ships within range that can
     *         be seen within the fog of war.
     * <p>
     * @since 1.0
     */
    public static List<ShipAPI> getNearbyEnemies(CombatEntityAPI entity, float range)
    {
        List<ShipAPI> enemies = new ArrayList<>();

        for (ShipAPI enemy : getEnemiesOnMap(entity))
        {
            if (MathUtils.isWithinRange(entity, enemy, range))
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
        List<ShipAPI> allies = new ArrayList<>();

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
        List<ShipAPI> allies = new ArrayList<>();
        for (ShipAPI ally : getAlliesOnMap(entity))
        {
            if (MathUtils.isWithinRange(entity, ally, range))
            {
                allies.add(ally);
            }
        }

        return allies;
    }

    /**
     * Find the closest visible enemy missile near an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return The enemy {@link MissileAPI} closest to {@code entity} that can
     *         be seen within the fog of war.
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
     * Find all present visible enemy missiles of an entity.
     *
     * @param entity The {@link CombatEntityAPI} to search around.
     * <p>
     * @return All enemy {@link MissileAPI}s of {@code entity} on the battle
     *         map that can be seen within the fog of war.
     * <p>
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesOnMap(CombatEntityAPI entity)
    {
        List<MissileAPI> missiles = new ArrayList<>();

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (tmp.getOwner() != entity.getOwner() || tmp.isFizzling())
            {
                missiles.add(tmp);
            }
        }

        return missiles;
    }

    /**
     * Finds all visible enemy missiles within a certain range around an entity.
     *
     * @param entity The entity to search around.
     * @param range  How far around {@code entity} to search.
     * <p>
     * @return A {@link List} containing all enemy missiles within range that
     *         can be seen within the fog of war.
     * <p>
     * @since 1.4
     */
    public static List<MissileAPI> getNearbyEnemyMissiles(CombatEntityAPI entity, float range)
    {
        List<MissileAPI> missiles = new ArrayList<>();

        for (MissileAPI enemy : getEnemyMissilesOnMap(entity))
        {
            if (MathUtils.isWithinRange(entity, enemy, range))
            {
                missiles.add(enemy);
            }
        }

        return missiles;
    }

    /**
     * Returns the best place to aim to hit a target, given its current location
     * and velocity. This method does not take acceleration into account.
     * <p>
     * @param point     The origin point of the object that will attempt to
     *                  collide with the target (usually a weapon's projectile
     *                  spawn point).
     * @param speed     The speed of the object that will attempt to collide
     *                  with the target (usually a projectile's travel speed).
     * @param targetLoc The location of the target.
     * @param targetVel The current velocity of the target.
     * <p>
     * @return The best point to aim towards to hit {@code target} given current
     *         velocities, or {@code null} if a collision is not possible.
     * <p>
     * @author Dark.Revenant (original by broofa @ stackoverflow.com)
     * @since 1.9
     */
    public static Vector2f getBestInterceptPoint(Vector2f point, float speed,
            Vector2f targetLoc, Vector2f targetVel)
    {
        Vector2f difference = new Vector2f(targetLoc.x - point.x, targetLoc.y - point.y);

        final float a = (targetVel.x * targetVel.x) + (targetVel.y * targetVel.y) - (speed * speed),
                b = 2f * ((targetVel.x * difference.x) + (targetVel.y * difference.y)),
                c = (difference.x * difference.x) + (difference.y * difference.y);

        Vector2f solutionSet = quad(a, b, c);
        if (solutionSet != null)
        {
            float bestFit = Math.min(solutionSet.x, solutionSet.y);
            if (bestFit < 0f)
            {
                bestFit = Math.max(solutionSet.x, solutionSet.y);
            }
            if (bestFit > 0f)
            {
                return new Vector2f(targetLoc.x + targetVel.x * bestFit,
                        targetLoc.y + targetVel.y * bestFit);
            }
        }

        // No possible intercept found
        return null;
    }

    private static Vector2f quad(float a, float b, float c)
    {
        Vector2f solution = null;

        if (Float.compare(Math.abs(a), 0) == 0)
        {
            if (Float.compare(Math.abs(b), 0) == 0)
            {
                solution = (Float.compare(Math.abs(c), 0) == 0)
                        ? new Vector2f(0, 0) : null;
            }
            else
            {
                solution = new Vector2f(-c / b, -c / b);
            }
        }
        else
        {
            float d = (b * b) - (4 * a * c);
            if (d >= 0)
            {
                d = (float) Math.sqrt(d);
                a = 2 * a;
                solution = new Vector2f((-b - d) / a, (-b + d) / a);
            }
        }

        return solution;
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

    private AIUtils()
    {
    }
}
