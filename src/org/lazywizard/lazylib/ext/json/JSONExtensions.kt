package org.lazywizard.lazylib.ext.json

import com.fs.starfarer.api.SettingsAPI
import org.json.JSONArray
import org.json.JSONObject

inline fun SettingsAPI.loadCoreCsv(id: String, path: String): JSONArray = this.getMergedSpreadsheetDataForMod(id, path, "starsector-core")

inline fun JSONObject.getFloat(key: String): Float = this.getDouble(key).toFloat()
inline fun JSONObject.optFloat(key: String): Float = this.optDouble(key).toFloat()
inline fun JSONObject.optFloat(key: String, defaultValue: Float): Float = this.optDouble(key, defaultValue.toDouble()).toFloat()

inline fun JSONArray.getFloat(index: Int): Float = this.getDouble(index).toFloat()
inline fun JSONArray.optFloat(index: Int): Float = this.optDouble(index).toFloat()
inline fun JSONArray.optFloat(index: Int, defaultValue: Float): Float = this.optDouble(index, defaultValue.toDouble()).toFloat()

operator fun JSONArray.iterator() : Iterator<JSONObject> = JSONIterator(this)

private class JSONIterator(val src: JSONArray) : Iterator<JSONObject>{
    var index = 0

    override fun next(): JSONObject {
        index++
        return src.getJSONObject(index - 1)
    }

    override fun hasNext(): Boolean = (index < src.length())
}