package org.lazywizard.lazylib;

import java.awt.Color;
import com.fs.starfarer.api.SettingsAPI;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Contains methods for dealing with JSON objects.
 *
 * @author LazyWizard
 * @since 1.8
 */
public class JSONUtils
{
    /**
     * Transform a {@link JSONArray} of {@code int}s into a {@link Color}.
     * Use with {@link SettingsAPI}'s file loading methods to read a color
     * from a file.
     *
     * @param array The {@link JSONArray} to convert to a {@link Color}.
     * <p>
     * @return A {@link Color} using {@code array}'s color values.
     * <p>
     * @throws JSONException if {@code array} doesn't contain three or more {@code ints}
     * @since 1.8
     */
    public static Color toColor(JSONArray array) throws JSONException
    {
        return new Color(array.getInt(0), array.getInt(1), array.getInt(2),
                (array.length() == 4 ? array.getInt(3) : 255));
    }

    private JSONUtils()
    {
    }
}
