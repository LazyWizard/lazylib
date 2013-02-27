package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

/**
 * Contains methods for working with fleets and fleet data.
 * @author LazyWizard
 */
public class FleetUtils
{
    /**
     * Checks if a fleet is the player fleet.
     *
     * @param fleet The {@link CampaignFleetAPI} to be checked.
     * @return {@code true} if {@code fleet} is the player fleet, {@code false} otherwise.
     */
    public static boolean isPlayer(CampaignFleetAPI fleet)
    {
        if (fleet == null)
        {
            return false;
        }

        return fleet == Global.getSector().getPlayerFleet();
    }

    private FleetUtils()
    {
    }
}
