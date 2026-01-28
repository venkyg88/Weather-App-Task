package com.example.weatherapp.data.repository

import com.example.weatherapp.data.remote.api.WeatherApiService
import com.example.weatherapp.data.remote.dto.*
import com.example.weatherapp.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

/**
 * Unit tests for WeatherRepositoryImpl
 * Testing data layer with MockK
 */
class WeatherRepositoryImplTest {
    
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var apiService: WeatherApiService
    
    @Before
    fun setup() {
        apiService = mockk()
        repository = WeatherRepositoryImpl(apiService)
    }
    
    @Test
    fun `getWeatherByCity emits loading then success`() = runTest {
        // Given
        val cityName = "New York"
        val mockResponse = createMockWeatherResponse()
        
        coEvery {
            apiService.getWeatherByCity(
                cityName = any(),
                apiKey = any(),
                units = any()
            )
        } returns mockResponse
        
        // When
        val results = repository.getWeatherByCity(cityName).toList()
        
        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        
        val successResult = results[1] as Result.Success
        assertEquals("New York", successResult.data.cityName)
    }
    
    @Test
    fun `getWeatherByCity emits error on network failure`() = runTest {
        // Given
        val cityName = "InvalidCity"
        
        coEvery {
            apiService.getWeatherByCity(
                cityName = any(),
                apiKey = any(),
                units = any()
            )
        } throws IOException("Network error")
        
        // When
        val results = repository.getWeatherByCity(cityName).toList()
        
        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Error)
        
        val errorResult = results[1] as Result.Error
        assertTrue(errorResult.message?.contains("internet") ?: false)
    }
    
    @Test
    fun `getWeatherByCity emits error on HTTP 404`() = runTest {
        // Given
        val cityName = "NonExistentCity"
        val httpException = mockk<HttpException>()
        
        coEvery { httpException.code() } returns 404
        coEvery { httpException.message() } returns "Not Found"
        
        coEvery {
            apiService.getWeatherByCity(
                cityName = any(),
                apiKey = any(),
                units = any()
            )
        } throws httpException
        
        // When
        val results = repository.getWeatherByCity(cityName).toList()
        
        // Then
        assertTrue(results[1] is Result.Error)
        val errorResult = results[1] as Result.Error
        assertTrue(errorResult.message?.contains("not found") ?: false)
    }
    
    @Test
    fun `getWeatherByCoordinates emits loading then success`() = runTest {
        // Given
        val lat = 40.7128
        val lon = -74.0060
        val mockResponse = createMockWeatherResponse()
        
        coEvery {
            apiService.getWeatherByCoordinates(
                lat = any(),
                lon = any(),
                apiKey = any(),
                units = any()
            )
        } returns mockResponse
        
        // When
        val results = repository.getWeatherByCoordinates(lat, lon).toList()
        
        // Then
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
    }
    
    private fun createMockWeatherResponse() = WeatherResponse(
        coord = Coord(lon = -74.0060, lat = 40.7128),
        weather = listOf(
            Weather(
                id = 800,
                main = "Clear",
                description = "clear sky",
                icon = "01d"
            )
        ),
        base = "stations",
        main = Main(
            temp = 72.5,
            feelsLike = 70.0,
            tempMin = 68.0,
            tempMax = 75.0,
            pressure = 1013,
            humidity = 60,
            seaLevel = null,
            grndLevel = null
        ),
        visibility = 10000,
        wind = Wind(speed = 10.5, deg = 180, gust = null),
        clouds = Clouds(all = 0),
        dt = System.currentTimeMillis() / 1000,
        sys = Sys(
            type = 1,
            id = 1234,
            country = "US",
            sunrise = 1234567890,
            sunset = 1234598890
        ),
        timezone = -18000,
        id = 5128581,
        name = "New York",
        cod = 200
    )
}
