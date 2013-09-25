package org.lazywizard.lazylib;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Level;
import org.lazywizard.lazylib.campaign.CargoUtils;
import org.lazywizard.lazylib.campaign.FleetUtils;
import org.lazywizard.lazylib.campaign.MessageUtils;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lazywizard.lazylib.combat.DefenseUtils;
import org.lazywizard.lazylib.combat.WeaponUtils;

/**
 * Contains information on the current version of LazyLib.
 *
 * @author LazyWizard
 * @since 1.1
 */
// TODO: Implement transient CampaignPlugin, add ModMenu plugin system
public class LazyLib extends BaseModPlugin
{
    private static final boolean IS_DEV_BUILD = false;
    private static final float LIBRARY_VERSION = 1.6f;
    private static final String GAME_VERSION = "0.6a";
    private static final Level LOG_LEVEL = (IS_DEV_BUILD ? Level.DEBUG : Level.ERROR);

    static
    {
        Global.getLogger(CollectionUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(CollisionUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(MathUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(CargoUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(FleetUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(MessageUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(AIUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(CombatUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(DefenseUtils.class).setLevel(LOG_LEVEL);
        Global.getLogger(WeaponUtils.class).setLevel(LOG_LEVEL);
    }

    /**
     * Get the version number of this LazyLib instance.
     *
     * @return The current version of LazyLib, as a {@link Float}.
     * @since 1.1
     */
    public static float getVersion()
    {
        return LIBRARY_VERSION;
    }

    /**
     * Gets the Starsector release this version was coded for.
     *
     * @return The version of Starsector this library supports, as a {@link String}.
     * @since 1.2
     */
    public static String getSupportedGameVersion()
    {
        return GAME_VERSION;
    }

    /**
     * Check if this is a dev (beta) build of LazyLib.
     *
     * @return {@code true} if this is a beta build of LazyLib, {@code false} otherwise.
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
     * @since 1.2
     */
    public static String getInfo()
    {
        return "LazyLib v" + LIBRARY_VERSION + (IS_DEV_BUILD ? "_dev" : "")
                + " for Starsector " + GAME_VERSION;
    }

    @Override
    public void onApplicationLoad() throws Exception
    {
        Global.getLogger(LazyLib.class).log(Level.INFO, "Running " + getInfo());
    }
}
