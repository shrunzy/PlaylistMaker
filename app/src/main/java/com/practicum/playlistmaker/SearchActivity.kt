package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.data.TrackRepository
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.model.OnTrackClickListener
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.model.TrackAdapter

class SearchActivity : AppCompatActivity(), OnTrackClickListener {

    private var searchQuery: String = ""

    private lateinit var binding: ActivitySearchBinding

    companion object {
        private const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
    }

    private val originalTracks = ArrayList(TrackRepository.getTracks())
    private val tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //trackAdapter = TrackAdapter(tracks)
        trackAdapter = TrackAdapter(tracks, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trackAdapter

        loadAllTracks()

        // Кнопка назад
        binding.searchBack.setOnClickListener {
            finish()
        }

        // Очистка поля
        binding.searchLayout.setEndIconOnClickListener {
            binding.etSearch.text?.clear()
            //binding.searchLayout.isEndIconVisible = false
            //loadAllTracks()           // показываем все треки
            hideKeyboard(binding.etSearch)
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            searchQuery = text?.toString() ?: ""

            val hasText = searchQuery.isNotEmpty()
            binding.searchLayout.isEndIconVisible = hasText

            if (hasText) {
                filterTracks(searchQuery)
            } else {
                loadAllTracks()
            }
        }

    }

    private fun loadAllTracks() {
        tracks.clear()
        tracks.addAll(originalTracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun filterTracks(query: String) {
        val filteredList = originalTracks.filter {
            it.trackName.contains(query, ignoreCase = true) ||
                    it.artistName.contains(query, ignoreCase = true)
        }

        tracks.clear()
        tracks.addAll(filteredList)
        trackAdapter.notifyDataSetChanged()
    }

    private fun hideKeyboard(view: android.view.View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val restoredText = savedInstanceState.getString(KEY_SEARCH_QUERY, "")

        if (restoredText.isNotEmpty()) {
            //val editText = findViewById<TextInputEditText>(R.id.et_search)
            binding.etSearch.setText(restoredText)
            //editText.setText(restoredText+" restored_text")

            // Ставим курсор в конец текста
            binding.etSearch.setSelection(restoredText.length)
        }
    }

    override fun onTrackClick(track: Track) {
        //Toast.makeText(this, "Клик по треку!", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "Выбран: ${track.trackName} - ${track.artistName}", Toast.LENGTH_SHORT)
            .show()


        // val intent = Intent(this, PlayerActivity::class.java)
        // intent.putExtra("TRACK", track)
        // startActivity(intent)
    }

}
