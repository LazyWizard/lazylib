package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.BaseUtils;
import org.lazywizard.lazylib.combat.CombatUtils.DefenseType;

public class CombatThreat
{
    /** Controls threat generated from weapons that aren't aiming
     * at you currently, but could be within this amount of seconds
     */
    private static final float SECONDS_PLANNED_AHEAD = 3f;
    /** Ships that are helpless beyond this amount aren't considered a threat */
    private static final float IGNORE_HELPLESS_BEYOND = 5f;

    // TODO: Remove enemy argument after next hotfix in favor of weapon.getOwner()
    public static Threat getThreatFromWeapon(ShipAPI threatened, ShipAPI enemy, WeaponAPI weapon)
    {
        if (weapon.usesAmmo() && weapon.getAmmo() == 0f)
        {
            return new Threat();
        }

        float turnTime = WeaponUtils.getTimeToAim(enemy, weapon, threatened.getLocation());

        if (turnTime > SECONDS_PLANNED_AHEAD)
        {
            return new Threat();
        }

        Threat totalThreat = new Threat();
        List<Float> modifiers = new ArrayList<Float>();
        modifiers.add(1.0f);

        // Modify the threat based on how long it would take this weapon to aim
        if (turnTime != 0f)
        {
            modifiers.add(1.0f - (1.0f * (turnTime / SECONDS_PLANNED_AHEAD)));
        }

        // Further modify it based on how long it would take to get in range
        // TODO: Update to use weapon origin, tune for decaying projectiles
        if (BaseUtils.getDistance(threatened, enemy) > weapon.getRange())
        {
            float closeTime = (BaseUtils.getDistance(threatened, enemy)
                    - weapon.getRange())
                    / enemy.getMutableStats().getMaxSpeed().getModifiedValue();

            // TODO: give this a curve, given way too high a priority right now
            //modifiers.add(1.0f - (1.0f * (closeTime / SECONDS_PLANNED_AHEAD)));
        }

        // TODO: uncomment after next hotfix
        /*if (weapon.isFiring())
         {
         modifiers.add(1.2f);
         }*/


        DefenseType defenseType = WeaponUtils.getDefenseAimedAt(threatened, weapon);
        DamageType damageType = weapon.getDamageType();

        // Factor in defense efficiency
        switch (defenseType)
        {
            case MISS:
            case PHASE:
                return new Threat();
            case HULL:
                modifiers.add(damageType.getHullMult());
                break;
            case ARMOR:
                modifiers.add(damageType.getArmorMult());
                break;
            case SHIELD:
                modifiers.add(damageType.getShieldMult());
        }

        float damage = WeaponUtils.calculateDamage(weapon);

        switch (damageType)
        {
            case HIGH_EXPLOSIVE:
                totalThreat.heThreat += damage;
                break;
            case KINETIC:
                totalThreat.kineticThreat += damage;
                break;
            case ENERGY:
                totalThreat.energyThreat += damage;
                break;
            case FRAGMENTATION:
                totalThreat.fragThreat += damage;
                break;
            case OTHER:
                totalThreat.empThreat += damage;
        }

        return totalThreat.modify(modifiers);
    }

    public static Threat getThreat(ShipAPI threatened, ShipAPI enemy)
    {
        // Filter out harmless ships
        if (enemy.isHulk() || threatened.getOwner() == enemy.getOwner())
        {
            return new Threat();
        }

        FluxTrackerAPI flux = enemy.getFluxTracker();

        // Don't consider ships that will be helpless for a significant time as a threat
        if (flux.isOverloadedOrVenting() && Math.max(flux.getOverloadTimeRemaining(),
                flux.getTimeToVent()) > IGNORE_HELPLESS_BEYOND)
        {
            return new Threat();
        }

        Threat totalThreat = new Threat();
        WeaponAPI currentWep;

        for (WeaponAPI wep : enemy.getAllWeapons())
        {
            totalThreat.add(getThreatFromWeapon(threatened, enemy, wep));
        }

        return totalThreat;
    }

    public static Threat getCombinedThreat(Threat... threats)
    {
        Threat combinedThreat = new Threat();

        for (Threat tmp : threats)
        {
            combinedThreat.add(tmp);
        }

        return combinedThreat;
    }

    /**
     * Compare the threat two ships generate against each other
     *
     * @param ship1 the ship to use as the baseline
     * @param ship2 the ship to compare ship1 against
     * @return a special Threat object containing the ratios of relative
     * firepower, e.g. a kinetic threat of 2.0 means ship1 has
     * double the kinetic firepower ready to use against ship2
     */
    public Threat getRelativeThreat(ShipAPI ship1, ShipAPI ship2)
    {
        Threat ship1Threat = getThreat(ship1, ship2);
        Threat ship2Threat = getThreat(ship2, ship1);
        Threat relativeThreat = new Threat(
                ship1Threat.heThreat / ship2Threat.heThreat,
                ship1Threat.kineticThreat / ship2Threat.kineticThreat,
                ship1Threat.energyThreat / ship2Threat.energyThreat,
                ship1Threat.fragThreat / ship2Threat.fragThreat,
                ship1Threat.empThreat / ship2Threat.empThreat);

        return relativeThreat;
    }

    public static class Threat
    {
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

        public Threat modify(List<Float> modifiers)
        {
            float percent = 0f;

            for (Float tmp : modifiers)
            {
                percent += tmp;
            }

            percent /= modifiers.size();

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
