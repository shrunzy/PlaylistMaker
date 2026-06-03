package com.practicum.playlistmaker.model

import java.text.SimpleDateFormat
import java.util.*

fun formatDuration(millis: Long?): String {
    if (millis == null) return "00:00"
    val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
    return formatter.format(Date(millis))
}