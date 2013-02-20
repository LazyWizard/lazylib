package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.geom.Circle;
import org.lazywizard.lazylib.geom.Convert;
import org.lazywizard.lazylib.geom.Line;
import org.lwjgl.util.vector.Vector2f;

public class WeaponUtils
{
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

    public static float calculateDamagePerShot(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getDamagePerShot(), weapon);
    }

    public static float calculateDamagePerSecond(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getDps(), weapon);
    }

    public static float calculateDamagePerBurst(WeaponAPI weapon)
    {
        return calculateActualDamage(weapon.getDerivedStats().getBurstDamage(), weapon);
    }

    /**
     * Finds the part of the ship that would be intersected by a given path.
     *
     * @param target The CombatEntityAPI to check collision with.
     * @param firingLine The Line the projectile traveled. The starting point should be the origin of the projectile.
     * @return The {@link Vector2f} of the point the projectile would hit at, or null if it doesn't hit.
     */
    public static Vector2f getCollisionPoint(CombatEntityAPI target, Line firingLine)
    {
        BoundsAPI bounds = target.getExactBounds();

        // Entities that lack bounds will use the collision circle instead
        if (bounds == null)
        {
            Vector2f loc = target.getLocation();
            if (firingLine.intersects(new Circle(loc.x, loc.y, target.getCollisionRadius())))
            {
                return loc;
            }
            else
            {
                return null;
            }
        }

        Vector2f closestIntersection = null;

        // Convert all segments to lines, do collision checks to find closest hit
        for (SegmentAPI tmp : bounds.getSegments())
        {
            Vector2f intersection =
                    firingLine.intersect(Convert.segmentToLine(tmp), true);

            // Collision = true
            if (intersection != null)
            {
                if (closestIntersection == null)
                {
                    closestIntersection = new Vector2f(intersection);
                }
                else if (MathUtils.getDistanceSquared(firingLine.getStart(), intersection)
                        > MathUtils.getDistanceSquared(firingLine.getStart(), closestIntersection))
                {
                    closestIntersection.set(intersection);
                }
            }
        }

        // Null if no segment was hit
        return closestIntersection;
    }

    public static Vector2f getCollisionPoint(SegmentAPI segment, Line firingLine)
    {
        Vector2f result = firingLine.intersect(Convert.segmentToLine(segment), true);

        if (result != null)
        {
            return result;
        }

        return null;
    }

    public static Line getFiringLine(WeaponAPI weapon)
    {
        Vector2f start = weapon.getLocation();
        Vector2f end = new Vector2f(MathUtils.getPointOnCircumference(start,
                weapon.getRange(), weapon.getArcFacing()));

        return new Line(start.x, start.y, end.x, end.y);
    }

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
        Vector2f loc1 = weapon.getLocation();
        Vector2f loc2 = MathUtils.getPointOnCircumference(loc1, weapon.getRange(),
                weapon.getArcFacing() - arc);
        Vector2f loc3 = MathUtils.getPointOnCircumference(loc1, weapon.getRange(),
                weapon.getArcFacing() + arc);
        Line line1 = new Line(loc1.x, loc1.y, loc3.x, loc3.y);
        Line line2 = new Line(loc2.x, loc2.y, loc3.x, loc3.y);
        float radSquared = entity.getCollisionRadius() * entity.getCollisionRadius();

        if (line1.distanceSquared(entity.getLocation()) < radSquared
                || line2.distanceSquared(entity.getLocation()) < radSquared)
        {
            return true;
        }

        // Not aimed at the target
        return false;
    }

    public static DefenseType getDefenseAimedAt(ShipAPI threatened, WeaponAPI weapon)
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
    }

    public static float getTimeToAim(ShipAPI ship, WeaponAPI weapon, Vector2f aimAt)
    {
        // TODO: add current turn velocity and acceleration to equation
        float time = weapon.distanceFromArc(aimAt)
                / ship.getMutableStats().getMaxTurnRate().getModifiedValue();

        // Divide by zero - ship can't turn, only a threat if already aimed
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
}
