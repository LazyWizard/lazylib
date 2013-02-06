package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;

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
