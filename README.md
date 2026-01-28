# Weather App

A modern Android weather application built with **MVVM + Clean Architecture** pattern, showcasing best practices in Android development.

## 🏗️ Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
app/
├── data/                          # Data Layer
│   ├── local/                     # Local data sources
│   │   └── PreferencesManager     # SharedPreferences wrapper
│   ├── location/                  # Location services
│   │   └── LocationManager        # Google Play Services location
│   ├── mapper/                    # DTO to Domain mappers
│   ├── remote/                    # Remote data sources
│   │   ├── api/                   # Retrofit API service
│   │   └── dto/                   # Data Transfer Objects
│   └── repository/                # Repository implementations
│
├── domain/                        # Domain Layer (Business Logic)
│   ├── model/                     # Domain models
│   ├── repository/                # Repository interfaces
│   ├── usecase/                   # Use cases
│   └── util/                      # Domain utilities
│
├── presentation/                  # Presentation Layer
│   ├── weather/                   # Weather feature
│   │   ├── WeatherScreen          # Jetpack Compose UI
│   │   └── WeatherViewModel       # ViewModel with StateFlow
│   └── ui/theme/                  # Compose theme
│
└── di/                            # Dependency Injection
    └── AppModule                  # Hilt modules
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Concurrency**: Kotlin Coroutines & Flows/StateFlows
- **Image Loading**: Coil
- **Location**: Google Play Services Location
- **Permissions**: Accompanist Permissions
- **Testing**: JUnit, MockK, Coroutines Test

## ✨ Features

### Core Features
- ✅ **City Search**: Search weather by US city name
- ✅ **Location-Based Weather**: Get weather for current location (requires permission)
- ✅ **Persistent Storage**: Auto-loads last searched city on app launch
- ✅ **Weather Icons**: Downloads and displays weather condition icons with caching
- ✅ **Beautiful UI**: Modern Material 3 design with gradient backgrounds

### Technical Features
- ✅ **Clean Architecture**: Clear separation between data, domain, and presentation layers
- ✅ **MVVM Pattern**: Reactive UI with StateFlow
- ✅ **Error Handling**: Comprehensive error handling with user-friendly messages
- ✅ **Dependency Injection**: Hilt for scalable DI
- ✅ **Unit Tests**: Test coverage for ViewModel and Repository
- ✅ **Coroutines**: Async operations with structured concurrency
- ✅ **Image Caching**: Coil handles image caching automatically
- ✅ **Location Services**: Google Play Services integration

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 28+
- Kotlin 2.0+

### Setup

1. **Clone the repository**
   ```bash
   cd /Users/vgonuguntala/AndroidStudioProjects/WeatherApp
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory

3. **Sync Gradle**
   - Android Studio will automatically prompt to sync
   - Or click: File → Sync Project with Gradle Files

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click the Run button (▶️) or press Shift+F10

### API Key

The OpenWeatherMap API key is already configured in `build.gradle.kts`:
```kotlin
buildConfigField("String", "WEATHER_API_KEY", "\"74867586c140eb6574cd5cd15d4c9be3\"")
```

## 📱 How to Use

1. **Launch the app**
   - On first launch, the app will request location permissions
   - Grant permissions to use location-based weather

2. **Search by City**
   - Enter a US city name in the search bar
   - Press the search button or keyboard search key
   - Weather data will be displayed

3. **Use Current Location**
   - Tap the location button (📍)
   - The app will fetch weather for your current location

4. **Auto-Load Last Search**
   - The app remembers your last searched city
   - Next time you open the app, it automatically loads that city's weather

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Test Coverage
- `WeatherViewModelTest`: Tests ViewModel business logic
- `WeatherRepositoryImplTest`: Tests repository data operations

## 🔑 Key Implementation Details

### Clean Architecture Layers

**Data Layer**
- `WeatherApiService`: Retrofit interface for API calls
- `WeatherRepositoryImpl`: Concrete repository implementation
- `WeatherMapper`: Converts DTOs to domain models

**Domain Layer**
- `WeatherRepository`: Repository interface (abstraction)
- `GetWeatherByCityUseCase`: Business logic for city search
- `GetWeatherByCoordinatesUseCase`: Business logic for location search
- `WeatherData`: Domain model (clean, business-focused)

**Presentation Layer**
- `WeatherViewModel`: Manages UI state with StateFlow
- `WeatherScreen`: Jetpack Compose UI
- `WeatherUiState`: Sealed class for UI states

### Dependency Injection

**AppModule** provides:
- Retrofit instance with OkHttp client
- WeatherApiService
- WeatherRepository implementation

All ViewModels are annotated with `@HiltViewModel` for automatic injection.

### State Management

Using **StateFlow** for reactive state management:
```kotlin
sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weatherData: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
```

### Error Handling

Comprehensive error handling in repository:
- HTTP errors (404, 401, 429, etc.)
- Network errors (IOException)
- Unexpected errors
- User-friendly error messages

## 📝 API Reference

**OpenWeatherMap API**
- Base URL: `https://api.openweathermap.org/data/2.5/`
- Endpoint: `/weather`
- Units: Imperial (Fahrenheit for US cities)

## 🎨 UI/UX Features

- **Modern Design**: Material 3 with gradient backgrounds
- **Responsive Layout**: Adapts to different screen sizes
- **Loading States**: Clear loading indicators
- **Error States**: User-friendly error messages with retry
- **Weather Details**: Temperature, humidity, wind speed, pressure, etc.
- **Weather Icons**: Visual representation of weather conditions

## 🔐 Permissions

- `INTERNET`: Required for API calls
- `ACCESS_NETWORK_STATE`: Check network connectivity
- `ACCESS_FINE_LOCATION`: Precise location for weather
- `ACCESS_COARSE_LOCATION`: Approximate location (fallback)

## 📦 Dependencies

See `gradle/libs.versions.toml` for complete dependency list.

## 🐛 Known Issues

None currently. All features are working as expected.

## 🤝 Contributing

This is a demo project. Feel free to use it as a reference for your own projects!

## 📄 License

This project is for demonstration purposes.

---

**Built with ❤️ using Modern Android Development practices**
