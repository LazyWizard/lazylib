package org.lazywizard.lazylib.combat;

import com.fs.starfarer.api.combat.BattleObjectiveAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.FluxTrackerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipSystemAPI;
import java.util.ArrayList;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;

public class AIUtils
{
    public static BattleObjectiveAPI getNearestObjective(CombatEntityAPI entity)
    {
        BattleObjectiveAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (BattleObjectiveAPI tmp : CombatUtils.getCombatEngine().getObjectives())
        {
            distance = MathUtils.getDistance(tmp.getLocation(),
                    entity.getLocation());

            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    public static ShipAPI getNearestEnemy(CombatEntityAPI entity)
    {
        ShipAPI closest = null;
        float distance, closestDistance = Float.MAX_VALUE;

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getOwner() == entity.getOwner() || tmp.isHulk() || tmp.isShuttlePod())
            {
                continue;
            }

            distance = MathUtils.getDistance(tmp.getLocation(),
                    entity.getLocation());

            if (distance < closestDistance)
            {
                closest = tmp;
                closestDistance = distance;
            }
        }

        return closest;
    }

    public static List<ShipAPI> getEnemiesOnMap(CombatEntityAPI entity)
    {
        List<ShipAPI> enemies = new ArrayList<ShipAPI>();

        for (ShipAPI tmp : CombatUtils.getCombatEngine().getShips())
        {
            if (tmp.getOwner() != entity.getOwner() && !tmp.isHulk() && !tmp.isShuttlePod())
            {
                enemies.add(tmp);
            }
        }

        return enemies;
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
