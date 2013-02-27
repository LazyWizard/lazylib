package org.lazywizard.lazylib;

/**
 * Contains information on the current version of LazyLib.
 *
 * @author LazyWizard
 */
public class LazyLib
{
    private static final float VERSION = 1.1f;

    /**
     * Get the version number of this LazyLib instance.
     *
     * @return The current version of LazyLib, as a {@link Float}.
     */
    public static float getVersion()
    {
        return VERSION;
    }

    private LazyLib()
    {
    }
}
