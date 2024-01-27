package com.sha.myexoplayer.player

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheWriter
import androidx.media3.datasource.cache.SimpleCache
import com.sha.myexoplayer.utils.Constants
import com.sha.myexoplayer.utils.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class VideoPreLoadingService : IntentService(VideoPreLoadingService::class.java.simpleName) {
    private val TAG = "VideoPreLoadingService"
    val DOWNLOAD_CONTENT_DIRECTORY = "downloads"


    private lateinit var mContext: Context
    private var cachingJob: Job? = null
    private var videosList: ArrayList<Movies>? = null

    private lateinit var httpDataSourceFactory: HttpDataSource.Factory
    private lateinit var defaultDataSourceFactory: DefaultDataSourceFactory
    private lateinit var cacheDataSourceFactory: CacheDataSource
    private lateinit var simpleCache: SimpleCache

    @OptIn(UnstableApi::class)
    override fun onHandleIntent(intent: Intent?) {
        mContext = applicationContext

        httpDataSourceFactory = DefaultHttpDataSource.Factory()
            //.setAllowCrossProtocolRedirects(true)

        defaultDataSourceFactory = DefaultDataSourceFactory(
            this, httpDataSourceFactory
        )
        val downloadContentDirectory =
            File(mContext.getExternalFilesDir(null), DOWNLOAD_CONTENT_DIRECTORY)

        simpleCache =
            VideoCache.getInstance(downloadContentDirectory, StandaloneDatabaseProvider(mContext))

        cacheDataSourceFactory = Utility.createCache(mContext).createDataSource()

      /*  cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .createDataSource()*/



        if (intent != null) {
            val extras = intent.extras
            videosList = extras?.getParcelableArrayList(Constants.VIDEO_LIST)

            if (!videosList.isNullOrEmpty()) {
                preCacheVideo(videosList)
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun preCacheVideo(videosList: ArrayList<Movies>?) {
        var videoUrl: String? = null
        if (!videosList.isNullOrEmpty()) {
            videoUrl = videosList[0].url
            videosList.removeAt(0)
        } else {
            stopSelf()
        }
        if (!videoUrl.isNullOrBlank()) {
            val videoUri = Uri.parse(videoUrl)
            val dataSpec = DataSpec(videoUri, 0, 100 * 1024, null)

            val progressListener =
                CacheWriter.ProgressListener { requestLength, bytesCached, newBytesCached ->
                    val downloadPercentage: Double = (bytesCached * 100.0
                            / requestLength)

                    Log.d(TAG, "downloadPercentage $downloadPercentage videoUri: $videoUri")
                }

            cachingJob = GlobalScope.launch(Dispatchers.IO) {
                cacheVideo(dataSpec, progressListener)
                preCacheVideo(videosList)
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun cacheVideo(
        dataSpec: DataSpec,
        progressListener: CacheWriter.ProgressListener
    ) {
        runCatching {
            CacheWriter(
                cacheDataSourceFactory,
                dataSpec,
                null,
                progressListener
            ).cache()
        }.onFailure {
            it.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cachingJob?.cancel()
    }
}