package com.example.weatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for SharedPreferences operations
 * Handles storing and retrieving persistent data like last searched city
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    /**
     * Save the last searched city name
     */
    fun saveLastSearchedCity(cityName: String) {
        sharedPreferences.edit().apply {
            putString(KEY_LAST_CITY, cityName)
            apply()
        }
    }
    
    /**
     * Get the last searched city name
     * @return City name or null if not found
     */
    fun getLastSearchedCity(): String? {
        return sharedPreferences.getString(KEY_LAST_CITY, null)
    }
    
    /**
     * Clear all preferences
     */
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
    
    companion object {
        private const val PREFS_NAME = "weather_app_prefs"
        private const val KEY_LAST_CITY = "last_searched_city"
    }
}
