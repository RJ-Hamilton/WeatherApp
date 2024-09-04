package com.hamilton.weatherapp.forecast

import androidx.annotation.StringRes
import com.hamilton.weatherapp.forecast.models.ForecastItemUiModel

data class ForecastScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val forecastItemUiModels: List<ForecastItemUiModel> = emptyList()
)
