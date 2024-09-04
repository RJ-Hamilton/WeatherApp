package com.hamilton.weatherapp.utils

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.hamilton.weatherapp.R

@Composable
fun LocationPermissionRequester(
    componentActivityContext: ComponentActivity,
    onLocationPermissionGranted: () -> Unit
) {
    var showRationale by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        showRationale = !isGranted
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (isLocationPermissionGranted(componentActivityContext)) {
            showRationale = false
            onLocationPermissionGranted()
        } else {
            showRationale = true
        }
    }

    LaunchedEffect(Unit) {
        when {
            isLocationPermissionGranted(componentActivityContext) -> {
                showRationale = false
                onLocationPermissionGranted()
            }

            shouldShowRequestPermissionRationale(componentActivityContext) -> {
                showRationale = true
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    if (showRationale) {
        PermissionRationale(
            title = stringResource(R.string.permission_rationale_title),
            description = stringResource(R.string.permission_rationale_description),
            positiveButtonText = stringResource(R.string.permission_rationale_settings_positive_button_text),
            negativeButtonText = stringResource(R.string.permission_rationale_negative_button_text),
            onDismissRequest = {
                showRationale = true
                componentActivityContext.finish()
            },
            onGrantPermission = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", componentActivityContext.packageName, null)
                }
                settingsLauncher.launch(intent)
            }
        )
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
    return ActivityCompat.shouldShowRequestPermissionRationale(
        componentActivity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}
