package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

/**
 * Contains methods for working with fleets and fleet data.
 * @author LazyWizard
 */
public class FleetUtils
{
    public static boolean isPlayer(CampaignFleetAPI fleet)
    {
        return fleet == Global.getSector().getPlayerFleet();
    }

    private FleetUtils()
    {
    }
}
