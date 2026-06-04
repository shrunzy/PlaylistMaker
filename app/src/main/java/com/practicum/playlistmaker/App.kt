package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false
        private set

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val systemDarkTheme = isSystemDarkThemeEnabled()
        darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, systemDarkTheme)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun isSystemDarkThemeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val PREFERENCES = "playlist_maker_preferences"
        private const val DARK_THEME_KEY = "dark_theme"
    }
}
