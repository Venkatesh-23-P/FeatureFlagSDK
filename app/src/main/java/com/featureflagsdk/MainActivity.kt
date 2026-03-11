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

        FeatureSDK.initialize(
            context = this,
            apiUrl = "https://unmeteorologic-subsumable-cami.ngrok-free.dev/api/hospitals/list?pageNumber=1&latitude=13.0893502&longitude=80.241408&searchName=&pageSize=20&userId=876305c4-57b5-4886-b303-b07bf6bbbbe7"
        )

        val value = FeatureSDK.getBool("success")

        println("Feature value: $value")
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