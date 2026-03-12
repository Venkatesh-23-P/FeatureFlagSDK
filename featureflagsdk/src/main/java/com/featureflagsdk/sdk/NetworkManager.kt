package com.featureflagsdk.sdk

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class NetworkManager {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    fun fetchConfig(apiUrl: String): String? {
        return try {
            val request = Request.Builder()
                .url(apiUrl)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string()
                println("FeatureSDK: Network response -> $body")
                body
            } else {
                println("FeatureSDK: Network error - HTTP ${response.code}")
                null
            }
        } catch (e: Exception) {
            println("FeatureSDK: Network exception - ${e.message}")
            null
        }
    }
}