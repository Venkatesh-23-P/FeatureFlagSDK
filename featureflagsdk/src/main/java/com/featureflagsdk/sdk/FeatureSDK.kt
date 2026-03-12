package com.featureflagsdk.sdk

object FeatureSDK {

    private val configMap: MutableMap<String, Any> = mutableMapOf()
    private var isInitialized = false
    private var cacheManager: CacheManager? = null
    private val networkManager = NetworkManager()

    fun initialize(
        context: android.content.Context,
        apiUrl: String,
        onReady: (() -> Unit)? = null
    ) {
        if (isInitialized) return
        isInitialized = true
        cacheManager = CacheManager(context)

        // Load cached config first
        val cachedJson = cacheManager?.getConfig()
        if (cachedJson != null) {
            parseJson(cachedJson)
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                onReady?.invoke()
            }
        }

        // Fetch latest config from API in background
        Thread {
            try {
                val response = networkManager.fetchConfig(apiUrl)
                if (response != null) {
                    cacheManager?.saveConfig(response)
                    parseJson(response)
                    android.os.Handler(android.os.Looper.getMainLooper()).post {
                        onReady?.invoke()
                    }
                } else {
                    println("FeatureSDK: API response is null")
                }
            } catch (e: Exception) {
                println("FeatureSDK: Error fetching config - ${e.message}")
            }
        }.start()

        println("FeatureSDK: Initialized")
    }

    private fun parseJson(json: String) {
        try {
            val jsonObject = org.json.JSONObject(json)
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                when (val value = jsonObject.get(key)) {
                    is Int -> configMap[key] = value
                    is Long -> configMap[key] = value.toInt()
                    is Double -> configMap[key] = value
                    is Float -> configMap[key] = value.toDouble()
                    is Boolean -> configMap[key] = value
                    is String -> configMap[key] = value
                    else -> configMap[key] = value
                }
            }
            println("FeatureSDK: Config parsed successfully -> $configMap")
        } catch (e: Exception) {
            println("FeatureSDK: Error parsing JSON - ${e.message}")
        }
    }

    fun getBool(key: String, defaultValue: Boolean = false): Boolean {
        return configMap[key] as? Boolean ?: defaultValue
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return configMap[key] as? String ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return when (val value = configMap[key]) {
            is Int -> value
            is Long -> value.toInt()
            is Double -> value.toInt()
            else -> defaultValue
        }
    }

    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return when (val value = configMap[key]) {
            is Double -> value
            is Float -> value.toDouble()
            is Int -> value.toDouble()
            else -> defaultValue
        }
    }

    fun getList(key: String): List<Any>? {
        return configMap[key] as? List<Any>
    }

    fun getObject(key: String): Map<String, Any>? {
        return configMap[key] as? Map<String, Any>
    }

    fun setConfig(config: Map<String, Any>) {
        configMap.clear()
        configMap.putAll(config)
    }

    fun isReady(): Boolean = isInitialized

    fun reset() {
        isInitialized = false
        configMap.clear()
        cacheManager?.clear()
        cacheManager = null
    }
}