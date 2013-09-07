package org.lazywizard.lazylib.campaign.dialog;

import com.fs.starfarer.api.Script;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.ai.CampaignFleetAIAPI;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetLogisticsAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.MutableFleetStatsAPI;
import org.lwjgl.util.vector.Vector2f;

public class FakeFleet implements CampaignFleetAPI
{
    private String name = "Test Fleet";
    private Vector2f location = new Vector2f();

    @Override
    public void setLocation(float x, float y)
    {
        location.set(x, y);
    }

    @Override
    public boolean isAlive()
    {
        return true;
    }

    @Override
    public void addAssignment(FleetAssignment assignment, SectorEntityToken target, float maxDurationInDays)
    {
    }

    @Override
    public void addAssignment(FleetAssignment assignment, SectorEntityToken target, float maxDurationInDays, Script onCompletion)
    {
    }

    @Override
    public void clearAssignments()
    {
    }

    @Override
    public void setPreferredResupplyLocation(SectorEntityToken token)
    {
    }

    @Override
    public FactionAPI getFaction()
    {
        return null;
    }

    @Override
    public Vector2f getVelocity()
    {
        return new Vector2f(0, 0);
    }

    @Override
    public Vector2f getLocation()
    {
        return location;
    }

    @Override
    public LocationAPI getContainingLocation()
    {
        return null;
    }

    @Override
    public PersonAPI getCommander()
    {
        return null;
    }

    @Override
    public MutableCharacterStatsAPI getCommanderStats()
    {
        return null;
    }

    @Override
    public FleetMemberAPI getFlagship()
    {
        return null;
    }

    @Override
    public boolean isPlayerFleet()
    {
        return false;
    }

    @Override
    public FleetDataAPI getFleetData()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public float getTotalSupplyCostPerDay()
    {
        return 20f;
    }

    @Override
    public int getNumCapitals()
    {
        return 1;
    }

    @Override
    public int getNumCruisers()
    {
        return 2;
    }

    @Override
    public int getNumDestroyers()
    {
        return 3;
    }

    @Override
    public int getNumFrigates()
    {
        return 4;
    }

    @Override
    public int getNumFighters()
    {
        return 5;
    }

    @Override
    public float getTravelSpeed()
    {
        return 125f;
    }

    @Override
    public CargoAPI getCargo()
    {
        return null;
    }

    @Override
    public OrbitAPI getOrbit()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getFullName()
    {
        return name;
    }

    @Override
    public void setFaction(String factionId)
    {
    }

    @Override
    public boolean isInCurrentLocation()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInHyperspace()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FleetLogisticsAPI getLogistics()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeFleetMemberWithDestructionFlash(FleetMemberAPI arg0)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CampaignFleetAIAPI getAI()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getFleetPoints()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNameWithFaction()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidPlayerFleet()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNoEngaging(float arg0)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MutableFleetStatsAPI getStats()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMoveDestination(float arg0, float arg1)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMoveDestinationOverride(float arg0, float arg1)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SectorEntityToken getInteractionTarget()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setInteractionTarget(SectorEntityToken arg0)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isInHyperspaceTransition()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOrbit(OrbitAPI arg0)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getRadius()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCustomDescriptionId()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCustomDescriptionId(String arg0)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
