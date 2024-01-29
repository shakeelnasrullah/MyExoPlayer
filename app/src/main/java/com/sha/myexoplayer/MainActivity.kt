package com.sha.myexoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger
import com.rudderstack.android.sdk.core.RudderProperty
import com.rudderstack.android.sdk.core.RudderTraits
import com.sha.myexoplayer.player.MyPlayerScreen
import com.sha.myexoplayer.ui.theme.MyExoPlayerTheme
import com.sha.myexoplayer.utils.PlayerManager
import java.util.Date

class MainActivity : ComponentActivity() {
    private var userCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyExoPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MyApp.rudderClient!!.identify(
                        "user$userCount",
                        RudderTraits().putEmail("user$userCount@gmail.com").putName("Mr. User$userCount"),
                        null
                    )

                   /* Button(onClick = {

                        MyApp.rudderClient!!.identify(
                            "user$userCount",
                            RudderTraits().putEmail("user$userCount@gmail.com").putName("Mr. User$userCount"),
                            null
                        )

                        val props = RudderProperty()
                        props.put("TestAndroidApp", "This is a test event ${Date().time}")
                        MyApp.rudderClient!!.track(
                            "ButtonTapped",
                            props,
                            null
                        )
                    }) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "HelloWorld", fontSize = 20.sp, modifier = Modifier.fillMaxSize()
                        )
                    }*/
                    MyPlayerScreen(modifier = Modifier.fillMaxSize())

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
