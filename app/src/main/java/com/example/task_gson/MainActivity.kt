package com.example.task_gson

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.task_gson.ui.theme.TaskGsonTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskGsonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val jsonResponse = """
        {
            "site": "example.com",
            "notificationType": "USER_DEVICE_REMOVED",
            "userMeta": {
                "id": "user123"
            },
            "payload": "data payload json in string"
        }
    """
                    val gson = Gson()
                    val dataResponse = gson.fromJson(jsonResponse, DataResponse::class.java)

                    val payloadString = dataResponse.payload

                    val deviceMatchState = remember { mutableStateOf(String()) }

                    val context = LocalContext.current
                    matchDeviceIds(payloadString, getDeviceId(context)) { text ->
                        Log.i("aryan", text)
                        deviceMatchState.value = text
                    }

                    if (deviceMatchState.value.isNotBlank())
                        Greeting(deviceMatchState.value)
                }
            }
        }
    }

    private fun matchDeviceIds(
        payloadString: String,
        deviceId: String?,
        isMatching: (String) -> Unit
    ) {
        if (payloadString == deviceId) {
            isMatching("Device Id matched!")
        } else {
            isMatching("Device Id didn't match!")
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

}

data class DataResponse(
    val site: String,
    val notificationType: String,
    val userMeta: UserMeta,
    val payload: String
)

data class UserMeta(val id: String)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskGsonTheme {
        Greeting("Android")
    }
}