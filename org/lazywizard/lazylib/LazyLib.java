package org.lazywizard.lazylib;

/**
 * Contains information on the current version of LazyLib.
 *
 * @author LazyWizard
 * @since 1.1
 */
public class LazyLib
{
    private static final boolean IS_DEV_BUILD = true;
    private static final float LIBRARY_VERSION = 1.4f;
    private static final String GAME_VERSION = "0.54.1a";

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
        return "LazyLib v" + LIBRARY_VERSION + " for Starsector " + GAME_VERSION;
    }

    private LazyLib()
    {
    }
}
