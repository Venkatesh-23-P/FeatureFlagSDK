package com.featureflagsdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.featureflagsdk.ui.theme.FeatureFlagSDKTheme
import com.featureflagsdk.sdk.FeatureSDK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("FeatureSDK: App Started")

        FeatureSDK.initialize(
            context = this,
            apiUrl = "https://jsonplaceholder.typicode.com/todos/1",
            onReady = {
                val completed = FeatureSDK.getBool("completed")
                println("FeatureSDK: Bool Value (completed) -> $completed")

                val userId = FeatureSDK.getInt("userId")
                println("FeatureSDK: Int Value (userId) -> $userId")

                val title = FeatureSDK.getString("title")
                println("FeatureSDK: String Value (title) -> $title")

                val id = FeatureSDK.getInt("id")
                println("FeatureSDK: Int Value (id) -> $id")
            }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FeatureFlagSDKTheme {
        Greeting("Android")
    }
}