package com.practicum.playlistmaker.model

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId: Long? = null,
    val trackName: String? = null,
    val artistName: String? = null,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long? = null,
    val artworkUrl100: String? = null,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null
) {
    fun getCoverArtwork(): String? =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseYear(): String? =
        releaseDate?.takeIf { it.length >= 4 }?.take(4)
}
