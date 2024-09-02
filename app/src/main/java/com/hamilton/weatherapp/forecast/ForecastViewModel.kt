package com.hamilton.weatherapp.forecast

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForecastViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(ForecastScreenState())
    val uiState: StateFlow<ForecastScreenState> = _uiState.asStateFlow()


}