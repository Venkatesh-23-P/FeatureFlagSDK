package com.featureflagsdk.sdk

import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkManager {

    private val client = OkHttpClient()

    fun fetchConfig(apiUrl: String): String? {
        val request = Request.Builder()
            .url(apiUrl)
            .get()
            .build()

        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            response.body?.string()
        } else {
            null
        }
    }
}