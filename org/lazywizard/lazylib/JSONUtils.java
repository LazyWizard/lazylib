package org.lazywizard.lazylib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Contains methods that help with parsing {@link JSONArray}s and {@link JSONObject}s.
 *
 * @author LazyWizard
 * @since 1.7
 */
// TODO: JavaDoc this!
public class JSONUtils
{
    public static Map<String, Object> toMap(JSONObject object) throws JSONException
    {
        Map<String, Object> asMap = new HashMap<String, Object>(object.length());
        Iterator<String> keys = object.keys();
        String key;
        while (keys.hasNext())
        {
            key = keys.next();
            asMap.put(key, object.get(key));
        }

        return asMap;
    }

    public static Object[] toArray(JSONArray array) throws JSONException
    {
        Object[] asArray = new Object[array.length()];
        for (int x = 0; x < array.length(); x++)
        {
            asArray[x] = array.get(x);
        }

        return asArray;
    }

    public static List toList(JSONArray array) throws JSONException
    {
        List asList = new ArrayList(array.length());
        for (int x = 0; x < array.length(); x++)
        {
            asList.add(array.get(x));
        }

        return asList;
    }

    private JSONUtils()
    {
    }
}
