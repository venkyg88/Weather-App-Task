package com.example.weatherapp.data.mapper

import com.example.weatherapp.data.remote.dto.WeatherResponse
import com.example.weatherapp.domain.model.WeatherData

/**
 * Mapper to convert API DTOs to Domain models
 * Following Clean Architecture - separating data layer concerns from domain layer
 */
fun WeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        cityName = this.name ?: "Unknown",
        country = this.sys?.country ?: "",
        temperature = this.main?.temp ?: 0.0,
        feelsLike = this.main?.feelsLike ?: 0.0,
        tempMin = this.main?.tempMin ?: 0.0,
        tempMax = this.main?.tempMax ?: 0.0,
        humidity = this.main?.humidity ?: 0,
        pressure = this.main?.pressure ?: 0,
        windSpeed = this.wind?.speed ?: 0.0,
        weatherDescription = this.weather?.firstOrNull()?.description ?: "No description",
        weatherMain = this.weather?.firstOrNull()?.main ?: "Unknown",
        weatherIcon = this.weather?.firstOrNull()?.icon ?: "01d",
        latitude = this.coord?.lat ?: 0.0,
        longitude = this.coord?.lon ?: 0.0,
        timestamp = this.dt ?: System.currentTimeMillis() / 1000
    )
}
