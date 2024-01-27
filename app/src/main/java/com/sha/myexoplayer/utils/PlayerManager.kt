package com.sha.myexoplayer.utils

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.sha.myexoplayer.player.Movies

class PlayerData(var prepared: Boolean = false, var player: ExoPlayer? = null)
class PlayerManager( private val context: Context, private val videoUrls: List<Movies>, private val preCacheItem : Int = 2) {

    private val players: MutableList<PlayerData> = mutableListOf()

    init {
        initializePlayers()
    }

    private fun initializePlayers() {
        for (url in videoUrls) {
            players.add(PlayerData())
        }
    }

    private fun preparePlayers(index: Int) {
        var startIndex = index - preCacheItem
        if(startIndex < 0){
            startIndex = 0
        }
            /*when {
            index < 2 -> 0
            else -> index - 2
        }*/
        var endIndex = index + preCacheItem
        if (endIndex >= videoUrls.size){
            endIndex = videoUrls.size-1
        }
            /*when (index) {
            (videoUrls.size - 1), (videoUrls.size - 2) -> videoUrls.size - 1
            else -> index + 2
        }*/
        for (i in 0 until startIndex) {
            players[i].player?.let {
                players[i].player?.release()
                players[i].player = null
            }
            players[i].prepared = false
        }
        for (i in startIndex..endIndex) {
            if (players[i].prepared.not()) {
                players[i].player = ExoPlayer.Builder(context).build()
                players[i].player?.setMediaItem(MediaItem.fromUri(videoUrls[i].url))
                players[i].player?.prepare()
                players[i].prepared = true
            } else if (players[i].player?.isPlaying == true) {
                println("Fahad::Paused$i")
                players[i].player?.pause()
            }
        }
        for (i in endIndex + 1 until videoUrls.size) {
            players[i].player?.let {
                players[i].player?.release()
                players[i].player = null
            }
            players[i].prepared = false
        }
    }

    fun preparePlayer(index: Int) {
        if (index in 0 until players.size) {
            players[index].player?.prepare()
        }
    }

    fun playCurrentItem(index: Int) {
        preparePlayers(index)
        if (index in 0 until players.size) {
            players[index].player?.play()
        }
    }

    fun getPlayer(index: Int): Player? {
        return if (index in 0 until players.size) {
            if (players[index].player != null) {
                players[index].player
            } else {
                val player = ExoPlayer.Builder(context).build()
                player.setMediaItem(MediaItem.fromUri(videoUrls[index].url))
                player.prepare()
                players[index].player = player
                players[index].prepared = true
                player
            }
        } else {
            null
        }
    }

    fun pause(index: Int) {
        if (index in 0 until players.size) {
            players[index].player?.pause()
        }
    }

    fun releasePlayers() {
        for (player in players) {
            player.player?.release()
            player.prepared = false
        }
        players.clear()
    }
}
