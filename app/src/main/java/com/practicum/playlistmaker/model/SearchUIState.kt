package com.practicum.playlistmaker.model

sealed class SearchUiState {
    object Idle : SearchUiState() // Начальное состояние
    object Loading : SearchUiState() // Запрос выполняется
    data class Success(val tracks: List<Track>) : SearchUiState() // Успешный список результатов
    data class EmptyResult(val query: String) :
        SearchUiState() // Результат пуст (успех, но 0 треков)

    data class Error(val query: String, val message: String) :
        SearchUiState() // Ошибка сервера/сети
}
