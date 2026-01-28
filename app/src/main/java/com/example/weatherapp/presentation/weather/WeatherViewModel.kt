package com.example.weatherapp.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.PreferencesManager
import com.example.weatherapp.data.location.LocationManager
import com.example.weatherapp.domain.model.WeatherData
import com.example.weatherapp.domain.usecase.GetWeatherByCityUseCase
import com.example.weatherapp.domain.usecase.GetWeatherByCoordinatesUseCase
import com.example.weatherapp.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Weather Screen
 * Manages UI state using StateFlow and handles business logic
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherByCityUseCase: GetWeatherByCityUseCase,
    private val getWeatherByCoordinatesUseCase: GetWeatherByCoordinatesUseCase,
    private val preferencesManager: PreferencesManager,
    private val locationManager: LocationManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        // Auto-load last searched city on app launch
        loadLastSearchedCity()
    }
    
    /**
     * Load the last searched city from preferences
     */
    private fun loadLastSearchedCity() {
        viewModelScope.launch {
            val lastCity = preferencesManager.getLastSearchedCity()
            if (!lastCity.isNullOrBlank()) {
                searchWeather(lastCity)
            }
        }
    }
    
    /**
     * Update search query
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Search weather by city name
     */
    fun searchWeather(cityName: String) {
        viewModelScope.launch {
            getWeatherByCityUseCase(cityName).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = WeatherUiState.Loading
                    }
                    is Result.Success -> {
                        // Save the successfully searched city
                        preferencesManager.saveLastSearchedCity(cityName)
                        _uiState.value = WeatherUiState.Success(result.data)
                    }
                    is Result.Error -> {
                        _uiState.value = WeatherUiState.Error(
                            result.message ?: "An error occurred"
                        )
                    }
                }
            }
        }
    }
    
    /**
     * Get weather by current location
     */
    fun getWeatherByLocation() {
        viewModelScope.launch {
            try {
                _uiState.value = WeatherUiState.Loading
                
                val location = locationManager.getCurrentLocation()
                
                getWeatherByCoordinatesUseCase(
                    location.latitude,
                    location.longitude
                ).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.value = WeatherUiState.Loading
                        }
                        is Result.Success -> {
                            // Save the city name from location
                            preferencesManager.saveLastSearchedCity(result.data.cityName)
                            _uiState.value = WeatherUiState.Success(result.data)
                        }
                        is Result.Error -> {
                            _uiState.value = WeatherUiState.Error(
                                result.message ?: "An error occurred"
                            )
                        }
                    }
                }
            } catch (e: SecurityException) {
                _uiState.value = WeatherUiState.Error(
                    "Location permission not granted. Please enable location access."
                )
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Failed to get location"
                )
            }
        }
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        if (_uiState.value is WeatherUiState.Error) {
            _uiState.value = WeatherUiState.Initial
        }
    }
}

/**
 * UI State sealed class for Weather Screen
 */
sealed class WeatherUiState {
    data object Initial : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
