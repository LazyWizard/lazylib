package org.lazywizard.lazylib.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SpawnPointPlugin;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.plugins.LevelupPlugin;
import data.scripts.plugins.LevelupPluginImpl;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class CheatDetector implements SpawnPointPlugin
{
    private static final Set APTITUDE_IDS = new HashSet(), SKILL_IDS = new HashSet();
    private static final String[] CHEAT_MOD_CLASSES =
    {
        "data.scripts.console.Console", // Console mod
    };
    private boolean allowDevMode = false;
    private boolean allowCheatMods = false;
    private boolean validateStats = true;
    private transient boolean hasChecked = false;

    static
    {
        // Aptitudes
        APTITUDE_IDS.add("combat");
        APTITUDE_IDS.add("leadership");
        APTITUDE_IDS.add("technology");
        APTITUDE_IDS.add("industry");

        // Combat skills
        SKILL_IDS.add("missile_specialization");
        SKILL_IDS.add("ordnance_expert");
        SKILL_IDS.add("damage_control");
        SKILL_IDS.add("target_analysis");
        SKILL_IDS.add("evasive_action");
        SKILL_IDS.add("helmsmanship");
        SKILL_IDS.add("flux_modulation");

        // Leadership skills
        SKILL_IDS.add("coordinated_maneuvers");
        SKILL_IDS.add("advanced_tactics");
        SKILL_IDS.add("command_experience");
        SKILL_IDS.add("fleet_logistics");

        // Technology skills
        SKILL_IDS.add("gunnery_implants");
        SKILL_IDS.add("applied_physics");
        SKILL_IDS.add("flux_dynamics");
        SKILL_IDS.add("computer_systems");
        SKILL_IDS.add("construction");
        SKILL_IDS.add("mechanical_engineering");
        SKILL_IDS.add("field_repairs");
        SKILL_IDS.add("navigation");

        // Industry skills

        // Mod-added skills
        SKILL_IDS.add("vfleet_multi_fleet_command"); // Fleet Control
        SKILL_IDS.add("vfleet_production_efficiency"); // Fleet Control
        SKILL_IDS.add("vfleet_mining"); // Fleet Control
        SKILL_IDS.add("vfleet_trade_efficiency"); // Fleet Control
        SKILL_IDS.add("vfleet_trade_contracts"); // Fleet Control
    }

    public void setAllowDevMode(boolean allowDevMode)
    {
        this.allowDevMode = allowDevMode;
    }

    public void setAllowCheatMods(boolean allowCheatMods)
    {
        this.allowCheatMods = allowCheatMods;
    }

    public void setValidateStats(boolean validateStats)
    {
        this.validateStats = validateStats;
    }

    private boolean checkCheating()
    {
        if (!allowDevMode && Global.getSettings().getBoolean("devMode"))
        {
            Global.getSector().addMessage("Dev mode detected.");
            //return true;
        }

        if (!allowCheatMods)
        {
            ClassLoader tmp = Global.getSettings().getScriptClassLoader();
            for (int x = 0; x < CHEAT_MOD_CLASSES.length; x++)
            {
                try
                {
                    //tmp.loadClass(CHEAT_MOD_CLASSES[x]);
                    Class.forName(CHEAT_MOD_CLASSES[x], false, tmp);
                    Global.getSector().addMessage("Found restricted class "
                            + CHEAT_MOD_CLASSES[x]);
                    //return true;
                }
                catch (ClassNotFoundException ex)
                {
                }
            }
        }

        if (validateStats)
        {
            float skillPoints, aptitudePoints, level;
            MutableCharacterStatsAPI player =
                    Global.getSector().getPlayerFleet().getCommanderStats();

            aptitudePoints = player.getAptitudePoints();
            for (Iterator aptitudes = APTITUDE_IDS.iterator(); aptitudes.hasNext();)
            {
                aptitudePoints += player.getAptitudeLevel((String) aptitudes.next());
            }

            skillPoints = player.getSkillPoints();
            for (Iterator skills = SKILL_IDS.iterator(); skills.hasNext();)
            {
                skillPoints += player.getSkillLevel((String) skills.next());
            }

            level = (skillPoints - 4) / 2;

            float expectedApt = 0, expectedSkill = 0;
            LevelupPlugin tmp = new LevelupPluginImpl();

            for (int x = 1; x <= level; x++)
            {
                expectedApt += tmp.getAptitudePointsAtLevel(x);
                expectedSkill += tmp.getSkillPointsAtLevel(x);
            }

            if (expectedApt != aptitudePoints || expectedSkill != skillPoints)
            {
                Global.getSector().addMessage("Level: " + level + " |  AP: "
                        + aptitudePoints + "(" + expectedApt + ") | SP: "
                        + skillPoints + "(" + expectedSkill + ")");
                //return true;
            }
        }

        return false;
    }

    @Override
    public void advance(SectorAPI sector, LocationAPI location)
    {
        if (!hasChecked)
        {
            hasChecked = true;

            if (checkCheating())
            {
                onCaughtCheating();
            }
        }
    }

    protected abstract void onCaughtCheating();

    public Object readResolve()
    {
        hasChecked = false;
        return this;
    }
}