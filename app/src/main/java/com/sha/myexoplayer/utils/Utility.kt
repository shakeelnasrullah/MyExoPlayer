package com.sha.myexoplayer.utils

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import com.sha.myexoplayer.player.Movies
import com.sha.myexoplayer.player.VideoCache
import java.io.File

class Utility {
    companion object {
        fun dummyData(): List<Movies> {
            return arrayListOf<Movies>(
                Movies(
                    "Shop",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
                ),
                Movies(
                    "Khoob",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
                ),
                Movies(
                    "Doodh",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
                ),
                Movies(
                    "Bob",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                ),
                Movies(
                    "Chop",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                ),
                Movies(
                    "Shop",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
                ),
                Movies(
                    "Khoob",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
                ),
                Movies(
                    "Doodh",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
                ),
                Movies(
                    "Bob",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
            )
        }

        @androidx.annotation.OptIn(UnstableApi::class)
        fun createCache(context: Context): CacheDataSource.Factory {
            val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
            val downloadContentDirectory =
                File(context.getExternalFilesDir(null), DOWNLOAD_CONTENT_DIRECTORY)


            val downloadCache =
                VideoCache.getInstance(downloadContentDirectory, StandaloneDatabaseProvider(context))
            val cacheSink = CacheDataSink.Factory().setCache(downloadCache)
            val upstreamFactory = DefaultDataSource.Factory(context, DefaultHttpDataSource.Factory())
            val downStreamFactory = FileDataSource.Factory()
            return CacheDataSource.Factory()
                .setCache(downloadCache)
                .setCacheWriteDataSinkFactory(cacheSink)
                .setCacheReadDataSourceFactory(downStreamFactory)
                .setUpstreamDataSourceFactory(upstreamFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }

    }
}