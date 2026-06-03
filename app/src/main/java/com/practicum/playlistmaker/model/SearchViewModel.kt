package com.practicum.playlistmaker.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val apiService: iTunesService) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    var lastFailedQuery: String? = null

    fun performSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            lastFailedQuery = query // Фиксируем запрос перед попыткой
            try {
                val response = apiService.searchSongs("song", query)

                if (response.isSuccessful && response.body() != null) {
                    val results = response.body()!!.results?.filterNotNull() ?: emptyList()
                    Log.d("API_RESPONSE", "Запрос: '$query' | Найдено треков: ${results.size}")

                    results.forEachIndexed { index, track ->
                        Log.d(
                            "API_TRACK_$index", """
                            Название: ${track.trackName}
                            Исполнитель: ${track.artistName}
                            trackTimeMillis: ${track.trackTimeMillis}
                            Artwork: ${track.artworkUrl100}
                        """.trimIndent()
                        )
                    }
                    if (results.isEmpty()) {
                        _uiState.value =
                            SearchUiState.EmptyResult(query) // Плейсхолдер: Нет результатов
                    } else {
                        _uiState.value = SearchUiState.Success(results) // Список треков
                    }
                } else {
                    // Ошибка HTTP (4xx, 5xx) -> Error State с кнопкой "Обновить"
                    val errorMsg = response.errorBody()?.string() ?: "Неизвестная ошибка сервера."
                    _uiState.value =
                        SearchUiState.Error(query, "HTTP ${response.code()}: $errorMsg")
                }
            } catch (e: Exception) {
                // Ошибка сети/парсер -> Error State с кнопкой "Обновить"
                _uiState.value =
                    SearchUiState.Error(query, "Ошибка соединения или парсинга данных.")
            }
        }
    }

    fun refreshLastFailedSearch() {
        lastFailedQuery?.let { query ->
            performSearch(query) // Повторяем последний неудавшийся запрос
        }
    }

    fun clearQuery() {
        // Сброс состояния и очистка запроса
        _uiState.value = SearchUiState.Idle
        lastFailedQuery = null
    }
}
