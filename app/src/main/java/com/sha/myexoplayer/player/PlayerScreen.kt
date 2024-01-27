package com.sha.myexoplayer.player

import android.content.Intent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.sha.myexoplayer.utils.Constants
import com.sha.myexoplayer.utils.Utility
import kotlinx.coroutines.flow.distinctUntilChanged


private const val TAG: String = "PlayerScreen"

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(modifier: Modifier) {
    // Screen Content
    val context = LocalContext.current
    val videoList = Utility.dummyData()


    // Starting service to download only 100KB data of each item
  /*  val preloadingServiceIntent = Intent(context, VideoPreLoadingService::class.java)
    preloadingServiceIntent.putParcelableArrayListExtra(
        Constants.VIDEO_LIST,
        videoList as ArrayList<Movies>
    )
    context.startService(preloadingServiceIntent)*/

    var currentPage by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState(pageCount = {
        videoList.size
    })


    Box(modifier = Modifier.fillMaxSize()) {

        LaunchedEffect(key1 = pagerState) {
            snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
                pagerState.animateScrollToPage(page)
                currentPage = page
            }
        }

        VerticalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            createPlayer(modifier = Modifier.fillMaxSize(), videoList, page, currentPage == pagerState.currentPage)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun createPlayer(
    modifier: Modifier,
    videoList: List<Movies>,
    currentPage: Int,
    startPlayer: Boolean,
    ) {

    val context = LocalContext.current

    val currentMediaItem = MediaItem.fromUri(videoList[currentPage].url)
    val cacheDataSourceFactory = Utility.createCache(context)
    val currentMediaSource =
        DefaultMediaSourceFactory(cacheDataSourceFactory).createMediaSource(currentMediaItem)

    val exoPlayer = ExoPlayer.Builder(context).build()

    exoPlayer.setMediaSource(currentMediaSource)
    exoPlayer.prepare()
    if(startPlayer) {
        exoPlayer.play()
    }

    Box(modifier = modifier) {
        DisposableEffect(key1 = Unit) {
            onDispose {
                exoPlayer.release()
            }
        }
        AndroidView(factory = {
            PlayerView(context).apply {
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        })
    }
}

