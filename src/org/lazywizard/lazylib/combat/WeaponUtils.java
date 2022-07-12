package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.jetbrains.annotations.Nullable;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods that deal with weapons and weapon arcs.
 *
 * @author LazyWizard
 * @since 1.0
 */
// TODO: add getTimeUntilInArc(WeaponAPI weapon, Vector2f point)
// TODO: this entire class needs thorough testing for bugs
public class WeaponUtils
{
    /**
     * Checks if a {@link CombatEntityAPI} is within the arc and range of a
     * {@link WeaponAPI}.
     *
     * @param entity The {@link CombatEntityAPI} to check if {@code weapon} is
     *               aimed at.
     * @param weapon The {@link WeaponAPI} to test against.
     *
     * @return {@code true} if in arc and in range, {@code false} otherwise.
     *
     * @since 1.0
     */
    public static boolean isWithinArc(CombatEntityAPI entity, WeaponAPI weapon)
    {
        // Check if weapon is in range
        if (!MathUtils.isWithinRange(entity, weapon.getLocation(), weapon.getRange()))
        {
            return false;
        }

        // Check if weapon is aimed at the target's center
        if (weapon.distanceFromArc(entity.getLocation()) == 0)
        {
            return true;
        }

        // Check if weapon is aimed at any part of the target
        float arc = weapon.getArc() / 2f;
        Vector2f target = entity.getLocation();
        Vector2f wep = weapon.getLocation();
        Vector2f endArcLeft = MathUtils.getPointOnCircumference(wep, weapon.getRange(),
                weapon.getCurrAngle() - arc);
        Vector2f endArcRight = MathUtils.getPointOnCircumference(wep, weapon.getRange(),
                weapon.getCurrAngle() + arc);
        float radSquared = entity.getCollisionRadius() * entity.getCollisionRadius();

        // Check if target is partially in weapon arc
        return (Line2D.ptSegDistSq(
                wep.x,
                wep.y,
                endArcLeft.x,
                endArcLeft.y,
                target.x,
                target.y) <= radSquared)
                || (Line2D.ptSegDistSq(
                wep.x,
                wep.y,
                endArcRight.x,
                endArcRight.y,
                target.x,
                target.y) <= radSquared);
    }

    /**
     * Calculate how long it would take to turn a {@link WeaponAPI} to aim at
     * a location. Does NOT factor in current ship turn speed.
     *
     * @param weapon The {@link WeaponAPI} to turn.
     * @param aimAt  The {@link Vector2f} to aim at.
     *
     * @return The time in seconds it would take to aim {@code weapon}.
     *
     * @since 1.0
     */
    public static float getTimeToAim(WeaponAPI weapon, Vector2f aimAt)
    {
        float distance = Math.abs(weapon.distanceFromArc(aimAt));
        if (distance == 0f)
        {
            return 0f;
        }

        float turnSpeed = weapon.getTurnRate();
        float time = distance / turnSpeed;

        // Divide by zero - can't turn, only a threat if already aimed
        if (Float.isNaN(time))
        {
            return Float.MAX_VALUE;
        }

        return time;
    }

    /**
     * Find the closest ally in range of a {@link WeaponAPI}.
     *
     * @param weapon The {@link WeaponAPI} to search around.
     *
     * @return The allied {@link ShipAPI} closest to {@code weapon}, or
     *         {@code null} if none are in range.
     *
     * @since 1.7
     */
    @Nullable
    public static ShipAPI getNearestAllyInArc(WeaponAPI weapon)
    {
        ShipAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        ShipAPI owner = weapon.getShip();
        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp == owner)
            {
                continue;
            }

            if (tmp.getOwner() != weapon.getShip().getOwner()
                    || weapon.distanceFromArc(tmp.getLocation()) > 0f)
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp, weapon.getLocation());

            if (distance > weapon.getRange())
            {
                continue;
            }

            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Finds all allied ships within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect allies in range of.
     *
     * @return A {@link List} containing all allied ships within range.
     *
     * @since 1.7
     */
    public static List<ShipAPI> getAlliesInArc(WeaponAPI weapon)
    {
        List<ShipAPI> allies = new ArrayList<>();
        float range = weapon.getRange();

        ShipAPI owner = weapon.getShip();
        for (ShipAPI ship : AIUtils.getAlliesOnMap(weapon.getShip()))
        {
            if (ship == owner)
            {
                continue;
            }

            if (MathUtils.isWithinRange(ship, weapon.getLocation(), range)
                    && weapon.distanceFromArc(ship.getLocation()) == 0f)
            {
                allies.add(ship);
            }
        }

        return allies;
    }

    /**
     * Find the closest enemy in range of a {@link WeaponAPI}.
     *
     * @param weapon The {@link WeaponAPI} to search around.
     *
     * @return The enemy {@link ShipAPI} closest to {@code weapon}, or
     *         {@code null} if none are in range.
     *
     * @since 1.4
     */
    @Nullable
    public static ShipAPI getNearestEnemyInArc(WeaponAPI weapon)
    {
        ShipAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
        {
            if (tmp.getOwner() == weapon.getShip().getOwner()
                    || weapon.distanceFromArc(tmp.getLocation()) > 0f)
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp, weapon.getLocation());

            if (distance > weapon.getRange())
            {
                continue;
            }

            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    /**
     * Finds all enemy ships within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect enemies in range of.
     *
     * @return A {@link List} containing all enemy ships within range.
     *
     * @since 1.4
     */
    public static List<ShipAPI> getEnemiesInArc(WeaponAPI weapon)
    {
        List<ShipAPI> enemies = new ArrayList<>();
        float range = weapon.getRange();

        for (ShipAPI ship : AIUtils.getEnemiesOnMap(weapon.getShip()))
        {
            if (MathUtils.isWithinRange(ship, weapon.getLocation(), range)
                    && weapon.distanceFromArc(ship.getLocation()) == 0f)
            {
                enemies.add(ship);
            }
        }

        return enemies;
    }

    /**
     * Find the closest enemy missile in range of a {@link WeaponAPI}.
     *
     * @param weapon The {@link WeaponAPI} to search around.
     *
     * @return The enemy {@link MissileAPI} closest to {@code weapon}, or
     *         {@code null} if none are in range.
     *
     * @since 1.4
     */
    @Nullable
    public static MissileAPI getNearestEnemyMissileInArc(WeaponAPI weapon)
    {
        MissileAPI closest = null;
        float maxRange = weapon.getRange() * weapon.getRange();
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (MissileAPI tmp : Global.getCombatEngine().getMissiles())
        {
            if (tmp.getOwner() == weapon.getShip().getOwner()
                    || weapon.distanceFromArc(tmp.getLocation()) > 0f)
            {
                continue;
            }

            distanceSquared = MathUtils.getDistanceSquared(tmp.getLocation(),
                    weapon.getLocation());

            if (distanceSquared > maxRange)
            {
                continue;
            }

            if (distanceSquared < closestDistanceSquared)
            {
                closest = tmp;
                closestDistanceSquared = distanceSquared;
            }
        }

        return closest;
    }

    /**
     * Finds all enemy missiles within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect enemies in range of.
     *
     * @return A {@link List} containing all enemy missiles within range.
     *
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesInArc(WeaponAPI weapon)
    {
        List<MissileAPI> missiles = new ArrayList<>();
        float range = weapon.getRange();

        for (MissileAPI missile : AIUtils.getEnemyMissilesOnMap(weapon.getShip()))
        {
            if (MathUtils.isWithinRange(missile.getLocation(), weapon.getLocation(),
                    range) && weapon.distanceFromArc(missile.getLocation()) == 0f)
            {
                missiles.add(missile);
            }
        }

        return missiles;
    }

    /**
     * Manually adjusts a weapon's aim towards a point.
     *
     * @param weapon The weapon to aim.
     * @param point  The point this weapon should try to aim at.
     * @param time   How long since the last frame (for turn rate calculations).
     *
     * @since 1.4
     */
    public static void aimTowardsPoint(WeaponAPI weapon, Vector2f point, float time)
    {
        float currentFacing = weapon.getCurrAngle();
        float intendedFacing = VectorUtils.getAngle(weapon.getLocation(), point);
        float facingChange = currentFacing - intendedFacing;
        boolean direction = facingChange < 0f;
        facingChange = Math.abs(facingChange);
        float maxChange = weapon.getTurnRate() * time;

        if (facingChange <= maxChange)
        {
            weapon.setCurrAngle(currentFacing + (facingChange * (direction ? 1f : -1f)));
        }
        else
        {
            weapon.setCurrAngle(currentFacing + (maxChange * (direction ? 1f : -1f)));
        }
    }

    /**
     * Checks if a {@link BeamAPI} has damage target and within the arc of a
     * {@link ShipAPI}'s shield, it may not actually dealing damage in this frame.
     *
     * @param beam The {@link BeamAPI} to check
     * @param target The {@link WeaponAPI} to test against.
     *
     * @return {@code true} if in arc and in range, {@code false} otherwise.
     *
     * @since
     */
    public static boolean isHittingShield(BeamAPI beam, ShipAPI target) {
        boolean targetCheck = beam.getDamageTarget() == target;
        boolean shieldCheck = target.getShield() != null
                           && target.getShield().isOn()
                           && target.getShield().isWithinArc(beam.getTo());
        return targetCheck && shieldCheck;
    }

    private WeaponUtils()
    {
    }
}
