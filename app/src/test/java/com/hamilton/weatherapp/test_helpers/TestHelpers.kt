package com.hamilton.weatherapp.test_helpers

import com.hamilton.services.open_weather_map.api.domain.TemperatureData
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.services.open_weather_map.api.domain.WindData
import com.hamilton.services.open_weather_map.api.domain.WindDirection
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object WeatherTestData {
    val currentDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()
    )
    val weatherData = WeatherData(
        cityName = "Grand Rapids",
        windData = WindData(
            speed = 28.29,
            direction = WindDirection.SOUTH
        ),
        temperatureData = TemperatureData(
            temperature = 30.31,
            temperatureHigh = 32.33,
            temperatureLow = 34.35,
            feelsLike = 36.37
        ),
        date = currentDateTime,
        weatherIcon = "10d",
        weatherDescription = "cloudy"
    )
}