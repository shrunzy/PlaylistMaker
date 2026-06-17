package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.IntentCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.model.formatDuration
import kotlin.math.roundToInt

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applySystemBarsInsetsPadding()

        val track = IntentCompat.getParcelableExtra(intent, EXTRA_TRACK, Track::class.java)
            ?: run {
                finish()
                return
            }

        binding.playerBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        bindTrack(track)
        adjustVerticalMarginsToFit()
    }

    private fun bindTrack(track: Track) {
        binding.trackName.text = track.trackName.orEmpty()
        binding.artistName.text = track.artistName.orEmpty()
        binding.durationValue.text = formatDuration(track.trackTimeMillis)
        binding.genreValue.text = track.primaryGenreName.orEmpty()
        binding.countryValue.text = track.country.orEmpty()

        bindOptionalRow(
            track.collectionName,
            binding.albumGroup,
            binding.albumValue
        )
        bindOptionalRow(
            track.getReleaseYear(),
            binding.yearGroup,
            binding.yearValue
        )

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_track_placeholder_big)
            .error(R.drawable.ic_track_placeholder_big)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius))
            )
            .into(binding.coverArtwork)
    }

    private fun bindOptionalRow(value: String?, group: Group, valueView: TextView) {
        val isVisible = !value.isNullOrBlank()
        group.visibility = if (isVisible) View.VISIBLE else View.GONE
        valueView.text = value.orEmpty()
    }

    private fun adjustVerticalMarginsToFit() {
        binding.root.doOnLayout { scrollView ->
            val availableHeight = scrollView.height - scrollView.paddingTop - scrollView.paddingBottom
            val marginSpecs = listOf(
                binding.coverArtwork to 24,
                binding.trackName to 24,
                binding.artistName to 12,
                binding.playbackControlArea to 30,
                binding.playbackProgress to 4,
                binding.durationLabel to 28,
                binding.albumLabel to 16,
                binding.yearLabel to 16,
                binding.genreLabel to 16,
                binding.countryLabel to 16
            ).filter { (view, _) -> view.visibility != View.GONE }

            val fixedHeight = binding.playerBack.height +
                binding.coverArtwork.height +
                binding.trackName.visibleHeight() +
                binding.artistName.visibleHeight() +
                binding.playbackControlArea.height +
                binding.playbackProgress.visibleHeight() +
                binding.durationLabel.visibleHeight() +
                binding.albumLabel.visibleHeight() +
                binding.yearLabel.visibleHeight() +
                binding.genreLabel.visibleHeight() +
                binding.countryLabel.visibleHeight()

            val baseMarginsHeight = marginSpecs.sumOf { (_, marginDp) -> marginDp.dpToPx() }
            val scale = if (baseMarginsHeight > 0) {
                ((availableHeight - fixedHeight).toFloat() / baseMarginsHeight)
                    .coerceIn(0f, 1f)
            } else {
                1f
            }

            marginSpecs.forEach { (view, marginDp) ->
                view.updateTopMargin(marginDp, scale)
            }
        }
    }

    private fun View.updateTopMargin(baseMargin: Int, scale: Float) {
        updateLayoutParams<ConstraintLayout.LayoutParams> {
            topMargin = (baseMargin.dpToPx() * scale).roundToInt()
        }
    }

    private fun View.visibleHeight(): Int {
        return if (visibility == View.GONE) 0 else height
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).roundToInt()
    }

    companion object {
        const val EXTRA_TRACK = "extra_track"
    }
}
