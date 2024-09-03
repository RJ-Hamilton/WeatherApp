package com.hamilton.weatherapp.forecast.models

data class ForecastItemUiModel(
    val date: String,
    val windDirection: String = "",
    val windSpeed: Int = 0,
    val highTemp: Int = 0,
    val lowTemp: Int = 0
)
