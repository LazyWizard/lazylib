package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import org.lazywizard.lazylib.BaseUtils;

public class AIUtils
{
    public static BattleObjectiveAPI getNearestObjective(CombatEntityAPI entity,
            CombatEngineAPI engine)
    {
        BattleObjectiveAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (BattleObjectiveAPI tmp : engine.getObjectives())
        {
            distance = BaseUtils.getDistance(tmp.getLocation(),
                    entity.getLocation());

            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    public static boolean canUseSystemThisFrame(ShipAPI ship, ShipSystemAPI system)
    {
        FluxTrackerAPI flux = ship.getFluxTracker();
        if (flux.isOverloadedOrVenting())
        {
            return false;
        }

        //ShipSystemAPI system = (ShipSystemAPI) ship.getPhaseCloak();
        if (system == null || system.isOutOfAmmo()
                || system.getCooldownRemaining() > 0f
                || system.getFluxPerUse() > (flux.getMaxFlux() - flux.getCurrFlux()))
        {
            return false;
        }

        return true;
    }
}
