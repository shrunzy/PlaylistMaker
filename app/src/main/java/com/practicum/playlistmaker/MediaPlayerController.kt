package com.practicum.playlistmaker

import android.media.MediaPlayer
import java.io.IOException

class MediaPlayerController : PlayerController {

    private val mediaPlayer = MediaPlayer()

    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    override fun prepare(
        previewUrl: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit
    ) {
        if (previewUrl.isNullOrBlank()) {
            onError()
            return
        }

        try {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.setOnPreparedListener {
                onPrepared()
            }
            mediaPlayer.setOnCompletionListener {
                onCompletion()
            }
            mediaPlayer.setOnErrorListener { _, _, _ ->
                onError()
                true
            }
            mediaPlayer.prepareAsync()
        } catch (_: IOException) {
            onError()
        } catch (_: IllegalArgumentException) {
            onError()
        } catch (_: IllegalStateException) {
            onError()
        }
    }

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun seekToStart() {
        mediaPlayer.seekTo(0)
    }

    override fun release() {
        mediaPlayer.release()
    }
}