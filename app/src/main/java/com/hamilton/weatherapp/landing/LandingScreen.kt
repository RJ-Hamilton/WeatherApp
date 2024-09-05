package com.hamilton.weatherapp.landing

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.hamilton.weatherapp.ui.FullScreenLoadingIndicator
import com.hamilton.weatherapp.utils.LocationPermissionRequester
import com.hamilton.weatherapp.utils.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun LandingScreen(modifier: Modifier = Modifier) {
    val viewModel: LandingViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current as Activity

    LocationPermissionRequester(
        activityContext = context,
        onLocationPermissionGranted = {
            getCurrentLocation(context) { lat, long ->
                viewModel.getCurrentWeather(lat, long)
                viewModel.getForecastWeather(lat, long)
            }
        }
    )

    if (state.isLoading) {
        FullScreenLoadingIndicator()
    } else {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                viewModel.fetchData(
                    latitude = state.latLong.latitude,
                    longitude = state.latLong.longitude,
                    isRefreshing = true
                )
            }
        ) {
            Column(
                modifier = modifier.background(color = MaterialTheme.colorScheme.background)
            ) {
                CurrentWeatherContent(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    currentWeatherUiModel = state.currentWeatherUiModel
                )

                VerticalSpacer(32)

                HourlyWeatherContent(state.hourlyWeatherUiModels)
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context, onSuccessCallback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                onSuccessCallback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
        }
}