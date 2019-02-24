package org.lazywizard.lazylib;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SettingsAPI;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

/**
 * Contains methods for dealing with JSON objects.
 *
 * @author LazyWizard
 * @since 1.8
 */
public class JSONUtils
{
    /**
     * Clears all entries in a {@link JSONObject}.
     *
     * @param toClear The {@link JSONObject} to clear.
     *
     * @since 2.4
     */
    public static void clear(JSONObject toClear)
    {
        final Iterator keys = toClear.keys();
        while (keys.hasNext())
        {
            keys.next();
            keys.remove();
        }
    }

    /**
     * Transform a {@link JSONArray} of {@code int}s into a {@link Color}.
     * Use with {@link SettingsAPI}'s file loading methods to read a color
     * from a file.
     *
     * @param array The {@link JSONArray} to convert to a {@link Color}.
     *
     * @return A {@link Color} using {@code array}'s color values.
     *
     * @throws JSONException if {@code array} doesn't contain three or more {@code ints}
     * @since 1.8
     */
    public static Color toColor(JSONArray array) throws JSONException
    {
        return new Color(array.getInt(0), array.getInt(1), array.getInt(2),
                (array.length() == 4 ? array.getInt(3) : 255));
    }

    /**
     * Loads a JSON file from common data, or creates it, populates it with default values, and saves it to disk
     * if it does not already exist.
     *
     * @param filename        The filename to load (or create) in common data.
     * @param defaultJSONPath The path to a default JSON in a mod folder. If {@code filename} does not already exist in
     *                        the common data directory, it will be created and populated with the contents of this JSON
     *                        file.
     *
     * @return A wrapper around a {@link JSONObject} that allows saving to common data.
     *
     * @throws IOException
     * @throws JSONException
     * @since 2.4
     */
    public static CommonDataJSONObject loadCommonJSON(String filename, @Nullable String defaultJSONPath)
            throws IOException, JSONException
    {
        final SettingsAPI settings = Global.getSettings();
        JSONObject json;
        try
        {
            json = new JSONObject(settings.readTextFileFromCommon(filename));
        }
        // TODO: Check which exceptions are thrown by readTextFileFromCommon()
        // If the common file exists but isn't valid JSON, we should probably throw a special exception
        catch (Exception ex)
        {
            if (defaultJSONPath == null)
            {
                return new CommonDataJSONObject(filename);
            }

            json = settings.loadJSON(defaultJSONPath);
        }

        return (json.length() > 0 ? new CommonDataJSONObject(filename, json) : new CommonDataJSONObject(filename));
    }

    /**
     * Loads a JSON file from common data, or creates it if it does not already exist.
     *
     * @param filename The filename to load (or create) in common data.
     *
     * @return A wrapper around a {@link JSONObject} that allows saving to common data.
     *
     * @throws IOException
     * @throws JSONException
     * @since 2.4
     */
    public static CommonDataJSONObject loadCommonJSON(String filename) throws IOException, JSONException
    {
        return loadCommonJSON(filename, null);
    }

    /**
     * Provides a wrapper around {@link JSONObject} that allows saving and restoring its values to/from the common data
     * folder (saves/common). Use this to have global settings that persist between saves.
     *
     * @since 2.4
     */
    // TODO: Add comment support
    public static class CommonDataJSONObject extends JSONObject implements AutoCloseable
    {
        private final String filename;

        private CommonDataJSONObject(String filename, JSONObject data) throws IOException, JSONException
        {
            super(data, JSONObject.getNames(data));
            this.filename = filename;
            save();
        }

        /**
         * Creates a new {@link JSONObject} that will save to the provided filename in the common data folder.
         *
         * @param filename The filename to save to under the common data folder.
         *
         * @since 2.4
         */
        public CommonDataJSONObject(String filename)
        {
            super();
            this.filename = filename;
        }

        /**
         * Saves this {@link JSONObject}'s contents to common data. You MUST call this if you want your data to persist!
         *
         * @throws JSONException
         * @throws IOException
         * @since 2.4
         */
        public void save() throws JSONException, IOException
        {
            Global.getSettings().writeTextFileToCommon(filename, this.toString(3));
        }

        /**
         * Implemented as part of the {@link AutoCloseable} interface. You will never need to call this.
         *
         * @throws Exception
         * @since 2.4
         */
        @Override
        public void close() throws Exception
        {
            save();
        }
    }

    private JSONUtils()
    {
    }
}
