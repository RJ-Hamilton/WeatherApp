package com.hamilton.weatherapp.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.weatherapp.forecast.models.ForecastItemUiModelMapper
import com.hamilton.weatherapp.utils.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationManager: LocationManager,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForecastScreenState())
    val uiState: StateFlow<ForecastScreenState> = _uiState.asStateFlow()

    fun getForecast(isRefreshing: Boolean = false) {
        if (isRefreshing) {
            _uiState.update { currentState ->
                currentState.copy(isRefreshing = true)
            }
        } else {
            showLoadingState()
        }

        viewModelScope.launch(coroutineDispatcher) {
            try {
                val forecastWeather = weatherRepository.getDailyForecast(
                    lat = locationManager.getLatitude(),
                    long = locationManager.getLongitude()
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        forecastItemUiModels = forecastWeather.map { weatherData ->
                            ForecastItemUiModelMapper.mapFromWeatherData(
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

    private fun showLoadingState() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }
}
