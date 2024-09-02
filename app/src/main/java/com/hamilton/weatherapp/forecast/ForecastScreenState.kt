package com.hamilton.weatherapp.forecast

import androidx.annotation.StringRes

data class ForecastScreenState(
    val isLoading: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
