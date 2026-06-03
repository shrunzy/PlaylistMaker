package com.practicum.playlistmaker.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding

class TrackAdapter(
    private val tracks: MutableList<Track>,
    private val listener: OnTrackClickListener? = null
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

    fun updateTracks(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}