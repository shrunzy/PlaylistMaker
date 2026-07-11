package com.practicum.playlistmaker

interface PlayerController {
    val currentPosition: Int
    val isPlaying: Boolean

    fun prepare(
        previewUrl: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit,
        onError: () -> Unit
    )

    fun play()
    fun pause()
    fun seekToStart()
    fun release()
}