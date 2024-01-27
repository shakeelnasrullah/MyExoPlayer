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



        fun dummyMoviesList() : List<Movies>{
            return arrayListOf<Movies>(
                Movies("Min", "https:\\/\\/untamed-vod-v1-source71e471f1-o72av4ghlldb.s3.amazonaws.com\\/upload\\/videos\\/2024\\/0\\/624hhDbFUq3zfu3wWJb94XEjjYjm22PLvhNAhDkuTJx19aSiuLjKjtw_video."),
                Movies("Pa", "https:\\/\\/d12wboyscx16xj.cloudfront.net\\/4382715f-2ae0-4290-8d6e-b764a3739b3a\\/hls\\/ed5tpD8fZvOkfDwVVy6oB947J8Y7pjPeSaz2yOCL8fakjDzg7AVbag1_video.m3u8"),
                Movies("Pa", "https:\\/\\/d12wboyscx16xj.cloudfront.net\\/0768b318-c695-4b64-b081-ebd36b82e4a6\\/hls\\/7pFDWICLmYB8ltTq1Wvxm3rbSuYj7nDhiTKehuDM9sZdRkoazBPW31e_video.m3u8"),
                Movies("Pa", "https:\\/\\/d12wboyscx16xj.cloudfront.net\\/c1935209-96b8-4659-b309-abebd5fca57b\\/hls\\/yGjcw8w2ab38quBjuaCmsgmL54E9CMmrnQnblb5LuQrE5RR4dgJoeZS_video.m3u8"),
                Movies("Pa", "https:\\/\\/d12wboyscx16xj.cloudfront.net\\/87da6b20-94a4-4ae6-9fd5-406a58754eaa\\/hls\\/qWipo4h6DuzRSzDMRxTp_26_46363d585b8acfd32416dff9164b30d0_video.m3u8"),
                Movies("Pa", "https:\\/\\/d12wboyscx16xj.cloudfront.net\\/7a5576e3-6525-4c93-8503-b8e4b4c39dab\\/hls\\/r4UAHUlrfqJjk5msgiV8_26_e0d960853bc4d3bc77502483e5d2832e_video.m3u8")
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