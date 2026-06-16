package com.practicum.playlistmaker.model

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson = Gson()
) {

    fun getTracks(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return arrayListOf()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type) ?: arrayListOf()
    }

    fun addTrack(track: Track) {
        val tracks = getTracks()
        tracks.removeAll { savedTrack ->
            if (track.trackId != null) {
                savedTrack.trackId == track.trackId
            } else {
                savedTrack == track
            }
        }
        tracks.add(0, track)

        if (tracks.size > MAX_HISTORY_SIZE) {
            tracks.subList(MAX_HISTORY_SIZE, tracks.size).clear()
        }

        saveTracks(tracks)
    }

    fun clear() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    private fun saveTracks(tracks: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(tracks))
            .apply()
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }
}
