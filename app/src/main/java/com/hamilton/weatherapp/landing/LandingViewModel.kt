package com.hamilton.weatherapp.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.weatherapp.landing.models.CurrentWeatherUiModelMapper
import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModelMapper
import com.hamilton.weatherapp.utils.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationManager: LocationManager,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(LandingScreenState())
    val uiState: StateFlow<LandingScreenState> = _uiState.asStateFlow()

    fun getCurrentWeather(latitude: Double, longitude: Double, isRefreshing: Boolean = false) {
        if (isRefreshing) {
            _uiState.update { currentState ->
                currentState.copy(isRefreshing = true)
            }
        } else {
            showLoadingState()
            locationManager.saveLocation(latitude = latitude, longitude = longitude)
        }
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val currentWeather = weatherRepository.getCurrentWeather(
                    lat = latitude,
                    long = longitude
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        latLong = LatLong(latitude = latitude, longitude = longitude),
                        currentWeatherUiModel = CurrentWeatherUiModelMapper.fromWeatherData(
                            weatherData = currentWeather
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        latLong = LatLong(latitude = latitude, longitude = longitude)
                    )
                }
            }
        }
    }

    fun getForecastWeather(latitude: Double, longitude: Double, isRefreshing: Boolean = false) {
        if (isRefreshing) {
            _uiState.update { currentState ->
                currentState.copy(isRefreshing = true)
            }
        } else {
            showLoadingState()
        }
        viewModelScope.launch(coroutineDispatcher) {
            try {
                val forecastWeather = weatherRepository.getForecastWeather(
                    lat = latitude,
                    long = longitude
                )
                val currentDateTime = Clock.System.now().toLocalDateTime(
                    TimeZone.currentSystemDefault()
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        hourlyWeatherUiModels = forecastWeather.filter {
                            it.date.date == currentDateTime.date
                        }.map { weatherData ->
                            HourlyWeatherUiModelMapper.fromWeatherData(
                                weatherData = weatherData
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    fun fetchData(latitude: Double, longitude: Double, isRefreshing: Boolean = false) {
        getCurrentWeather(latitude, longitude, isRefreshing)
        getForecastWeather(latitude, longitude, isRefreshing)
    }

    private fun showLoadingState() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }
}