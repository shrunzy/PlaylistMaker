package com.practicum.playlistmaker.model

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Long? = null,
    val trackName: String? = null,
    val artistName: String? = null,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long? = null,
    val artworkUrl100: String? = null
)
