package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Level;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with weapons and weapon arcs.
 *
 * @author LazyWizard
 * @since 1.0
 */
// TODO: add getTimeUntilInArc(WeaponAPI weapon, Vector2f point)
public class WeaponUtils
{
    /**
     * Checks if a {@link CombatEntityAPI} is within the arc and range of a {@link WeaponAPI}.
     *
     * @param entity The {@link CombatEntityAPI} to check if {@code weapon} is aimed at.
     * @param weapon The {@link WeaponAPI} to test against.
     * @return {@code true} if in arc and in range, {@code false} otherwise.
     * @since 1.0
     */
    public static boolean isWithinArc(CombatEntityAPI entity, WeaponAPI weapon)
    {
        // Check if weapon is in range
        if (MathUtils.getDistance(entity, weapon.getLocation())
                > (weapon.getRange() + entity.getCollisionRadius()))
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
        Vector2f loc = entity.getLocation();
        Vector2f loc1 = weapon.getLocation();
        Vector2f loc2 = MathUtils.getPointOnCircumference(loc1, weapon.getRange(),
                weapon.getArcFacing() - arc);
        Vector2f loc3 = MathUtils.getPointOnCircumference(loc1, weapon.getRange(),
                weapon.getArcFacing() + arc);
        Line2D.Float line1 = new Line2D.Float(loc1.x, loc1.y, loc3.x, loc3.y);
        Line2D.Float line2 = new Line2D.Float(loc2.x, loc2.y, loc3.x, loc3.y);
        float radSquared = entity.getCollisionRadius() * entity.getCollisionRadius();

        if (line1.ptLineDistSq(loc.x, loc.y) < radSquared
                || line2.ptLineDistSq(loc.x, loc.y) < radSquared)
        {
            return true;
        }

        // Not aimed at the target
        return false;
    }

    /**
     * Calculate how long it would take to turn a {@link WeaponAPI} to aim at
     * a location. Does NOT factor in current ship turn speed.
     *
     * @param weapon The {@link WeaponAPI} to turn.
     * @param aimAt The {@link Vector2f} to aim at.
     * @return The time in seconds it would take to aim {@code weapon}.
     * @since 1.0
     */
    public static float getTimeToAim(WeaponAPI weapon, Vector2f aimAt)
    {
        float turnSpeed = weapon.getTurnRate();
        float time = Math.abs(weapon.distanceFromArc(aimAt)) / turnSpeed;

        // Divide by zero - can't turn, only a threat if already aimed
        if (Float.isNaN(time))
        {
            if (weapon.distanceFromArc(aimAt) == 0)
            {
                return 0f;
            }
            else
            {
                return Float.MAX_VALUE;
            }
        }

        return time;
    }

    /**
     * Find the closest enemy in range of a {@link WeaponAPI}.
     *
     * @param weapon The {@link WeaponAPI} to search around.
     * @return The enemy {@link ShipAPI} closest to {@code weapon}, or {@code null} if none are in range.
     * @since 1.4
     */
    public static ShipAPI getNearestEnemyInArc(WeaponAPI weapon)
    {
        ShipAPI closest = null;
        float maxRange = weapon.getRange() * weapon.getRange();
        float distanceSquared, closestDistanceSquared = Float.MAX_VALUE;

        for (ShipAPI tmp : Global.getCombatEngine().getShips())
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
     * Finds all enemy ships within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect enemies in range of.
     * @param sortByDistance Whether to sort the results by distance from {@code weapon}.
     * @return A {@link List} containing all enemy ships within range.
     * @since 1.4
     */
    public static List<ShipAPI> getEnemiesInArc(WeaponAPI weapon,
            boolean sortByDistance)
    {
        List<ShipAPI> enemies = new ArrayList();
        float range = weapon.getRange();

        for (ShipAPI ship : AIUtils.getEnemiesOnMap(weapon.getShip()))
        {
            if (MathUtils.getDistance(ship, weapon.getLocation()) <= range
                    && weapon.distanceFromArc(ship.getLocation()) == 0f)
            {
                enemies.add(ship);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(enemies,
                    new CollectionUtils.SortEntitiesByDistance(weapon.getLocation()));
        }

        return enemies;
    }

    /**
     * Finds all enemy ships within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect enemies in range of.
     * @return A {@link List} containing all enemy ships within range.
     * @see WeaponUtils#getEnemiesInArc(com.fs.starfarer.api.combat.WeaponAPI, boolean)
     * @since 1.4
     */
    public static List<ShipAPI> getEnemiesInArc(WeaponAPI weapon)
    {
        return getEnemiesInArc(weapon, false);
    }

    /**
     * Find the closest enemy missile in range of a {@link WeaponAPI}.
     *
     * @param weapon The {@link WeaponAPI} to search around.
     * @return The enemy {@link MissileAPI} closest to {@code weapon}, or {@code null} if none are in range.
     * @since 1.4
     */
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
     * @param sortByDistance Whether to sort the results by distance from {@code weapon}.
     * @return A {@link List} containing all enemy missiles within range.
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesInArc(WeaponAPI weapon,
            boolean sortByDistance)
    {
        List<MissileAPI> missiles = new ArrayList();
        float range = weapon.getRange();
        range *= range;

        for (MissileAPI missile : AIUtils.getEnemyMissilesOnMap(weapon.getShip()))
        {
            if (MathUtils.getDistanceSquared(missile.getLocation(), weapon.getLocation())
                    <= range && weapon.distanceFromArc(missile.getLocation()) == 0f)
            {
                missiles.add(missile);
            }
        }

        if (sortByDistance)
        {
            Collections.sort(missiles,
                    new CollectionUtils.SortEntitiesByDistance(weapon.getLocation()));
        }

        return missiles;
    }

    /**
     * Finds all enemy missiles within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect enemies in range of.
     * @return A {@link List} containing all enemy missiles within range.
     * @see WeaponUtils#getEnemyMissilesInArc(com.fs.starfarer.api.combat.WeaponAPI, boolean)
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesInArc(WeaponAPI weapon)
    {
        return getEnemyMissilesInArc(weapon, false);
    }

    /**
     * Manually adjusts a weapon's aim towards a point.
     *
     * @param weapon The weapon to aim.
     * @param point The point this weapon should try to aim at.
     * @param time How long since the last frame (for turn rate calculations).
     * @since 1.4
     */
    public static void aimTowardsPoint(WeaponAPI weapon, Vector2f point, float time)
    {
        float currentFacing = weapon.getCurrAngle();
        float intendedFacing = MathUtils.getAngle(weapon.getLocation(), point);
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
     * Calculates the damage done by a weapon after MutableShipStats are applied.
     *
     * @param baseDamage The base damage of this weapon.
     * @param weapon The {@link WeaponAPI} to check for bonuses on.
     * @return The damage done by {@code weapon} after bonuses are applied.
     * @since 1.0
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateActualDamage(float baseDamage, WeaponAPI weapon)
    {
        Global.getLogger(WeaponUtils.class).log(Level.WARN,
                "Using deprecated method calculateActualDamage(float baseDamage,"
                + " WeaponAPI weapon)");

        if (weapon.getShip() == null)
        {
            return baseDamage;
        }

        MutableShipStatsAPI stats = weapon.getShip().getMutableStats();
        switch (weapon.getType())
        {
            case BALLISTIC:
                baseDamage *= stats.getBallisticWeaponDamageMult().getModifiedValue();
                break;
            case ENERGY:
                baseDamage *= stats.getEnergyWeaponDamageMult().getModifiedValue();
                break;
            case MISSILE:
                baseDamage *= stats.getMissileWeaponDamageMult().getModifiedValue();
        }

        if (weapon.isBeam())
        {
            baseDamage *= stats.getBeamWeaponDamageMult().getModifiedValue();
        }

        return baseDamage;
    }

    /**
     * Calculates the damage done by a weapon to a ship after all
     * MutableShipStats on both sides are applied.
     *
     * @param baseDamage The base damage of this weapon.
     * @param weapon The {@link WeaponAPI} to check for bonuses on.
     * @param target The ship {@code weapon} is aiming at.
     * @param defense The defense of {@code target} aimed at (used for calculating
     * penalties).
     * @return The damage dealt by {@code weapon} after all bonuses and penalties
     * are applied.
     * @since 1.5
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateActualDamage(float baseDamage, WeaponAPI weapon,
            ShipAPI target, DefenseType defense)
    {
        Global.getLogger(WeaponUtils.class).log(Level.WARN,
                "Using deprecated method calculateActualDamage(float baseDamage,"
                + " WeaponAPI weapon, ShipAPI target, DefenseType defense)");

        MutableShipStatsAPI stats = target.getMutableStats();
        MutableShipStatsAPI shooter = (weapon.getShip() == null ? null
                : weapon.getShip().getMutableStats());

        float damageMod = 1.0f;

        switch (defense)
        {
            case SHIELD:
                switch (weapon.getDamageType())
                {
                    case ENERGY:
                        damageMod *= (shooter == null ? 1f : shooter
                                .getEnergyShieldDamageTakenMult().getModifiedValue());
                        break;
                    case FRAGMENTATION:
                        damageMod *= (shooter == null ? 1f : shooter
                                .getFragmentationShieldDamageTakenMult().getModifiedValue());
                        break;
                    case HIGH_EXPLOSIVE:
                        damageMod *= (shooter == null ? 1f : shooter
                                .getHighExplosiveShieldDamageTakenMult().getModifiedValue());
                        break;
                    case KINETIC:
                        damageMod *= (shooter == null ? 1f : shooter
                                .getKineticShieldDamageTakenMult().getModifiedValue());
                        break;
                }

                if (weapon.isBeam())
                {
                    damageMod *= stats.getBeamShieldDamageTakenMult().getModifiedValue();
                }
                else if (weapon.getType().equals(WeaponType.MISSILE))
                {
                    damageMod *= stats.getMissileShieldDamageTakenMult().getModifiedValue();
                }
                else
                {
                    damageMod *= stats.getProjectileShieldDamageTakenMult().getModifiedValue();
                }

                damageMod *= weapon.getDamageType().getShieldMult()
                        * (shooter == null ? 1f : shooter
                        .getDamageToTargetShieldsMult().getModifiedValue())
                        * stats.getShieldDamageTakenMult().getModifiedValue()
                        * stats.getShieldAbsorptionMult().getModifiedValue();
                break;
            case ARMOR:
                damageMod *= weapon.getDamageType().getArmorMult()
                        * stats.getArmorDamageTakenMult().getModifiedValue();
                break;
            case HULL:
                damageMod *= weapon.getDamageType().getHullMult()
                        * stats.getHullDamageTakenMult().getModifiedValue();
                break;
            case PHASE_OR_MISS:
                return 0f;
        }

        return calculateActualDamage(baseDamage, weapon) * damageMod;
    }

    /**
     * Calculates the damage done per shot by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per shot, after bonuses.
     * @since 1.0
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateDamagePerShot(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getDamagePerShot(), weapon);
    }

    /**
     * Calculates the damage done per shot by a weapon to a ship after all
     * MutableShipStats on both sides are applied.
     *
     * @param weapon The weapon to check.
     * @param target The ship {@code weapon} is aiming at.
     * @param defense The defense of {@code target} aimed at (used for calculating
     * penalties).
     * @return The damage dealt by {@code weapon} per shot after all bonuses
     * and penalties are applied.
     * @since 1.5
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateDamagePerShot(WeaponAPI weapon, ShipAPI target,
            DefenseType defense)
    {
        return calculateActualDamage(calculateDamagePerShot(weapon), weapon,
                target, defense);
    }

    /**
     * Calculates the damage done per second by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per second, after bonuses.
     * @since 1.0
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateDamagePerSecond(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getDps(), weapon);
    }

    /**
     * Calculates the damage done per second by a weapon to a ship after all
     * MutableShipStats on both sides are applied.
     *
     * @param weapon The weapon to check.
     * @param target The ship {@code weapon} is aiming at.
     * @param defense The defense of {@code target} aimed at (used for calculating
     * penalties).
     * @return The damage dealt by {@code weapon} per second after all bonuses
     * and penalties are applied.
     * @since 1.5
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateDamagePerSecond(WeaponAPI weapon, ShipAPI target,
            DefenseType defense)
    {
        return calculateActualDamage(calculateDamagePerSecond(weapon), weapon,
                target, defense);
    }

    /**
     * Calculates the damage done per burst by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per burst, after bonuses.
     * @since 1.0
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateDamagePerBurst(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getBurstDamage(), weapon);
    }

    /**
     * Calculates the damage done per burst by a weapon to a ship after all
     * MutableShipStats on both sides are applied.
     *
     * @param weapon The weapon to check.
     * @param target The ship {@code weapon} is aiming at.
     * @param defense The defense of {@code target} aimed at (used for calculating
     * penalties).
     * @return The damage dealt by {@code weapon} per burst after all bonuses
     * and penalties are applied.
     * @since 1.5
     * @deprecated Too complicated to test/maintain. These methods will be
     * removed after the next Starsector release.
     */
    @Deprecated
    public static float calculateDamagePerBurst(WeaponAPI weapon, ShipAPI target,
            DefenseType defense)
    {
        return calculateActualDamage(calculateDamagePerBurst(weapon), weapon,
                target, defense);
    }

    private WeaponUtils()
    {
    }
}
