package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with weapons and weapon arcs.
 *
 * @author LazyWizard
 * @since 1.0
 */
public class WeaponUtils
{
    /**
     * Calculates the damage done by a weapon after MutableShipStats are applied.
     *
     * @param baseDamage The base damage of this weapon.
     * @param weapon The {@link WeaponAPI} to check for bonuses on.
     * @return The actual damage done by {@code weapon} after bonuses are applied.
     * @since 1.0
     */
    public static float calculateActualDamage(float baseDamage, WeaponAPI weapon)
    {
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
     * Calculates the damage done per shot by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per shot, after bonuses.
     * @since 1.0
     */
    public static float calculateDamagePerShot(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getDamagePerShot(), weapon);
    }

    /**
     * Calculates the damage done per second by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per second, after bonuses.
     * @since 1.0
     */
    public static float calculateDamagePerSecond(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getDps(), weapon);
    }

    /**
     * Calculates the damage done per burst by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per burst, after bonuses.
     * @since 1.0
     */
    public static float calculateDamagePerBurst(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getBurstDamage(), weapon);
    }

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
        if (MathUtils.getDistanceSquared(entity, weapon.getLocation())
                > Math.pow(weapon.getRange() + entity.getCollisionRadius(), 2))
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

    /*public static DefenseType getDefenseAimedAt(ShipAPI threatened, WeaponAPI weapon)
     {
     // TODO: filter out weapons that can't hit the target (CollisionClass)

     if (!isWithinArc(threatened, weapon))
     {
     return DefenseType.MISS;
     }

     if (threatened.getHullSpec().getDefenseType() == ShieldType.PHASE)
     {
     ShipSystemAPI cloak = (ShipSystemAPI) threatened.getPhaseCloak();

     if (cloak != null && cloak.isActive())
     {
     return DefenseType.PHASE;
     }
     }

     Vector2f hit = getCollisionPoint(threatened, getFiringLine(weapon));
     if (hit == null)
     {
     return DefenseType.MISS;
     }

     // TODO: check for glancing blows against shield
     ShieldAPI shield = threatened.getShield();
     if (shield != null && shield.isOn() && shield.isWithinArc(hit))
     {
     return DefenseType.SHIELD;
     }

     // TODO: Armor checks
     return DefenseType.ARMOR;

     //return DefenseType.HULL;
     }*/
    /**
     * Calculate how long it would take to turn a {@link WeaponAPI} to aim at a location.
     *
     * @param weapon The {@link WeaponAPI} to turn.
     * @param aimAt The {@link Vector2f} to aim at.
     * @return The time in seconds it would take to aim {@code weapon}.
     * @since 1.0
     */
    public static float getTimeToAim(WeaponAPI weapon, Vector2f aimAt)
    {
        ShipAPI ship = weapon.getShip();
        float turnSpeed = (ship == null ? 0
                : ship.getMutableStats().getMaxTurnRate().getModifiedValue());
        // TODO: add current turn velocity and acceleration to equation
        float time = weapon.distanceFromArc(aimAt)
                / turnSpeed;

        // Divide by zero - no ship or can't turn, only a threat if already aimed
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
     * Finds all enemy missiles within range of a {@link WeaponAPI}.
     *
     * @param weapon The weapon to detect enemies in range of.
     * @param sortByDistance Whether to sort the results by distance from {@code weapon}.
     * @return A {@link List} containing all enemy missiles within range.
     * @see WeaponUtils#getEnemyMissilesInRange(com.fs.starfarer.api.combat.WeaponAPI, boolean)
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesInRange(WeaponAPI weapon,
            boolean sortByDistance)
    {
        List<MissileAPI> missiles = new ArrayList();
        float range = weapon.getRange();
        range *= range;

        for (MissileAPI enemy : AIUtils.getEnemyMissilesOnMap(weapon.getShip()))
        {
            if (MathUtils.getDistanceSquared(enemy, weapon.getLocation()) <= range)
            {
                missiles.add(enemy);
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
     * @see WeaponUtils#getEnemyMissilesInRange(com.fs.starfarer.api.combat.WeaponAPI, boolean)
     * @since 1.4
     */
    public static List<MissileAPI> getEnemyMissilesInRange(WeaponAPI weapon)
    {
        return getEnemyMissilesInRange(weapon, false);
    }

    /**
     * Manually adjusts a weapon's aim towards a point.
     *
     * @param weapon The weapon to aim.
     * @param point The point this weapon should try to aim at.
     * @param time How long since the last frame (for turn rate calculations).
     */
    public static void aimTowardsPoint(WeaponAPI weapon, Vector2f point, float time)
    {
        float currentFacing = weapon.getCurrAngle();
        float intendedFacing = MathUtils.getFacing(MathUtils.getDirectionalVector(
                weapon.getLocation(), point));
        float facingChange = MathUtils.clampAngle(currentFacing - intendedFacing);
        boolean clockwise = facingChange < 0f;
        facingChange = Math.abs(facingChange);
        float maxChange = weapon.getTurnRate() * time;

        if (facingChange <= maxChange)
        {
            weapon.setCurrAngle(currentFacing + (facingChange * (clockwise ? 1f : -1f)));
        }
        else
        {
            weapon.setCurrAngle(currentFacing + (maxChange * (clockwise ? 1f : -1f)));
        }
    }

    public static void main(String[] args)
    {
        float turnRate = 20f;
        float currentFacing = 50f;
        float intendedFacing = MathUtils.getFacing(MathUtils.getDirectionalVector(
                new Vector2f(50f, 50f), new Vector2f(0f, 0f)));

        System.out.println("Switching facing from " + currentFacing + " to "
                + intendedFacing + ".");


        float time = 1f / 60f;
        for (int x = 0; x < 600; x++)
        {
            System.out.println("Current facing: " + currentFacing);
            float facingChange = MathUtils.clampAngle(currentFacing - intendedFacing);
            boolean clockwise = facingChange < 0;
            facingChange = Math.abs(facingChange);
            float maxChange = turnRate * time;

            if (facingChange <= maxChange)
            {
                currentFacing = currentFacing + (facingChange * (clockwise ? 1f : -1f));
            }
            else
            {
                currentFacing = currentFacing + (maxChange * (clockwise ? 1f : -1f));
            }
        }
    }

    private WeaponUtils()
    {
    }
}
