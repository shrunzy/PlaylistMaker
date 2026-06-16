package com.practicum.playlistmaker

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun AppCompatActivity.applySystemBarsInsetsPadding() {
    val content = findViewById<ViewGroup>(android.R.id.content)
    content.getChildAt(0)?.applySystemBarsInsetsPadding()
}

fun View.applySystemBarsInsetsPadding() {
    val initialLeft = paddingLeft
    val initialTop = paddingTop
    val initialRight = paddingRight
    val initialBottom = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            left = initialLeft + systemBars.left,
            top = initialTop + systemBars.top,
            right = initialRight + systemBars.right,
            bottom = initialBottom + systemBars.bottom
        )
        insets
    }

    ViewCompat.requestApplyInsets(this)
}
