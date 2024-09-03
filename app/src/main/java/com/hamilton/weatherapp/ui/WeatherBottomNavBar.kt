package com.hamilton.weatherapp.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamilton.weatherapp.R
import com.hamilton.weatherapp.ui.theme.WeatherAppTheme

const val HOME_BUTTON_INDEX = 0
const val FORECAST_BUTTON_INDEX = 1

/**
 * A composable function that renders a custom bottom navigation bar for a weather application.
 *
 * @param modifier [Modifier] to be applied to the `BottomAppBar`. By default, this is an empty
 * `Modifier`.
 * @param onItemSelected Lambda function that is invoked when a navigation item is selected. It
 * receives the index of the selected item as an argument.
 */
@Composable
fun WeatherBottomNavBar(
    modifier: Modifier = Modifier,
    onItemSelected: (Int) -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        var selectedIndex by rememberSaveable { mutableIntStateOf(HOME_BUTTON_INDEX) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavButton(
                type = BottomNavButtonType.HOME,
                onClick = { newSelectedIndex ->
                    selectedIndex = newSelectedIndex
                    onItemSelected(newSelectedIndex)
                }
            )

            BottomNavButton(
                type = BottomNavButtonType.FORECAST,
                onClick = { newSelectedIndex ->
                    selectedIndex = newSelectedIndex
                    onItemSelected(newSelectedIndex)
                }
            )
        }
    }
}

@Composable
private fun BottomNavButton(
    modifier: Modifier = Modifier,
    type: BottomNavButtonType,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = modifier.clickable(indication = null, interactionSource = null) {
            onClick(type.data.buttonIndex)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = type.data.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = stringResource(type.data.stringResource),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private enum class BottomNavButtonType(val data: BottomNavButtonData) {
    HOME(
        data = BottomNavButtonData(
            buttonIndex = HOME_BUTTON_INDEX,
            icon = Icons.Default.Home,
            stringResource = R.string.bottom_nav_home_button
        )
    ),
    FORECAST(
        data = BottomNavButtonData(
            buttonIndex = FORECAST_BUTTON_INDEX,
            icon = Icons.Default.DateRange,
            stringResource = R.string.bottom_nav_forecast_button
        )
    )
}

private data class BottomNavButtonData(
    val buttonIndex: Int,
    val icon: ImageVector,
    @StringRes val stringResource: Int
)

@Preview
@Composable
private fun PreviewWeatherBottomNavBar() {
    WeatherAppTheme {
        WeatherBottomNavBar { }
    }
}