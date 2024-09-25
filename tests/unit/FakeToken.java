import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.InteractionDialogImageVisual;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.AbilityPlugin;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.StatBonus;
import com.fs.starfarer.api.impl.campaign.procgen.Constellation;
import com.fs.starfarer.api.impl.campaign.procgen.SalvageEntityGenDataSpec.DropData;
import org.lwjgl.util.vector.Vector2f;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FakeToken implements SectorEntityToken
{
    private final Vector2f loc;
    private final float radius;

    public FakeToken(Vector2f loc, float radius)
    {
        this.loc = loc;
        this.radius = radius;
    }

    @Override
    public void setLocation(float x, float y)
    {
        loc.set(x, y);
    }

    @Override
    public float getRadius()
    {
        return radius;
    }

    //<editor-fold desc="Unimplemented methods">
    @Override
    public boolean isPlayerFleet()
    {
        return false;
    }

    @Override
    public MarketAPI getMarket()
    {
        return null;
    }

    @Override
    public void setMarket(MarketAPI market)
    {

    }

    @Override
    public CargoAPI getCargo()
    {
        return null;
    }

    @Override
    public Vector2f getLocation()
    {
        return null;
    }

    @Override
    public Vector2f getLocationInHyperspace()
    {
        return null;
    }

    @Override
    public OrbitAPI getOrbit()
    {
        return null;
    }

    @Override
    public void setOrbit(OrbitAPI orbit)
    {

    }

    @Override
    public String getId()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public String getFullName()
    {
        return null;
    }

    @Override
    public void setFaction(String factionId)
    {

    }

    @Override
    public LocationAPI getContainingLocation()
    {
        return null;
    }

    @Override
    public FactionAPI getFaction()
    {
        return null;
    }

    @Override
    public String getCustomDescriptionId()
    {
        return null;
    }

    @Override
    public void setCustomDescriptionId(String customDescriptionId)
    {

    }

    @Override
    public void setCustomInteractionDialogImageVisual(InteractionDialogImageVisual visual)
    {

    }

    @Override
    public InteractionDialogImageVisual getCustomInteractionDialogImageVisual()
    {
        return null;
    }

    @Override
    public void setFreeTransfer(boolean freeTransfer)
    {

    }

    @Override
    public boolean isFreeTransfer()
    {
        return false;
    }

    @Override
    public boolean hasTag(String tag)
    {
        return false;
    }

    @Override
    public void addTag(String tag)
    {

    }

    @Override
    public void removeTag(String tag)
    {

    }

    @Override
    public Collection<String> getTags()
    {
        return null;
    }

    @Override
    public void clearTags()
    {

    }

    @Override
    public void setFixedLocation(float x, float y)
    {

    }

    @Override
    public void setCircularOrbit(SectorEntityToken focus, float angle, float orbitRadius, float orbitDays)
    {

    }

    @Override
    public void setCircularOrbitPointingDown(SectorEntityToken focus, float angle, float orbitRadius, float orbitDays)
    {

    }

    @Override
    public void setCircularOrbitWithSpin(SectorEntityToken focus, float angle, float orbitRadius, float orbitDays, float minSpin, float maxSpin)
    {

    }

    @Override
    public MemoryAPI getMemory()
    {
        return null;
    }

    @Override
    public MemoryAPI getMemoryWithoutUpdate()
    {
        return null;
    }

    @Override
    public float getFacing()
    {
        return 0;
    }

    @Override
    public void setFacing(float facing)
    {

    }

    @Override
    public boolean isInHyperspace()
    {
        return false;
    }

    @Override
    public void addScript(EveryFrameScript script)
    {

    }

    @Override
    public void removeScript(EveryFrameScript script)
    {

    }

    @Override
    public void removeScriptsOfClass(Class c)
    {

    }

    @Override
    public boolean isInOrNearSystem(StarSystemAPI system)
    {
        return false;
    }

    @Override
    public boolean isInCurrentLocation()
    {
        return false;
    }

    @Override
    public Vector2f getVelocity()
    {
        return null;
    }

    @Override
    public void setInteractionImage(String category, String key)
    {

    }

    @Override
    public void setName(String name)
    {

    }

    @Override
    public boolean isAlive()
    {
        return false;
    }

    @Override
    public PersonAPI getActivePerson()
    {
        return null;
    }

    @Override
    public void setActivePerson(PersonAPI activePerson)
    {

    }

    @Override
    public boolean isVisibleToSensorsOf(SectorEntityToken other)
    {
        return false;
    }

    @Override
    public boolean isVisibleToPlayerFleet()
    {
        return false;
    }

    @Override
    public VisibilityLevel getVisibilityLevelToPlayerFleet()
    {
        return null;
    }

    @Override
    public VisibilityLevel getVisibilityLevelTo(SectorEntityToken other)
    {
        return null;
    }

    @Override
    public void addAbility(String id)
    {

    }

    @Override
    public void removeAbility(String id)
    {

    }

    @Override
    public AbilityPlugin getAbility(String id)
    {
        return null;
    }

    @Override
    public boolean hasAbility(String id)
    {
        return false;
    }

    @Override
    public Map<String, AbilityPlugin> getAbilities()
    {
        return null;
    }

    @Override
    public boolean isTransponderOn()
    {
        return false;
    }

    @Override
    public void setTransponderOn(boolean transponderOn)
    {

    }

    @Override
    public void addFloatingText(String text, Color color, float duration)
    {

    }

    @Override
    public SectorEntityToken getLightSource()
    {
        return null;
    }

    @Override
    public Color getLightColor()
    {
        return null;
    }

    @Override
    public void setMemory(MemoryAPI memory)
    {

    }

    @Override
    public Map<String, Object> getCustomData()
    {
        return null;
    }

    @Override
    public Color getIndicatorColor()
    {
        return null;
    }

    @Override
    public CustomCampaignEntityPlugin getCustomPlugin()
    {
        return null;
    }

    @Override
    public float getCircularOrbitRadius()
    {
        return 0;
    }

    @Override
    public float getCircularOrbitPeriod()
    {
        return 0;
    }

    @Override
    public SectorEntityToken getOrbitFocus()
    {
        return null;
    }

    @Override
    public void setId(String id)
    {

    }

    @Override
    public String getAutogenJumpPointNameInHyper()
    {
        return null;
    }

    @Override
    public void setAutogenJumpPointNameInHyper(String autogenJumpPointNameInHyper)
    {

    }

    @Override
    public boolean isSkipForJumpPointAutoGen()
    {
        return false;
    }

    @Override
    public void setSkipForJumpPointAutoGen(boolean skipForJumpPointAutoGen)
    {

    }

    @Override
    public float getCircularOrbitAngle()
    {
        return 0;
    }

    @Override
    public String getCustomEntityType()
    {
        return null;
    }

    @Override
    public float getSensorStrength()
    {
        return 0;
    }

    @Override
    public void setSensorStrength(Float sensorStrength)
    {

    }

    @Override
    public float getSensorProfile()
    {
        return 0;
    }

    @Override
    public void setSensorProfile(Float sensorProfile)
    {

    }

    @Override
    public StatBonus getDetectedRangeMod()
    {
        return null;
    }

    @Override
    public StatBonus getSensorRangeMod()
    {
        return null;
    }

    @Override
    public float getBaseSensorRangeToDetect(float sensorProfile)
    {
        return 0;
    }

    @Override
    public boolean hasSensorStrength()
    {
        return false;
    }

    @Override
    public boolean hasSensorProfile()
    {
        return false;
    }

    @Override
    public float getMaxSensorRangeToDetect(SectorEntityToken other)
    {
        return 0;
    }

    @Override
    public boolean isDiscoverable()
    {
        return false;
    }

    @Override
    public void setDiscoverable(Boolean discoverable)
    {

    }

    @Override
    public CustomEntitySpecAPI getCustomEntitySpec()
    {
        return null;
    }

    @Override
    public List<DropData> getDropValue()
    {
        return null;
    }

    @Override
    public List<DropData> getDropRandom()
    {
        return null;
    }

    @Override
    public void addDropValue(String group, int value)
    {

    }

    @Override
    public void addDropRandom(String group, int chances)
    {

    }

    @Override
    public void addDropRandom(String group, int chances, int value)
    {

    }

    @Override
    public boolean isExpired()
    {
        return false;
    }

    @Override
    public void setExpired(boolean expired)
    {

    }

    @Override
    public float getSensorFaderBrightness()
    {
        return 0;
    }

    @Override
    public float getSensorContactFaderBrightness()
    {
        return 0;
    }

    @Override
    public void forceSensorFaderBrightness(float b)
    {

    }

    @Override
    public Float getDiscoveryXP()
    {
        return null;
    }

    @Override
    public void setDiscoveryXP(Float discoveryXP)
    {

    }

    @Override
    public boolean hasDiscoveryXP()
    {
        return false;
    }

    @Override
    public void addDropValue(DropData data)
    {

    }

    @Override
    public void addDropRandom(DropData data)
    {

    }

    @Override
    public void setAlwaysUseSensorFaderBrightness(Boolean alwaysUseSensorFaderBrightness)
    {

    }

    @Override
    public Boolean getAlwaysUseSensorFaderBrightness()
    {
        return null;
    }

    @Override
    public void advance(float amount)
    {

    }

    @Override
    public boolean hasScriptOfClass(Class c)
    {
        return false;
    }

    @Override
    public void setContainingLocation(LocationAPI location)
    {

    }

    @Override
    public void clearAbilities()
    {

    }

    @Override
    public Constellation getConstellation()
    {
        return null;
    }

    @Override
    public boolean isStar()
    {
        return false;
    }

    @Override
    public Float getSalvageXP()
    {
        return null;
    }

    @Override
    public void setSalvageXP(Float salvageXP)
    {

    }

    @Override
    public boolean hasSalvageXP()
    {
        return false;
    }

    @Override
    public void setDetectionRangeDetailsOverrideMult(Float detectionRangeDetailsOverrideMult)
    {

    }

    @Override
    public Float getDetectionRangeDetailsOverrideMult()
    {
        return null;
    }

    @Override
    public VisibilityLevel getVisibilityLevelOfPlayerFleet()
    {
        return null;
    }

    @Override
    public void setCircularOrbitAngle(float angle)
    {

    }

    @Override
    public void addFloatingText(String text, Color color, float duration, boolean showWhenOnlySensorContact)
    {

    }

    @Override
    public boolean isSystemCenter()
    {
        return false;
    }

    @Override
    public StarSystemAPI getStarSystem()
    {
        return null;
    }

    @Override
    public void clearFloatingText()
    {

    }

    @Override
    public void autoUpdateHyperLocationBasedOnInSystemEntityAtRadius(SectorEntityToken sectorEntityToken, float v)
    {

    }

    @Override
    public void forceSensorContactFaderBrightness(float b)
    {

    }

    @Override
    public void forceSensorFaderOut()
    {

    }

    @Override
    public void setLightSource(SectorEntityToken star, Color color)
    {

    }

    @Override
    public List<EveryFrameScript> getScripts()
    {
        return Collections.emptyList();
    }

    @Override
    public float getExtendedDetectedAtRange()
    {
        return 0;
    }

    @Override
    public void setExtendedDetectedAtRange(Float extendedDetectedAtRange)
    {
    }

    @Override
    public void fadeOutIndicator()
    {
    }

    @Override
    public void fadeInIndicator()
    {
    }

    @Override
    public void forceOutIndicator()
    {
    }
    //</editor-fold>
}
