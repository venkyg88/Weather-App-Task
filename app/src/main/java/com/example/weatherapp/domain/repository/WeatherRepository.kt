package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining weather data operations
 * Following Clean Architecture principles - domain layer doesn't depend on data layer
 */
interface WeatherRepository {
    
    /**
     * Get weather data for a specific city
     * @param cityName Name of the city to search
     * @return Flow emitting Result with WeatherData
     */
    fun getWeatherByCity(cityName: String): Flow<Result<WeatherData>>
    
    /**
     * Get weather data by geographic coordinates
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @return Flow emitting Result with WeatherData
     */
    fun getWeatherByCoordinates(latitude: Double, longitude: Double): Flow<Result<WeatherData>>
}
