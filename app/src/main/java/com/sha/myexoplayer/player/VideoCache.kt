 package com.sha.myexoplayer.player

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

 @OptIn(UnstableApi::class)
object VideoCache {
    private var sDownloadCache: SimpleCache? = null
    private const val maxCacheSize: Long = 100 * 1024 * 1024

    fun getInstance(file: File, standaloneDatabaseProvider: StandaloneDatabaseProvider ): SimpleCache {
        val evictor = LeastRecentlyUsedCacheEvictor(maxCacheSize)
        if (sDownloadCache == null) sDownloadCache =
            SimpleCache(file, evictor, standaloneDatabaseProvider)
        return sDownloadCache as SimpleCache
    }
}
