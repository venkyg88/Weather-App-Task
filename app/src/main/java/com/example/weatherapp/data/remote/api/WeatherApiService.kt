package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API service for OpenWeatherMap API
 */
interface WeatherApiService {
    
    /**
     * Get weather by city name
     * @param cityName Name of the city
     * @param apiKey API key for authentication
     * @param units Units of measurement (metric, imperial, standard)
     * @return Weather data for the specified city
     */
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial" // Using imperial for US cities (Fahrenheit)
    ): WeatherResponse
    
    /**
     * Get weather by geographic coordinates
     * @param lat Latitude
     * @param lon Longitude
     * @param apiKey API key for authentication
     * @param units Units of measurement (metric, imperial, standard)
     * @return Weather data for the specified coordinates
     */
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial"
    ): WeatherResponse
}
