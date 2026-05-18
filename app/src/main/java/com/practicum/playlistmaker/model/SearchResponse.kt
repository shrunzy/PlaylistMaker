package com.practicum.playlistmaker.model

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>? = null
)