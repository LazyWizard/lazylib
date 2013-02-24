package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.Line;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

/**
 * Contains methods that deal with weapons and weapon arcs.
 * @author LazyWizard
 */
public class WeaponUtils
{
    /**
     * Calculates the damage done by a weapon after MutableShipStats are applied.
     *
     * @param baseDamage The base damage of this weapon.
     * @param weapon The {@link WeaponAPI} to check for bonuses on.
     * @return The actual damage done by {@code weapon} after bonuses are applied.
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
                if (weapon.isBeam())
                {
                    baseDamage *= stats.getBeamWeaponDamageMult().getModifiedValue();
                }
                break;
            case MISSILE:
                baseDamage *= stats.getMissileWeaponDamageMult().getModifiedValue();
        }

        return baseDamage;
    }

    /**
     * Calculates the damage done per shot by a weapon after MutableShipStats are applied.
     *
     * @param weapon The weapon to check.
     * @return The actual damage done by {@code weapon} per shot, after bonuses.
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
        Line line1 = new Line(loc1, loc3);
        Line line2 = new Line(loc2, loc3);
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

    private WeaponUtils()
    {
    }
}