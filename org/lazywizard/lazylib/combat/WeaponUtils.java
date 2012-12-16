package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BoundsAPI;
import com.fs.starfarer.api.combat.BoundsAPI.SegmentAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.BaseUtils;
import org.lazywizard.lazylib.combat.CombatUtils.DefenseType;
import org.lazywizard.lazylib.geom.Line;
import org.lwjgl.util.vector.Vector2f;

public class WeaponUtils
{
    // TODO: Rewrite after DerivedWeaponStatsAPI is released in next hotfix
    public static float calculateDamage(WeaponAPI weapon)
    {
        // Placeholder
        return (float) (100 * (Math.pow(weapon.getSize().ordinal() + 1, 3)));
    }

    /**
     * Finds the part of the ship that would be intersected by a given path.
     *
     * @param target The CombatEntityAPI to check collision with.
     * @param firingLine The Line the projectile traveled. The starting point should be the origin of the projectile.
     * @return The SegmentAPI that the projectile would hit, null if none hit.
     */
    public static SegmentAPI getCollisionSegment(CombatEntityAPI target, Line firingLine)
    {
        BoundsAPI bounds = target.getExactBounds();

        // TODO: add code for entities that lack bounds (using collision circle)
        if (bounds == null)
        {
            return null;
        }

        SegmentAPI closestSeg = null;
        Line closest = null;
        Vector2f closestIntersection = new Vector2f();

        // Convert all segments to lines, do collision checks to find closest hit
        for (SegmentAPI tmp : bounds.getSegments())
        {
            Line segment = CombatUtils.convertSegmentToLine(tmp);
            Vector2f intersection = firingLine.intersect(segment);

            // Collision = true
            if (intersection != null)
            {
                if (closest != null)
                {
                    if (BaseUtils.getDistance(firingLine.getStart(), intersection)
                            > BaseUtils.getDistance(firingLine.getStart(), closestIntersection))
                    {
                        closest = segment;
                        closestSeg = tmp;
                        closestIntersection.set(intersection);
                    }
                }
                else
                {
                    closest = segment;
                    closestSeg = tmp;
                    closestIntersection.set(intersection);
                }
            }
        }

        // Null if no segment was hit
        return closestSeg;
    }

    public static Vector2f getCollisionPoint(SegmentAPI segment, Line firingLine)
    {
        Vector2f result = firingLine.intersect(CombatUtils.convertSegmentToLine(segment));

        if (result != null)
        {
            return result;
        }

        return null;
    }

    // TODO
    public static Line getFiringLine(WeaponAPI weapon)
    {
        return null;
    }

    // TODO
    public static boolean checkCanHit(WeaponAPI weapon, CombatEntityAPI entity)
    {
        //CollisionClass wepCollide = weapon.getProjectileCollisionClass();
        //CollisionClass entCollide = entity.getCollisionClass();

        // TODO: Collision logic

        return true;
    }

    public static DefenseType getDefenseAimedAt(ShipAPI threatened, WeaponAPI weapon)
    {
        // TODO: Replace with weapon.getOrigin() equivalent, if/when added
        //Line weaponFire = new Line(4, 3);

        // TODO: filter out weapons that can't hit the target (CollisionClass)
        if (!checkCanHit(weapon, threatened))
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

        ShieldAPI shield = threatened.getShield();
        if (shield != null && shield.isOn())
        {
            // TODO: Shield collision checks
            return DefenseType.SHIELD;
        }

        // TODO: Armor checks

        return DefenseType.ARMOR;
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

    // TODO
    public static float getSecondsOfAmmoRemaining(WeaponAPI weapon)
    {
        return 0f;
    }
}
