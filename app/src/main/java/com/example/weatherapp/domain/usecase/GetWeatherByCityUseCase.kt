package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting weather data by city name
 * Encapsulates business logic for weather search
 */
class GetWeatherByCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    /**
     * Execute the use case
     * @param cityName Name of the city to search
     * @return Flow emitting Result with WeatherData
     */
    operator fun invoke(cityName: String): Flow<Result<WeatherData>> {
        // Validate input
        if (cityName.isBlank()) {
            throw IllegalArgumentException("City name cannot be empty")
        }
        
        return repository.getWeatherByCity(cityName.trim())
    }
}
