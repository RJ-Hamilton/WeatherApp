package com.hamilton.weatherapp.landing.models

import com.hamilton.services.open_weather_map.api.domain.WeatherData
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class HourlyWeatherUiModel(
    val time: String,
    val temperature: Int,
    val weatherIconName: String
)

object HourlyWeatherUiModelMapper {
    fun toHourlyWeatherUiModel(weatherData: WeatherData): HourlyWeatherUiModel {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

        return HourlyWeatherUiModel(
            time = weatherData.date.toJavaLocalDateTime().toLocalTime().format(dateTimeFormatter),
            temperature = weatherData.temperatureData.temperature.toInt(),
            weatherIconName = weatherData.weatherIcon
        )
    }
}
