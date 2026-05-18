package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View                      // ← ОБЯЗАТЕЛЬНО!
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

    companion object {
        private const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter = TrackAdapter(mutableListOf(), this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = trackAdapter
        }

        setupListeners()
        observeUiState()

        binding.searchBack.setOnClickListener { finish() }

        binding.searchLayout.isEndIconVisible = false

        restoreSearchQuery(savedInstanceState)
    }

    private fun setupListeners() {
        binding.searchLayout.setEndIconOnClickListener {
            viewModel.clearQuery()
            binding.etSearch.text?.clear()
            hideKeyboard(binding.etSearch)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.etSearch.text?.toString()?.trim() ?: ""
                if (query.isNotEmpty()) {
                    viewModel.performSearch(query)
                }
                true
            } else false
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            val hasText = !text.isNullOrBlank()
            binding.searchLayout.isEndIconVisible = hasText
        }


        //binding.root.findViewById<View>(R.id.btnRetry)?.setOnClickListener {
        //    viewModel.refreshLastFailedSearch()
        //}
        binding.placeholderError.btnRetry.setOnClickListener {
            viewModel.refreshLastFailedSearch()
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
    }

    private fun showLoadingState() {
        hideAllViews()
        //binding.root.findViewById<View>(R.id.progressBar)?.visibility = View.VISIBLE
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
    }

    private fun restoreSearchQuery(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(KEY_SEARCH_QUERY)?.let { query ->
            if (query.isNotEmpty()) {
                binding.etSearch.setText(query)
                binding.etSearch.setSelection(query.length)
            }
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, binding.etSearch.text?.toString() ?: "")
    }

    override fun onTrackClick(track: Track) {
        Toast.makeText(this, "Выбран: ${track.trackName} — ${track.artistName}", Toast.LENGTH_SHORT)
            .show()
    }
}