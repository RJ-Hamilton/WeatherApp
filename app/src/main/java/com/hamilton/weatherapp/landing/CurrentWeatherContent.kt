package com.hamilton.weatherapp.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.hamilton.weatherapp.R
import com.hamilton.weatherapp.landing.models.CurrentWeatherUiModel
import com.hamilton.weatherapp.ui.theme.WeatherAppTheme
import com.hamilton.weatherapp.utils.VerticalSpacer

@Composable
fun CurrentWeatherContent(
    modifier: Modifier = Modifier,
    currentWeatherUiModel: CurrentWeatherUiModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentWeatherUiModel.cityName,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        VerticalSpacer(8)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WindContent(
                windDirection = currentWeatherUiModel.windDirection,
                windSpeed = currentWeatherUiModel.windSpeed
            )
            TemperatureContent(
                feelsLikeTemp = currentWeatherUiModel.feelsLikeTemp,
                highTemp = currentWeatherUiModel.highTemp,
                lowTemp = currentWeatherUiModel.lowTemp
            )
        }
    }
}

@Composable
private fun WindContent(windDirection: String, windSpeed: Int) {
    Column {
        Text(
            text = stringResource(R.string.landing_screen_wind_content_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = windDirection,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.landing_screen_wind_content_wind_speed, windSpeed),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TemperatureContent(feelsLikeTemp: Int, highTemp: Int, lowTemp: Int) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = stringResource(
                R.string.landing_screen_wind_content_feels_like,
                "$feelsLikeTemp°"
            ),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "$highTemp°/$lowTemp°",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun PreviewCurrentWeatherContent() {
    val uiModel = CurrentWeatherUiModel(
        cityName = "Grand Rapids",
        windDirection = "NE",
        windSpeed = 10,
        feelsLikeTemp = 60,
        highTemp = 65,
        lowTemp = 35
    )
    WeatherAppTheme {
        CurrentWeatherContent(currentWeatherUiModel = uiModel)
    }
}