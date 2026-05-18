package com.practicum.playlistmaker.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesService {
    @GET("search")
    suspend fun searchSongs(
        @Query("entity") entity: String = "song",
        @Query("term") term: String
    ): Response<SearchResponse>
}
