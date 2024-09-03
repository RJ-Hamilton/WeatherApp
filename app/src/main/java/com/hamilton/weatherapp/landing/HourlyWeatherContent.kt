package com.hamilton.weatherapp.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hamilton.weatherapp.R
import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModel
import com.hamilton.weatherapp.ui.theme.WeatherAppTheme
import com.hamilton.weatherapp.utils.HorizontalSpacer
import com.hamilton.weatherapp.utils.VerticalSpacer

private const val WEATHER_ICON_URL = "https://openweathermap.org/img/wn/[iconName]@2x.png"

@Composable
fun HourlyWeatherContent(hourlyWeatherUiModels: List<HourlyWeatherUiModel>) {
    LazyColumn {
        items(hourlyWeatherUiModels) { uiModel ->
            HourlyWeatherView(hourlyWeatherUiModel = uiModel)
            VerticalSpacer(8)
        }
    }
}

@Composable
private fun HourlyWeatherView(
    modifier: Modifier = Modifier,
    hourlyWeatherUiModel: HourlyWeatherUiModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = hourlyWeatherUiModel.time,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        TemperatureAndIcon(
            temperature = hourlyWeatherUiModel.temperature,
            weatherIconName = hourlyWeatherUiModel.weatherIconName
        )
    }
}

@Composable
private fun TemperatureAndIcon(
    modifier: Modifier = Modifier,
    temperature: Int,
    weatherIconName: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$temperature° F",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        HorizontalSpacer(16)

        AsyncImage(
            modifier = Modifier.size(48.dp),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            model = stringResource(R.string.weather_icon_url, weatherIconName),
            contentDescription = "Weather icon"
        )
    }
}

@Preview
@Composable
private fun PreviewHourlyWeatherContent() {
    val uiModels = listOf(
        HourlyWeatherUiModel(
            time = "1:00 PM",
            temperature = 66,
            weatherIconName = "10d"
        ),
        HourlyWeatherUiModel(
            time = "2:00 PM",
            temperature = 67,
            weatherIconName = "10d"
        ),
        HourlyWeatherUiModel(
            time = "3:00 PM",
            temperature = 68,
            weatherIconName = "10d"
        ),
        HourlyWeatherUiModel(
            time = "4:00 PM",
            temperature = 65,
            weatherIconName = "10d"
        )
    )
    WeatherAppTheme {
        HourlyWeatherContent(uiModels)
    }
}