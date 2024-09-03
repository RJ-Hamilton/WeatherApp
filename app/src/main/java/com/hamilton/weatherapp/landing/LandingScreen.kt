package com.hamilton.weatherapp.landing

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun LandingScreen(modifier: Modifier = Modifier) {
    val viewModel: LandingViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current as ComponentActivity

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.updatePermissionState(isLocationPermissionGranted = true)
        } else {
            viewModel.updatePermissionState(isLocationPermissionGranted = false)
        }
    }

    LaunchedEffect(Unit) {
        when {
            isLocationPermissionGranted(context) -> {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val lat = location.latitude
                            val long = location.longitude
                            viewModel.getCurrentWeather(lat, long)
                        }
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                    }
            }

            shouldShowRequestPermissionRationale(context) -> {
                viewModel.updateShouldShowPermissionRationale(true)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }


    Column(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        CurrentWeatherContent(
            modifier = Modifier.padding(horizontal = 8.dp),
            currentWeatherUiModel = state.currentWeatherUiModel
        )
    }
}

private fun isLocationPermissionGranted(componentActivity: ComponentActivity): Boolean {
    return ActivityCompat.checkSelfPermission(
        componentActivity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun shouldShowRequestPermissionRationale(componentActivity: ComponentActivity): Boolean {
    return shouldShowRequestPermissionRationale(
        componentActivity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}