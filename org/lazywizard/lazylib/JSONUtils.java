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
// TODO: Clean this up, some methods may need to be redesigned to be more intuitive
public class JSONUtils
{
    public static Map<String, Object> toMap(JSONObject object,
            boolean convertJSONArraysToList) throws JSONException
    {
        Map<String, Object> asMap = new HashMap<String, Object>(object.length());
        Iterator<String> keys = object.keys();
        String key;
        Object tmp;
        while (keys.hasNext())
        {
            key = keys.next();
            tmp = object.get(key);

            if (tmp instanceof JSONObject)
            {
                asMap.put(key, toMap((JSONObject) tmp, convertJSONArraysToList));
            }
            else if (convertJSONArraysToList && tmp instanceof JSONArray)
            {
                asMap.put(key, toList((JSONArray) tmp, true));
            }
            else
            {
                asMap.put(key, tmp);
            }
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

    // TODO: convert JSONArrays inside JSONObjects if convertSubArraysToList == true
    public static List toList(JSONArray array,
            boolean convertSubArraysToList) throws JSONException
    {
        List asList = new ArrayList(array.length());
        Object tmp;
        for (int x = 0; x < array.length(); x++)
        {
            tmp = array.get(x);

            if (convertSubArraysToList && tmp instanceof JSONArray)
            {
                asList.add(toList((JSONArray) tmp, true));
            }
            else
            {
                asList.add(array.get(x));
            }
        }

        return asList;
    }

    private JSONUtils()
    {
    }
}
