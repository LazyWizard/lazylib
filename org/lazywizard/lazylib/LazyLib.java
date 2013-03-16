package org.lazywizard.lazylib;

/**
 * Contains information on the current version of LazyLib.
 *
 * @author LazyWizard
 */
public class LazyLib
{
    private static final float LIBRARY_VERSION = 1.2f;
    private static final String GAME_VERSION = "0.54.1a";

    /**
     * Get the version number of this LazyLib instance.
     *
     * @return The current version of LazyLib, as a {@link Float}.
     */
    public static float getVersion()
    {
        return LIBRARY_VERSION;
    }

    /**
     * Gets the Starsector release this version was coded for.
     *
     * @return The version of Starsector this library supports, as a {@link String}.
     */
    public static String getSupportedGameVersion()
    {
        return GAME_VERSION;
    }

    /**
     * Get the library information (for startup messages, etc).
     *
     * @return A {@link String} containing information on the library.
     */
    public static String getInfo()
    {
        return "LazyLib v" + LIBRARY_VERSION + " for Starsector " + GAME_VERSION;
    }

    private LazyLib()
    {
    }
}