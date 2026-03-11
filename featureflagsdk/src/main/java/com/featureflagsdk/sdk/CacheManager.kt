package com.featureflagsdk.sdk

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class CacheManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("feature_sdk_cache", Context.MODE_PRIVATE)

    fun saveConfig(json: String) {
        prefs.edit().putString("config_json", json).apply()
    }

    fun getConfig(): String? {
        return prefs.getString("config_json", null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}