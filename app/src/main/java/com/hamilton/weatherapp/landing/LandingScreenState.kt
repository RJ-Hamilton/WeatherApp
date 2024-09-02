package com.hamilton.weatherapp.landing

import androidx.annotation.StringRes

data class LandingScreenState(
    val isLoading: Boolean = false,
    val isLocationPermissionGranted: Boolean = false,
    val shouldShowPermissionRationale: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
