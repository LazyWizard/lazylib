package org.lazywizard.lazylib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ModSpecAPI;
import org.apache.log4j.Logger;

/**
 * Contains methods for dealing with mod level (non-gameplay) tasks.
 *
 * @author LazyWizard
 * @since 1.9b
 */
public class ModUtils
{
    private static final Logger Log = Global.getLogger(ModUtils.class);

    /**
     * Checks if a class is present within the loaded mods.
     *
     * Note: this does <i>not</i> initialize the class if it is present.
     *
     * @param classCanonicalName The canonical name of the class to check for
     *                           (ex: {@code "data.scripts.plugins.ExamplePlugin"}).
     * <p>
     * @return {@code true} if the class is present, {@code false} otherwise.
     * <p>
     * @since 1.9b
     */
    public static boolean isClassPresent(String classCanonicalName)
    {
        try
        {
            Global.getSettings().getScriptClassLoader().loadClass(classCanonicalName);
            return true;
        }
        catch (ClassNotFoundException ex)
        {
            Log.info("Class " + classCanonicalName + " not found");
            return false;
        }
    }

    // TODO: Test, Javadoc, add to changelog
    static boolean loadClassesIfClassIsPresent(String classCanonicalName,
            List<String> classesToLoadCanonicalNames,
            boolean initializeClasses) throws ClassNotFoundException
    {
        if (isClassPresent(classCanonicalName))
        {
            for (String name : classesToLoadCanonicalNames)
            {
                Log.debug("Attempting to load class: " + name);
                Class.forName(name, initializeClasses,
                        Global.getSettings().getScriptClassLoader());
            }

            return true;
        }

        return false;
    }

    // TODO: Javadoc, add to changelog
    static boolean isModEnabled(String modId)
    {
        return Global.getSettings().getModManager().isModEnabled(modId);
    }

    // TODO: Test, Javadoc, add to changelog
    static List<String> getEnabledModIds()
    {
        final List<String> enabledMods = new ArrayList<>();
        for (ModSpecAPI mod : Global.getSettings().getModManager().getEnabledModsCopy())
        {
            enabledMods.add(mod.getId());
        }

        return enabledMods;
    }

    // TODO: Test, Javadoc, add to changelog
    static List<String> getOverrides()
    {
        final Set<String> overriddenFiles = new HashSet<>();
        for (ModSpecAPI mod : Global.getSettings().getModManager().getEnabledModsCopy())
        {
            overriddenFiles.addAll(mod.getFullOverrides());
        }

        return new ArrayList<>(overriddenFiles);
    }

    private ModUtils()
    {
    }
}
