package com.sha.myexoplayer

import android.app.Application
import androidx.work.Configuration
import com.rudderstack.android.sdk.core.RudderClient
import com.rudderstack.android.sdk.core.RudderConfig
import com.rudderstack.android.sdk.core.RudderLogger
import java.util.concurrent.TimeUnit

class MyApp : Application(), Configuration.Provider {
    companion object {
        var rudderClient: RudderClient? = null
        const val TAG = "MainApplication"
        const val DATA_PLANE_URL = "https://rudderstack-data-plane.rayn-data.com"
        const val WRITE_KEY = "2bUh6wZkHy64ptGhRnyVuBUI41s"
        const val CONTROL_PLANE_URL = "https://rudderstack-control-plane-config-server.rayn-data.com/android/2bUh6wZkHy64ptGhRnyVuBUI41s"
    }

    override fun onCreate() {
        super.onCreate()

        RudderClient.putAuthToken("testAuthToken");
        rudderClient = RudderClient.getInstance(
            this,
            WRITE_KEY,
            RudderConfig.Builder()
                .withDataPlaneUrl(DATA_PLANE_URL)
                .withControlPlaneUrl(CONTROL_PLANE_URL)
                .withLogLevel(RudderLogger.RudderLogLevel.DEBUG)
                .withFlushPeriodically(20, TimeUnit.MINUTES)
                .withCollectDeviceId(false)
               /* .withFactory(BrazeIntegrationFactory.FACTORY)
                .withFactory(AmplitudeIntegrationFactory.FACTORY)*/
                .withTrackLifecycleEvents(true)
                .withNewLifecycleEvents(true)
                .withRecordScreenViews(true)
                .build()
        )

    }


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}