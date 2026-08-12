package org.lazywizard.lazylib;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ModManagerAPI;
import com.fs.starfarer.api.ModSpecAPI;
import com.fs.starfarer.api.VersionInfoAPI;
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

    /**
     * Check a mod's version if it meets the given major/minor/path demand.
     * Useful when you need to incompatible with a lower version of a specific mod but compatible without it.
     * 
     * <b>Important note:</b> ONLY USABLE WHEN THE MOD'S VERSION IS ALL NUMBERIC
     * 
     * @param modId The ID of the mod to check.
     * @param major The minimal 'major' part demand.
     * @param minor The minimal 'minor' part demand.
     * @param patch The minimal 'patch' part demand.
     *
     * @return Meets the given major/minor/path demand or not.
     *
     * @since 2.3
     */
    public boolean isVersionNewerThan(String modId, int major, int minor, int patch) {
        if (!isModEnabled(modId)) return false;

        try {
            VersionInfoAPI spec = Global.getSettings().getModManager().getModSpec(modId).getVersionInfo();
            int currentMajor = Integer.parseInt(spec.getMajor());
            int currentMinor = Integer.parseInt(spec.getMinor());
            int currentPatch = Integer.parseInt(spec.getPatch());
            if (major < currentMajor) return false;
            if (major == currentMajor) {
                if (minor < currentMinor) return false;
                if (minor == currentMinor) {
                    if (patch < currentPatch) return false;
                    return true;
                }
            }

            return true;
        } catch (Exception ex) {
            Log.info("Mod " + modId + " 's version is not all numberic");
            return false;
        }
    }

    private ModUtils()
    {
    }
}
