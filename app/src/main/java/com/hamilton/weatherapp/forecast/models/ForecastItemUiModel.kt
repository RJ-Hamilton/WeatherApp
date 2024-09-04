package com.hamilton.weatherapp.forecast.models

import com.hamilton.services.open_weather_map.api.domain.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ForecastItemUiModel(
    val date: String,
    val windDirection: String = "",
    val windSpeed: Int = 0,
    val highTemp: Int = 0,
    val lowTemp: Int = 0,
    val weatherDescription: String = "",
    val weatherIconString: String = ""
)

object ForecastItemUiModelMapper {
    fun mapFromWeatherData(weatherData: WeatherData): ForecastItemUiModel {
        val localDateTime = LocalDateTime.parse(weatherData.date.toString())
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)

        return ForecastItemUiModel(
            date = localDateTime.format(formatter),
            windDirection = weatherData.windData.direction.displayString,
            windSpeed = weatherData.windData.speed.toInt(),
            highTemp = weatherData.temperatureData.temperatureHigh.toInt(),
            lowTemp = weatherData.temperatureData.temperatureLow.toInt(),
            weatherDescription = weatherData.weatherDescription,
            weatherIconString = weatherData.weatherIcon
        )
    }
}
