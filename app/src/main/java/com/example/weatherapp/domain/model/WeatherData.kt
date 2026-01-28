package com.example.weatherapp.domain.model

/**
 * Domain model for Weather data
 * Clean representation of weather information used throughout the app
 */
data class WeatherData(
    val cityName: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val weatherDescription: String,
    val weatherMain: String,
    val weatherIcon: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
) {
    /**
     * Get the full icon URL for weather icon
     */
    fun getIconUrl(): String {
        return "https://openweathermap.org/img/wn/$weatherIcon@2x.png"
    }
    
    /**
     * Format temperature with degree symbol
     */
    fun getFormattedTemperature(): String {
        return "${temperature.toInt()}°F"
    }
    
    /**
     * Format humidity as percentage
     */
    fun getFormattedHumidity(): String {
        return "$humidity%"
    }
    
    /**
     * Format wind speed
     */
    fun getFormattedWindSpeed(): String {
        return "${windSpeed.toInt()} mph"
    }
}
