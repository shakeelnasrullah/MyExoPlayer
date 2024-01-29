package com.sha.myexoplayer.player

import android.util.Log
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.sha.myexoplayer.utils.PlayerManager
import com.sha.myexoplayer.utils.Utility
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged


private const val TAG: String = "PlayerScreen"

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyPlayerScreen(modifier: Modifier) {
    // Screen Content
    val context = LocalContext.current
    //val videoList = Utility.dummyData()
    val videoList = Utility.dummyMoviesList()
    val playerManager = PlayerManager(context, videoList)


    var currentPage by remember {
        mutableIntStateOf(0)
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        videoList.size
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
            currentPage = page
            //pagerState.animateScrollToPage(page)
            println("Fahad::pageSelected$currentPage")
        }
    }

    Box(modifier = modifier) {
        VerticalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            //createChild(Modifier.fillMaxSize(), page, playerManager, currentPage == page)
            createChild(Modifier.fillMaxSize(), page, playerManager, pagerState.currentPage == page)
        }
    }
}

@Composable
fun createChild(
    modifier: Modifier,
    currentPage: Int,
    playerManager: PlayerManager,
    shouldPlay: Boolean
) {
    if (shouldPlay) {
        createMyPlayer(modifier, currentPage, playerManager)
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun createMyPlayer(
    modifier: Modifier,
    currentPage: Int,
    playerManager: PlayerManager,
) {
    println("Fahad::createMyPlayer$currentPage::true")


    val exoPlayer = playerManager.getPlayer(currentPage)

    playerManager.playCurrentItem(currentPage)

    Box(modifier = modifier) {


        val playerView = rememberPlayerView(exoPlayer!!)
        Player(
            playerView = playerView,
            aspectRatio = 1f
        )
    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Player(
    playerView: PlayerView,
    modifier: Modifier = Modifier,
    aspectRatio: Float
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        //viewModel.onTappedScreen()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .align(Alignment.Center)
        )
        ComposableLifecycle { _, event ->
            when (event) {

                Lifecycle.Event.ON_START -> {
                    playerView.player!!.play()
                    Log.d(TAG, "On Start")
                }
                Lifecycle.Event.ON_STOP -> {
                    playerView.player!!.pause()
                    Log.d(TAG, "On Stop")
                }

                else -> {}
            }
        }
    }
}


@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun rememberPlayerView(player: Player): PlayerView {
    val context = LocalContext.current
    val playerView = remember {
        PlayerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            useController = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

            //setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
            this.player = player
        }
    }
    DisposableEffect(key1 = player) {
        playerView.player = player
        onDispose {
            playerView.player = null
        }
    }
    return playerView
}
@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

