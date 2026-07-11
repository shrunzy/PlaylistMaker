package com.practicum.playlistmaker.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val country: String? = null,
    val previewUrl: String? = null
) : Parcelable {
    fun getCoverArtwork(): String? =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun getReleaseYear(): String? =
        releaseDate?.takeIf { it.length >= 4 }?.take(4)
}
