package com.example.weatherapp.presentation.weather

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.presentation.weather.components.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * Main Weather Screen with Jetpack Compose
 * Features: Search by city, location-based weather, beautiful gradient UI
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    // Location permissions
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    
    // Request location permission on first launch
    LaunchedEffect(Unit) {
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2193b0),
                        Color(0xFF6dd5ed)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Title
            Text(
                text = "Weather App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        viewModel.searchWeather(searchQuery)
                        keyboardController?.hide()
                    }
                },
                onLocationClick = {
                    if (locationPermissions.allPermissionsGranted) {
                        viewModel.getWeatherByLocation()
                    } else {
                        locationPermissions.launchMultiplePermissionRequest()
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Content based on state
            when (val state = uiState) {
                is WeatherUiState.Initial -> {
                    InitialState()
                }
                is WeatherUiState.Loading -> {
                    LoadingState()
                }
                is WeatherUiState.Success -> {
                    WeatherCard(weatherData = state.weatherData)
                }
                is WeatherUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onDismiss = { viewModel.clearError() }
                    )
                }
            }
        }
    }
}
