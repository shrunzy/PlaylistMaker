package com.practicum.playlistmaker.model

import com.google.gson.annotations.SerializedName
import kotlin.concurrent.atomics.atomicArrayOfNulls

data class Track(
    val trackName: String? = null,
    val artistName: String? = null,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long? = null,
    val artworkUrl100: String? = null
)