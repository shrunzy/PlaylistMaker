package com.practicum.playlistmaker.model

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val tracks: List<Track>) : SearchUiState()
    data class EmptyResult(val query: String) : SearchUiState()

    data class Error(val query: String, val message: String) : SearchUiState()
}
