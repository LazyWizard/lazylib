package org.lazywizard.lazylib;

import java.util.List;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Level;

/**
 * Contains methods for dealing with mod level (non-gameplay) tasks.
 *
 * @author LazyWizard
 * @since 1.9b
 */
public class ModUtils
{
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
            Global.getLogger(ModUtils.class).log(Level.INFO,
                    "Class " + classCanonicalName + " not found");
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
                Global.getLogger(ModUtils.class).log(Level.DEBUG,
                        "Attempting to load class: " + name);
                Class.forName(name, initializeClasses,
                        Global.getSettings().getScriptClassLoader());
            }

            return true;
        }

        return false;
    }

    private ModUtils()
    {
    }
}
