package org.lazywizard.lazylib;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ModManagerAPI;
import com.fs.starfarer.api.ModSpecAPI;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * <p>
     * Note: this does <i>not</i> initialize the class if it is present.
     *
     * @param classCanonicalName The canonical name of the class to check for
     *                           (ex: {@code "data.scripts.plugins.ExamplePlugin"}).
     *
     * @return {@code true} if the class is present, {@code false} otherwise.
     *
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

    /**
     * Loads a {@link List} of classes if a specific class is present, optionally initializing them in the process.
     *
     * @param classCanonicalName          The canonical name of the class to test for the presence of. If it is not
     *                                    present, no other classes will be loaded.
     * @param classesToLoadCanonicalNames The other classes to load if {@code classCanonicalName} is present.
     * @param initializeClasses           Whether to initialize all newly loaded classes - used if they have static
     *                                    initializers you want to run.
     *
     * @return {@code true} if {@code classCanonicalName} was present and {@code classesToLoadCanonicalNames} were
     *         loaded, {@code false} otherwise.
     *
     * @throws ClassNotFoundException If any of the classes in {@code classesToLoadCanonicalNames} do not exist.
     * @since 2.3
     */
    public static boolean loadClassesIfClassIsPresent(String classCanonicalName,
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

    /**
     * Check if a mod is enabled. Equivalent to {@link ModManagerAPI#isModEnabled(String)}.
     *
     * @param modId The ID of the mod to check.
     *
     * @return {@code true} if the mod with ID {@code modId} is enabled, {@code false} otherwise.
     *
     * @since 2.3
     */
    public static boolean isModEnabled(String modId)
    {
        return Global.getSettings().getModManager().isModEnabled(modId);
    }

    /**
     * Returns the IDs of all currently enabled mods.
     *
     * @return A {@link List} containing the Ids of all enabled mods.
     *
     * @since 2.3
     */
    public static List<String> getEnabledModIds()
    {
        final List<String> enabledMods = new ArrayList<>();
        for (ModSpecAPI mod : Global.getSettings().getModManager().getEnabledModsCopy())
        {
            enabledMods.add(mod.getId());
        }

        return enabledMods;
    }

    /**
     * Returns all vanilla files that have been explicitly overridden in a mod's mod_info.json.
     *
     * @return All vanilla files overridden by mods.
     *
     * @since 2.3
     */
    public static List<String> getOverrides()
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
