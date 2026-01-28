package com.example.weatherapp.data.repository

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.mapper.toWeatherData
import com.example.weatherapp.data.remote.api.WeatherApiService
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of WeatherRepository
 * Handles data fetching from remote API with error handling
 */
class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRepository {
    
    private val apiKey = BuildConfig.WEATHER_API_KEY
    
    override fun getWeatherByCity(cityName: String): Flow<Result<WeatherData>> = flow {
        try {
            emit(Result.Loading)
            
            // Add US country code for better search results since requirement specifies US cities
            val searchQuery = if (!cityName.contains(",")) {
                "$cityName,US"
            } else {
                cityName
            }
            
            val response = apiService.getWeatherByCity(
                cityName = searchQuery,
                apiKey = apiKey
            )
            
            val weatherData = response.toWeatherData()
            emit(Result.Success(weatherData))
            
        } catch (e: HttpException) {
            // Handle HTTP errors (4xx, 5xx)
            val errorMessage = when (e.code()) {
                404 -> "City not found. Please check the city name."
                401 -> "API authentication failed."
                429 -> "Too many requests. Please try again later."
                else -> "Network error: ${e.message()}"
            }
            emit(Result.Error(e, errorMessage))
            
        } catch (e: IOException) {
            // Handle network errors
            emit(Result.Error(e, "No internet connection. Please check your network."))
            
        } catch (e: Exception) {
            // Handle other unexpected errors
            emit(Result.Error(e, "An unexpected error occurred: ${e.message}"))
        }
    }
    
    override fun getWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Flow<Result<WeatherData>> = flow {
        try {
            emit(Result.Loading)
            
            val response = apiService.getWeatherByCoordinates(
                lat = latitude,
                lon = longitude,
                apiKey = apiKey
            )
            
            val weatherData = response.toWeatherData()
            emit(Result.Success(weatherData))
            
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                404 -> "Location not found."
                401 -> "API authentication failed."
                429 -> "Too many requests. Please try again later."
                else -> "Network error: ${e.message()}"
            }
            emit(Result.Error(e, errorMessage))
            
        } catch (e: IOException) {
            emit(Result.Error(e, "No internet connection. Please check your network."))
            
        } catch (e: Exception) {
            emit(Result.Error(e, "An unexpected error occurred: ${e.message}"))
        }
    }
}
