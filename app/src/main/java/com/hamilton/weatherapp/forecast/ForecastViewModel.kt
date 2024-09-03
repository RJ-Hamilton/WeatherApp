package com.hamilton.weatherapp.forecast

import androidx.lifecycle.ViewModel
import com.hamilton.services.open_weather_map.api.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ForecastViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForecastScreenState())
    val uiState: StateFlow<ForecastScreenState> = _uiState.asStateFlow()



}