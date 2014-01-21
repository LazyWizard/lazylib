package org.lazywizard.lazylib;

import java.awt.Color;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author LazyWizard
 * @since 1.8
 */
public class JSONUtils
{
    /**
     *
     * @param array
     * @return
     * @throws JSONException
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
