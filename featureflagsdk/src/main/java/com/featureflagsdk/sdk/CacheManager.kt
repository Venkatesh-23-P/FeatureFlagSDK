package com.featureflagsdk.sdk

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("feature_sdk_cache", Context.MODE_PRIVATE)

    fun saveConfig(json: String) {
        prefs.edit { putString("config_json", json) }
        println("FeatureSDK: Config saved to cache")
    }

    fun getConfig(): String? {
        val cached = prefs.getString("config_json", null)
        println("FeatureSDK: Cache ${if (cached != null) "hit" else "miss"}")
        return cached
    }

    fun clear() {
        prefs.edit { clear() }
        println("FeatureSDK: Cache cleared")
    }
}