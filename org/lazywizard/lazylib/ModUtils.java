package org.lazywizard.lazylib;

import com.fs.starfarer.api.Global;
import java.util.List;
import org.apache.log4j.Level;

/**
 * Contains methods for dealing with mod level (non-gameplay) tasks.
 *
 * @author LazyWizard
 * @since 1.9
 */
// TODO: Finish, test, make public, add to changelog
class ModUtils
{
    // TODO: Test, Javadoc, add to changelog
    // Note: this does NOT initialize the class if it is present
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
                    "Class " + classCanonicalName + " not found.");
            return false;
        }
    }

    // TODO: Test, Javadoc, add to changelog
    public static boolean loadClassesIfClassIsPresent(String classCanonicalName,
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
