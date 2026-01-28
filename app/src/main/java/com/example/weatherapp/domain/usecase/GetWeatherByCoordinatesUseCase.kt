package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting weather data by geographic coordinates
 * Encapsulates business logic for location-based weather
 */
class GetWeatherByCoordinatesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    /**
     * Execute the use case
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @return Flow emitting Result with WeatherData
     */
    operator fun invoke(latitude: Double, longitude: Double): Flow<Result<WeatherData>> {
        // Validate coordinates
        require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90" }
        require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180" }
        
        return repository.getWeatherByCoordinates(latitude, longitude)
    }
}
