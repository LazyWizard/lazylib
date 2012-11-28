package data.scripts.lazylib.combat.ai;

import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import data.scripts.lazylib.combat.CombatUtils;

public class CombatThreat
{
    /** Controls threat generated from weapons that aren't aiming
     * at you currently, but could be within this amount of seconds
     */
    private static final float SECONDS_PLANNED_AHEAD = 3f;
    /** Ships that are helpless beyond this amount aren't considered a threat */
    private static final float IGNORE_HELPLESS_BEYOND = 5f;

    public static Threat getThreatFromWeapon(ShipAPI threatened, ShipAPI enemy, WeaponAPI weapon)
    {
        if (weapon.getAmmo() == 0f)
        {
            return Threat.EMPTY_THREAT;
        }

        float turnTime = weapon.distanceFromArc(threatened.getLocation())
                / enemy.getMutableStats().getMaxTurnRate().getModifiedValue();

        // Divide by zero - enemy ship can't turn, only a threat if already aimed
        if (Float.isNaN(turnTime))
        {
            if (weapon.distanceFromArc(threatened.getLocation()) == 0)
            {
                turnTime = 0;
            }
            else
            {
                return Threat.EMPTY_THREAT;
            }
        }
        else if (turnTime > SECONDS_PLANNED_AHEAD)
        {
            return Threat.EMPTY_THREAT;
        }

        Threat totalThreat = new Threat();
        // Placeholder due to WeaponAPI's lack of getDamage()
        float tmpThreat = (float) (100 * (Math.pow(weapon.getSize().ordinal() + 1, 3)));
        float modifier = 1.0f;

        // Modify the threat based on how long it would take this weapon to aim
        if (turnTime != 0f)
        {
            modifier -= (1.0f * (turnTime / SECONDS_PLANNED_AHEAD));
        }

        // Further modify it based on how long it would take to get in range
        if (CombatUtils.getDistance(threatened, enemy) > weapon.getRange())
        {
            float closeTime = (CombatUtils.getDistance(threatened, enemy)
                    - weapon.getRange())
                    / enemy.getMutableStats().getMaxSpeed().getModifiedValue();
            modifier -= (1.0f * (closeTime / SECONDS_PLANNED_AHEAD));
        }

        switch (weapon.getDamageType())
        {
            case HIGH_EXPLOSIVE:
                totalThreat.heThreat += tmpThreat;
                break;
            case KINETIC:
                totalThreat.kineticThreat += tmpThreat;
                break;
            case ENERGY:
                totalThreat.energyThreat += tmpThreat;
                break;
            case FRAGMENTATION:
                totalThreat.fragThreat += tmpThreat;
                break;
            case OTHER:
                totalThreat.empThreat += tmpThreat;
        }

        return totalThreat.modify(modifier);
    }

    public static Threat getThreat(ShipAPI threatened, ShipAPI enemy)
    {
        // Don't consider allies as threats
        if (threatened.getOwner() == enemy.getOwner())
        {
            return Threat.EMPTY_THREAT;
        }

        FluxTrackerAPI flux = enemy.getFluxTracker();

        // Don't consider ships that will be helpless for a significant time as a threat
        if (flux.isOverloadedOrVenting() && Math.max(flux.getOverloadTimeRemaining(),
                flux.getTimeToVent()) > IGNORE_HELPLESS_BEYOND)
        {
            return Threat.EMPTY_THREAT;
        }

        Threat totalThreat = new Threat();
        WeaponAPI currentWep;

        for (WeaponAPI wep : enemy.getAllWeapons())
        {
            totalThreat.add(getThreatFromWeapon(threatened, enemy, wep));
        }

        return totalThreat;
    }

    // I miss operator overloading
    public static Threat getCombinedThreat(Threat t1, Threat t2)
    {
        return new Threat(t1.heThreat + t2.heThreat, t1.kineticThreat
                + t2.kineticThreat, t1.energyThreat + t2.energyThreat,
                t1.fragThreat + t2.fragThreat, t1.empThreat + t2.empThreat);
    }

    public static class Threat
    {
        public static final Threat EMPTY_THREAT = new Threat(0, 0, 0, 0, 0);
        private float heThreat = 0f, kineticThreat = 0f, energyThreat = 0f,
                fragThreat = 0f, empThreat = 0f;

        public Threat(float heThreat, float kineticThreat, float energyThreat,
                float fragThreat, float empThreat)
        {
            this.heThreat = heThreat;
            this.kineticThreat = kineticThreat;
            this.energyThreat = energyThreat;
            this.fragThreat = fragThreat;
            this.empThreat = empThreat;
            clamp();
        }

        public Threat()
        {
        }

        private void clamp()
        {
            heThreat = Math.max(0, heThreat);
            kineticThreat = Math.max(0, kineticThreat);
            energyThreat = Math.max(0, energyThreat);
            fragThreat = Math.max(0, fragThreat);
            empThreat = Math.max(0, empThreat);
        }

        public Threat add(Threat toAdd)
        {
            heThreat += toAdd.heThreat;
            kineticThreat += toAdd.kineticThreat;
            energyThreat += toAdd.energyThreat;
            fragThreat += toAdd.fragThreat;
            empThreat += toAdd.empThreat;
            clamp();

            return this;
        }

        public Threat modify(float percent)
        {
            heThreat *= percent;
            kineticThreat *= percent;
            energyThreat *= percent;
            fragThreat *= percent;
            empThreat *= percent;
            clamp();
            return this;
        }

        public void addThreat(DamageType type, float amount)
        {
            switch (type)
            {
                case HIGH_EXPLOSIVE:
                    heThreat += amount;
                    break;
                case KINETIC:
                    kineticThreat += amount;
                    break;
                case ENERGY:
                    energyThreat += amount;
                    break;
                case FRAGMENTATION:
                    fragThreat += amount;
                    break;
                case OTHER:
                    empThreat += amount;
                    break;
            }

            clamp();
        }

        public float getThreat(DamageType type)
        {
            switch (type)
            {
                case HIGH_EXPLOSIVE:
                    return heThreat;
                case KINETIC:
                    return kineticThreat;
                case ENERGY:
                    return energyThreat;
                case FRAGMENTATION:
                    return fragThreat;
                case OTHER:
                    return empThreat;
            }

            return 0f;
        }

        public float getTotalThreat(boolean includeEMP)
        {
            float totalThreat = heThreat + kineticThreat + energyThreat + fragThreat;

            if (includeEMP)
            {
                totalThreat += empThreat;
            }

            return totalThreat;
        }
    }
}
