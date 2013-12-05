package org.lazywizard.lazylib;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Level;
import org.json.JSONObject;
import org.lazywizard.lazylib.campaign.CargoUtils;
import org.lazywizard.lazylib.campaign.FleetUtils;
import org.lazywizard.lazylib.campaign.MessageUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lazywizard.lazylib.combat.DefenseUtils;
import org.lazywizard.lazylib.combat.WeaponUtils;
import org.lazywizard.lazylib.combat.entities.AnchoredEntity;
import org.lazywizard.lazylib.combat.entities.SimpleEntity;

/**
 * Contains information on the current version of LazyLib.
 *
 * @author LazyWizard
 * @since 1.1
 */
// TODO: Implement transient CampaignPlugin, add ModMenu plugin system
public class LazyLib extends BaseModPlugin
{
    private static final String SETTINGS_FILE = "lazylib_settings.json";
    private static final boolean IS_DEV_BUILD = false;
    private static final float LIBRARY_VERSION = 1.7f;
    private static final String GAME_VERSION = "0.6.1a";
    private static Level LOG_LEVEL;

    /**
     * Get the version number of this LazyLib instance.
     *
     * @return The current version of LazyLib, as a {@link Float}.
     * <p>
     * @since 1.1
     */
    public static float getVersion()
    {
        return LIBRARY_VERSION;
    }

    /**
     * Gets the Starsector release this version was coded for.
     *
     * @return The version of Starsector this library supports, as a
     *         {@link String}.
     * <p>
     * @since 1.2
     */
    public static String getSupportedGameVersion()
    {
        return GAME_VERSION;
    }

    /**
     * Check if this is a dev (beta) build of LazyLib.
     *
     * @return {@code true} if this is a beta build of LazyLib, {@code false}
     *         otherwise.
     * <p>
     * @since 1.4
     */
    public static boolean isDevBuild()
    {
        return IS_DEV_BUILD;
    }

    /**
     * Get the library information (for startup messages, etc).
     *
     * @return A {@link String} containing information on the library.
     * <p>
     * @since 1.2
     */
    public static String getInfo()
    {
        return "LazyLib v" + LIBRARY_VERSION + (IS_DEV_BUILD ? "_dev" : "")
                + " for Starsector " + GAME_VERSION;
    }

    /**
     * Gets the log level used for all LazyLib classes.
     *
     * @return The current log level for all LazyLib classes.
     * <p>
     * @since 1.6b
     */
    public static Level getLogLevel()
    {
        return LOG_LEVEL;
    }

    /**
     * Sets the log level used for all LazyLib classes.
     *
     * @param level The minimum level of entries that will be logged.
     * <p>
     * @since 1.6
     */
    public static void setLogLevel(Level level)
    {
        Global.getLogger(LazyLib.class).log(Level.INFO,
                "Setting log level to " + level);

        // org.lazywizard.lazylib
        Global.getLogger(CollectionUtils.class).setLevel(level);
        Global.getLogger(CollisionUtils.class).setLevel(level);
        Global.getLogger(MathUtils.class).setLevel(level);
        // org.lazywizard.lazylib.campaign
        Global.getLogger(CargoUtils.class).setLevel(level);
        Global.getLogger(FleetUtils.class).setLevel(level);
        Global.getLogger(MessageUtils.class).setLevel(level);
        // org.lazywizard.lazylib.combat
        Global.getLogger(AIUtils.class).setLevel(level);
        Global.getLogger(CombatUtils.class).setLevel(level);
        Global.getLogger(DefenseUtils.class).setLevel(level);
        Global.getLogger(WeaponUtils.class).setLevel(level);
        // org.lazywizard.lazylib.combat.entities
        Global.getLogger(AnchoredEntity.class).setLevel(level);
        Global.getLogger(SimpleEntity.class).setLevel(level);

        LOG_LEVEL = level;
    }

    @Override
    public void onApplicationLoad() throws Exception
    {
        Global.getLogger(LazyLib.class).log(Level.INFO, "Running " + getInfo());

        // Load LazyLib settings from JSON file
        JSONObject settings = Global.getSettings().loadJSON(SETTINGS_FILE);
        setLogLevel(Level.toLevel(settings.getString("logLevel"), Level.ERROR));
    }
}
