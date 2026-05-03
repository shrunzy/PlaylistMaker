package com.practicum.playlistmaker.model

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding

class TrackViewHolder(
    private val binding: ItemTrackBinding,
    private val listener: OnTrackClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    private var currentTrack: Track? = null

    init {
        itemView.setOnClickListener {
            currentTrack?.let { track ->
                listener?.onTrackClick(track)
            }
        }
    }

    fun bind(track: Track) {

        currentTrack = track

        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = track.trackTime

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_track_placeholder)
            .error(R.drawable.ic_track_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.track_corner_radius))
            )
            .into(binding.ivArtwork)
    }
}
