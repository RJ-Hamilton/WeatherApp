package com.hamilton.weatherapp.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
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
    activityContext: Activity,
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
        if (isLocationPermissionGranted(activityContext)) {
            showRationale = false
            onLocationPermissionGranted()
        } else {
            showRationale = true
        }
    }

    LaunchedEffect(Unit) {
        when {
            isLocationPermissionGranted(activityContext) -> {
                showRationale = false
                onLocationPermissionGranted()
            }

            shouldShowRequestPermissionRationale(activityContext) -> {
                showRationale = true
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            onLocationPermissionGranted()
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
                activityContext.finish()
            },
            onGrantPermission = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activityContext.packageName, null)
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

private fun isLocationPermissionGranted(activityContext: Activity): Boolean {
    return ActivityCompat.checkSelfPermission(
        activityContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}
