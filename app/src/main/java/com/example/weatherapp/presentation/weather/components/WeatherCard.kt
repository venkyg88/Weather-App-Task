package com.example.weatherapp.presentation.weather.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.domain.model.WeatherData
import java.text.SimpleDateFormat
import java.util.*

/**
 * Main weather content card displaying all weather information
 */
@Composable
fun WeatherCard(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // City name and country
            Text(
                text = "${weatherData.cityName}, ${weatherData.country}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2193b0)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Date
            Text(
                text = formatDate(weatherData.timestamp),
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Weather icon
            AsyncImage(
                model = weatherData.getIconUrl(),
                contentDescription = "Weather icon",
                modifier = Modifier.size(120.dp)
            )
            
            // Temperature
            Text(
                text = weatherData.getFormattedTemperature(),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2193b0)
            )
            
            // Weather description
            Text(
                text = weatherData.weatherDescription.replaceFirstChar { 
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() 
                },
                fontSize = 20.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Weather details grid
            WeatherDetailsGrid(weatherData)
        }
    }
}

/**
 * Grid layout for weather details (humidity, wind, pressure, etc.)
 */
@Composable
fun WeatherDetailsGrid(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(
                label = "Feels Like",
                value = "${weatherData.feelsLike.toInt()}°F"
            )
            WeatherDetailItem(
                label = "Humidity",
                value = weatherData.getFormattedHumidity()
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(
                label = "Wind Speed",
                value = weatherData.getFormattedWindSpeed()
            )
            WeatherDetailItem(
                label = "Pressure",
                value = "${weatherData.pressure} hPa"
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(
                label = "Min Temp",
                value = "${weatherData.tempMin.toInt()}°F"
            )
            WeatherDetailItem(
                label = "Max Temp",
                value = "${weatherData.tempMax.toInt()}°F"
            )
        }
    }
}

/**
 * Individual weather detail item (label + value)
 */
@Composable
fun WeatherDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE3F2FD))
            .padding(16.dp)
            .width(140.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2193b0)
        )
    }
}

/**
 * Formats Unix timestamp to readable date string
 */
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}
