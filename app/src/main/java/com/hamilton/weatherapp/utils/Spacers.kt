package com.hamilton.weatherapp.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Used to create vertical spacing with a given height.
 *
 * @param height Amount of spacing to be added in [Dp]
 */
@Composable
fun VerticalSpacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}

/**
 * Used to create horizontal spacing with a given width.
 *
 * @param width Amount of spacing to be added in [Dp]
 */
@Composable
fun HorizontalSpacer(width: Int) {
    Spacer(modifier = Modifier.width(width.dp))
}