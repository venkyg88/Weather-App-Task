package com.example.weatherapp.presentation.weather

import com.example.weatherapp.data.local.PreferencesManager
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.usecase.GetWeatherByCityUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByCoordinatesUseCase
import com.example.weatherapp.domain.util.Result
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for WeatherViewModel
 * Using MockK for mocking and Coroutines Test for testing suspend functions
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    
    private lateinit var viewModel: WeatherViewModel
    private lateinit var getWeatherByCityUseCase: GetWeatherByCityUseCase
    private lateinit var getWeatherByCoordinatesUseCase: GetWeatherByCoordinatesUseCase
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var locationManager: LocationManager
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Create mocks
        getWeatherByCityUseCase = mockk()
        getWeatherByCoordinatesUseCase = mockk()
        preferencesManager = mockk(relaxed = true)
        locationManager = mockk()
        
        // Mock initial preference call
        every { preferencesManager.getLastSearchedCity() } returns null
        
        viewModel = WeatherViewModel(
            getWeatherByCityUseCase,
            getWeatherByCoordinatesUseCase,
            preferencesManager,
            locationManager
        )
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `searchWeather emits loading then success state`() = runTest {
        // Given
        val cityName = "New York"
        val mockWeatherData = createMockWeatherData()
        
        every { getWeatherByCityUseCase(cityName) } returns flow {
            emit(Result.Loading)
            emit(Result.Success(mockWeatherData))
        }
        
        // When
        viewModel.searchWeather(cityName)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Success)
        assertEquals(mockWeatherData, (state as WeatherUiState.Success).weatherData)
        
        // Verify city was saved
        verify { preferencesManager.saveLastSearchedCity(cityName) }
    }
    
    @Test
    fun `searchWeather emits error state on failure`() = runTest {
        // Given
        val cityName = "InvalidCity"
        val errorMessage = "City not found"
        
        every { getWeatherByCityUseCase(cityName) } returns flow {
            emit(Result.Loading)
            emit(Result.Error(Exception(), errorMessage))
        }
        
        // When
        viewModel.searchWeather(cityName)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state is WeatherUiState.Error)
        assertEquals(errorMessage, (state as WeatherUiState.Error).message)
    }
    
    @Test
    fun `updateSearchQuery updates search query state`() = runTest {
        // Given
        val query = "Los Angeles"
        
        // When
        viewModel.updateSearchQuery(query)
        advanceUntilIdle()
        
        // Then
        assertEquals(query, viewModel.searchQuery.value)
    }
    
    @Test
    fun `clearError changes state from error to initial`() = runTest {
        // Given
        val cityName = "InvalidCity"
        every { getWeatherByCityUseCase(cityName) } returns flow {
            emit(Result.Error(Exception(), "Error"))
        }
        
        viewModel.searchWeather(cityName)
        advanceUntilIdle()
        
        // When
        viewModel.clearError()
        
        // Then
        assertTrue(viewModel.uiState.value is WeatherUiState.Initial)
    }
    
    @Test
    fun `loads last searched city on init`() = runTest {
        // Given
        val lastCity = "Chicago"
        val mockWeatherData = createMockWeatherData()
        
        every { preferencesManager.getLastSearchedCity() } returns lastCity
        every { getWeatherByCityUseCase(lastCity) } returns flow {
            emit(Result.Success(mockWeatherData))
        }
        
        // When - create new ViewModel instance
        val newViewModel = WeatherViewModel(
            getWeatherByCityUseCase,
            getWeatherByCoordinatesUseCase,
            preferencesManager,
            locationManager
        )
        advanceUntilIdle()
        
        // Then
        verify { preferencesManager.getLastSearchedCity() }
        verify { getWeatherByCityUseCase(lastCity) }
    }
    
    private fun createMockWeatherData() = WeatherData(
        cityName = "New York",
        country = "US",
        temperature = 72.5,
        feelsLike = 70.0,
        tempMin = 68.0,
        tempMax = 75.0,
        humidity = 60,
        pressure = 1013,
        windSpeed = 10.5,
        weatherDescription = "Clear sky",
        weatherMain = "Clear",
        weatherIcon = "01d",
        latitude = 40.7128,
        longitude = -74.0060,
        timestamp = System.currentTimeMillis() / 1000
    )
}
