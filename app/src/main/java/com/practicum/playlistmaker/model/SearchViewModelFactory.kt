package com.practicum.playlistmaker.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SearchViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(RetrofitInstance.apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}