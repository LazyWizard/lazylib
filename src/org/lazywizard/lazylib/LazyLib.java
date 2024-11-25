package org.lazywizard.lazylib;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import kotlin.KotlinVersion;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.lazywizard.lazylib.campaign.CampaignUtils;
import org.lazywizard.lazylib.campaign.CargoUtils;
import org.lazywizard.lazylib.campaign.MessageUtils;
import org.lazywizard.lazylib.campaign.orbits.EllipticalOrbit;
import org.lazywizard.lazylib.combat.AIUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lazywizard.lazylib.combat.DefenseUtils;
import org.lazywizard.lazylib.combat.WeaponUtils;
import org.lazywizard.lazylib.combat.entities.AnchoredEntity;
import org.lazywizard.lazylib.combat.entities.SimpleEntity;
import org.lazywizard.lazylib.opengl.ColorUtils;
import org.lazywizard.lazylib.opengl.DrawUtils;
import org.lazywizard.lazylib.ui.LazyFont;

import java.util.Arrays;

/**
 * Contains information on the current version of LazyLib.
 *
 * @author LazyWizard
 * @since 1.1
 */
public class LazyLib extends BaseModPlugin
{
    public static final String MOD_ID = "lw_lazylib";
    private static final String SETTINGS_FILE = "lazylib_settings.json";
    // These values are read from mod_info.json at application startup
    private static float libVersion = 0f;
    private static String gameVersion = "[UNDEFINED]";
    private static boolean cacheEnabled = false;
    private static boolean logDeprecated = false;
    private static boolean crashOnDeprecated = false;
    private static Level logLevel;

    /**
     * Returns the running version of LazyLib. If your mod requires features
     * from a certain version of LazyLib, this can be used to warn players that
     * their version is out of date.
     *
     * @return The current version of LazyLib, as a {@link Float}.
     *
     * @since 1.1
     */
    public static float getVersion()
    {
        return libVersion;
    }

    /**
     * Returns the Starsector release this version was coded for.
     *
     * @return The version of Starsector this library supports, as a
     *         {@link String}.
     *
     * @since 1.2
     */
    public static String getSupportedGameVersion()
    {
        return gameVersion;
    }

    /**
     * Checks if caching of the results of expensive methods is enabled.
     *
     * @return {@code true} if results caching is enabled, {@code false}
     *         otherwise.
     *
     * @since 1.8b
     */
    public static boolean isCachingEnabled()
    {
        return cacheEnabled;
    }

    /**
     * Gets the library information (for startup messages, etc).
     *
     * @return A {@link String} containing information on the library.
     *
     * @since 1.2
     */
    public static String getInfo()
    {
        return "LazyLib v" + libVersion +
                " with Kotlin v" + KotlinVersion.CURRENT +
                ", built for Starsector " + gameVersion;
    }

    /**
     * Returns the log level used for all other LazyLib classes. {@link LazyLib}
     * itself will always use log level {@link Level#ALL}.
     *
     * @return The current log level for all LazyLib classes.
     *
     * @since 1.6b
     */
    public static Level getLogLevel()
    {
        return logLevel;
    }

    /**
     * Sets the log level used for all other LazyLib classes.
     *
     * @param level The minimum level of entries that will be logged.
     *
     * @since 1.6
     */
    public static void setLogLevel(Level level)
    {
        Global.getLogger(LazyLib.class).setLevel(Level.ALL);

        // org.lazywizard.lazylib
        Global.getLogger(CollectionUtils.class).setLevel(level);
        Global.getLogger(CollisionUtils.class).setLevel(level);
        Global.getLogger(EllipseUtils.class).setLevel(level);
        Global.getLogger(JSONUtils.class).setLevel(level);
        Global.getLogger(MathUtils.class).setLevel(level);
        Global.getLogger(ModUtils.class).setLevel(level);
        Global.getLogger(StringUtils.class).setLevel(level);
        Global.getLogger(VectorUtils.class).setLevel(level);
        // org.lazywizard.lazylib.campaign
        Global.getLogger(CampaignUtils.class).setLevel(level);
        Global.getLogger(CargoUtils.class).setLevel(level);
        Global.getLogger(MessageUtils.class).setLevel(level);
        // org.lazywizard.lazylib.campaign.orbits
        Global.getLogger(EllipticalOrbit.class).setLevel(level);
        // org.lazywizard.lazylib.combat
        Global.getLogger(AIUtils.class).setLevel(level);
        Global.getLogger(CombatUtils.class).setLevel(level);
        Global.getLogger(DefenseUtils.class).setLevel(level);
        Global.getLogger(WeaponUtils.class).setLevel(level);
        // org.lazywizard.lazylib.combat.entities
        Global.getLogger(AnchoredEntity.class).setLevel(level);
        Global.getLogger(SimpleEntity.class).setLevel(level);
        // org.lazywizard.lazylib.opengl
        Global.getLogger(ColorUtils.class).setLevel(level);
        Global.getLogger(DrawUtils.class).setLevel(level);
        // org.lazywizard.lazylib.ui
        Global.getLogger(LazyFont.class).setLevel(level);
        Global.getLogger(LazyFont.DrawableString.class).setLevel(level);
        Global.getLogger(LazyFont.FontLoader.class).setLevel(level);

        logLevel = level;
    }

    /**
     * Called internally by LazyLib when a deprecated method is used. If
     * "logDeprecated" is set in lazylib_settings.json it will log usage of
     * those methods. If "crashOnDeprecated" is true, this method will throw a
     * {@link RuntimeException} so modders can track down the problematic code
     * using the stacktrace. You can ignore this method; there's no reason to
     * ever call it manually, and calls from outside of LazyLib's namespace
     * will be ignored.
     *
     * @since 1.7
     */
    public static void onDeprecatedMethodUsage()
    {
        // Don't do something as expensive as dumping a stack trace if we don't need to!
        if (!logDeprecated && !crashOnDeprecated)
            return;

        // Basic sanity check
        StackTraceElement[] tmp = Thread.currentThread().getStackTrace();
        if (tmp.length < 3) return;

        StackTraceElement caller = tmp[2];

        // Only do something if this method was actually called by LazyLib
        if (!caller.getClassName().startsWith("org.lazywizard.lazylib."))
            return;

        if (logDeprecated)
        {
            // Search stack trace for last non-LazyLib class and log the
            // method/line number of whatever called the deprecated method
            // Kind of hacky, but much easier to use than the old version
            for (StackTraceElement ste : Arrays.copyOfRange(tmp, 2, tmp.length))
            {
                if (!ste.getClassName().startsWith("org.lazywizard.lazylib."))
                {
                    Global.getLogger(LazyLib.class).log(Level.WARN,
                            "Called deprecated method " + caller.getClassName()
                                    + "." + caller.getMethodName() + "() from " + ste.getClassName()
                                    + "." + ste.getMethodName() + "():" + ste.getLineNumber());
                    break;
                }
            }
        }

        if (crashOnDeprecated)
        {
            throw new RuntimeException("Deprecated method "
                    + caller.getClassName() + "." + caller.getMethodName()
                    + "() used while \"crashOnDeprecated\" = true");
        }
    }

    private static String collapseVersion(@NotNull JSONObject versionObject) throws JSONException
    {
        String version = versionObject.optString("major", Float.toString(libVersion));
        if (versionObject.has("minor"))
            version += "." + versionObject.getString("minor");
        if (versionObject.has("patch"))
            version += versionObject.getString("patch"); // Assumes patch is always a character

        return version;
    }

    private static float parseVersion(@Nullable Object versionData) throws JSONException
    {
        if (versionData == null) return libVersion;

        final String version = (versionData instanceof JSONObject
                ? collapseVersion((JSONObject) versionData)
                : (String) versionData);
        final StringBuilder sb = new StringBuilder(version.length());
        for (char ch : version.toCharArray())
        {
            if (ch >= 'A' && ch <= 'z')
            {
                ch = Character.toLowerCase(ch);
                sb.append((char) (ch - ('a' - '0')));
            }
            else if (Character.isDigit(ch) || ch == '.')
                sb.append(ch);
        }

        return Float.parseFloat(sb.toString());
    }

    @Override
    public void onApplicationLoad() throws Exception
    {
        // Load LazyLib settings from JSON file
        JSONObject settings = Global.getSettings().loadJSON(SETTINGS_FILE, MOD_ID);
        setLogLevel(Level.toLevel(settings.optString("logLevel", "ERROR"), Level.ERROR));
        cacheEnabled = settings.optBoolean("enableCaching", false);
        logDeprecated = settings.optBoolean("logDeprecated", false);
        crashOnDeprecated = settings.optBoolean("crashOnDeprecated", false);

        try
        {
            settings = Global.getSettings().loadJSON("mod_info.json", MOD_ID);
            libVersion = parseVersion(settings.opt("version"));
            gameVersion = settings.optString("gameVersion", gameVersion);
        }
        catch (Exception ex)
        {
            Global.getLogger(LazyLib.class).log(Level.ERROR,
                    "Failed to open LazyLib's mod_info.json! Mod id must match LazyLib.MOD_ID ('" +
                            MOD_ID + "'). Using fallback values...", ex);
        }

        Global.getLogger(LazyLib.class).log(Level.INFO, "Running " + getInfo());
    }
}
