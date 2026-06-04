package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager

import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.model.OnTrackClickListener
import com.practicum.playlistmaker.model.SearchHistory
import com.practicum.playlistmaker.model.SearchUiState
import com.practicum.playlistmaker.model.SearchViewModel
import com.practicum.playlistmaker.model.SearchViewModelFactory
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.model.TrackAdapter

class SearchActivity : AppCompatActivity(), OnTrackClickListener {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory()
    }
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory

    companion object {
        private const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
        private const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsInsetsPadding()

        searchHistory = SearchHistory(
            getSharedPreferences(SEARCH_HISTORY_PREFERENCES, Context.MODE_PRIVATE)
        )

        trackAdapter = TrackAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        historyAdapter = TrackAdapter(mutableListOf(), this)
        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = historyAdapter
        }

        setupListeners()
        observeUiState()

        binding.searchBack.setOnClickListener { finish() }

        binding.searchLayout.isEndIconVisible = false

        restoreSearchQuery(savedInstanceState)
        updateSearchHistoryVisibility()
    }

    private fun setupListeners() {
        binding.searchLayout.setEndIconOnClickListener {
            viewModel.clearQuery()
            binding.etSearch.text?.clear()
            updateSearchHistoryVisibility()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.etSearch.text?.toString()?.trim() ?: ""
                if (query.isNotEmpty()) {
                    binding.searchHistoryContainer.visibility = View.GONE
                    viewModel.performSearch(query)
                }
                true
            } else false
        }

        binding.etSearch.setOnFocusChangeListener { _, _ ->
            updateSearchHistoryVisibility()
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            val hasText = !text.isNullOrBlank()
            binding.searchLayout.isEndIconVisible = hasText
            if (hasText) {
                binding.searchHistoryContainer.visibility = View.GONE
            } else {
                viewModel.clearQuery()
                updateSearchHistoryVisibility()
            }
        }

        binding.placeholderError.btnRetry.setOnClickListener {
            viewModel.refreshLastFailedSearch()
        }

        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clear()
            historyAdapter.updateTracks(emptyList())
            binding.searchHistoryContainer.visibility = View.GONE
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is SearchUiState.Idle -> showIdleState()
                        is SearchUiState.Loading -> showLoadingState()
                        is SearchUiState.Success -> showSuccessState(state.tracks)
                        is SearchUiState.EmptyResult -> showEmptyState()
                        is SearchUiState.Error -> showErrorState()
                    }
                }
            }
        }
    }


    private fun showIdleState() {
        hideAllViews()
        updateSearchHistoryVisibility()
    }

    private fun showLoadingState() {
        hideAllViews()
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showSuccessState(tracks: List<Track>) {
        hideAllViews()
        trackAdapter.updateTracks(tracks)
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        hideAllViews()
        binding.placeholderEmpty.root.visibility = View.VISIBLE
    }

    private fun showErrorState() {
        hideAllViews()
        binding.placeholderError.root.visibility = View.VISIBLE
    }

    private fun hideAllViews() {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderEmpty.root.visibility = View.GONE
        binding.placeholderError.root.visibility = View.GONE
        binding.searchHistoryContainer.visibility = View.GONE
    }

    private fun updateSearchHistoryVisibility() {
        val historyTracks = searchHistory.getTracks()
        val shouldShowHistory = binding.etSearch.hasFocus() &&
                binding.etSearch.text.isNullOrEmpty() &&
                historyTracks.isNotEmpty()

        if (shouldShowHistory) {
            binding.recyclerView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.placeholderEmpty.root.visibility = View.GONE
            binding.placeholderError.root.visibility = View.GONE
            historyAdapter.updateTracks(historyTracks)
            binding.searchHistoryContainer.visibility = View.VISIBLE
        } else {
            binding.searchHistoryContainer.visibility = View.GONE
        }
    }

    private fun restoreSearchQuery(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(KEY_SEARCH_QUERY)?.let { query ->
            if (query.isNotEmpty()) {
                binding.etSearch.setText(query)
                binding.etSearch.setSelection(query.length)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, binding.etSearch.text?.toString() ?: "")
    }

    override fun onTrackClick(track: Track) {
        searchHistory.addTrack(track)
        updateSearchHistoryVisibility()
        Toast.makeText(this, "Выбран: ${track.trackName} — ${track.artistName}", Toast.LENGTH_SHORT)
            .show()
    }
}
