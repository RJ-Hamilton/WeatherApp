package com.hamilton.weatherapp.landing

import androidx.annotation.StringRes
import com.hamilton.weatherapp.landing.models.CurrentWeatherUiModel
import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModel

data class LandingScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLocationPermissionGranted: Boolean = false,
    val shouldShowPermissionRationale: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val latLong: LatLong = LatLong(),
    val currentWeatherUiModel: CurrentWeatherUiModel = CurrentWeatherUiModel(),
    val hourlyWeatherUiModels: List<HourlyWeatherUiModel> = emptyList()
)

data class LatLong(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
