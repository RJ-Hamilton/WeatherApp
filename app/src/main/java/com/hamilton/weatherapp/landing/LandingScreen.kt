package com.hamilton.weatherapp.landing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.hamilton.weatherapp.R
import com.hamilton.weatherapp.ui.FullScreenLoadingIndicator
import com.hamilton.weatherapp.utils.VerticalSpacer

@OptIn(ExperimentalMaterial3Api::class)
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
            getCurrentLocation(context) { lat, long ->
                viewModel.getCurrentWeather(lat, long)
                viewModel.getForecastWeather(lat, long)
            }
        } else {
            viewModel.updatePermissionState(isLocationPermissionGranted = false)
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.updatePermissionState(isLocationPermissionGranted = false)
    }

    LaunchedEffect(Unit) {
        when {
            isLocationPermissionGranted(context) -> {
                getCurrentLocation(context) { lat, long ->
                    viewModel.getCurrentWeather(lat, long)
                    viewModel.getForecastWeather(lat, long)
                }
            }

            shouldShowRequestPermissionRationale(context) -> {
                viewModel.updateShouldShowPermissionRationale(shouldShow = true)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    when {
        state.shouldShowPermissionRationale -> {
            PermissionRationale(
                title = stringResource(R.string.permission_rationale_title),
                description = stringResource(R.string.permission_rationale_description),
                positiveButtonText = stringResource(R.string.permission_rationale_settings_positive_button_text),
                negativeButtonText = stringResource(R.string.permission_rationale_negative_button_text),
                onDismissRequest = {
                    viewModel.updateShouldShowPermissionRationale(shouldShow = false)
                    context.finish()
                },
                onGrantPermission = {
                    viewModel.updateShouldShowPermissionRationale(shouldShow = false)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    settingsLauncher.launch(intent)
                }
            )
        }

        state.isLoading -> {
            FullScreenLoadingIndicator()
        }

        else -> {
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
}

@Composable
fun PermissionRationale(
    title: String,
    description: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onDismissRequest: () -> Unit,
    onGrantPermission: () -> Unit
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(
                text = description
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onGrantPermission,
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = positiveButtonText,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = negativeButtonText,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
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