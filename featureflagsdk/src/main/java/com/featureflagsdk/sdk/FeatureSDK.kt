package com.featureflagsdk.sdk

object FeatureSDK {

    private val configMap: MutableMap<String, Any> = mutableMapOf()

    private var isInitialized = false
    private var cacheManager: CacheManager? = null
    private val networkManager = NetworkManager()


    fun initialize(context: android.content.Context, apiUrl: String) {
        if (isInitialized) return
        isInitialized = true
        cacheManager = CacheManager(context)

        // Load cached config
        val cachedJson = cacheManager?.getConfig()

        if (cachedJson != null) {
            val jsonObject = org.json.JSONObject(cachedJson)

            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                configMap[key] = jsonObject.get(key)
            }
        }

        // Fetch latest config from API
        Thread {
            val response = networkManager.fetchConfig(apiUrl)

            if (response != null) {

                cacheManager?.saveConfig(response)

                val jsonObject = org.json.JSONObject(response)

                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    configMap[key] = jsonObject.get(key)
                }
            }
        }.start()

        println("Feature SDK Initialized")
    }



    fun getBool(key: String, defaultValue: Boolean = false): Boolean {
        return configMap[key] as? Boolean ?: defaultValue
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return configMap[key] as? String ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return configMap[key] as? Int ?: defaultValue
    }

    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return configMap[key] as? Double ?: defaultValue
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
}