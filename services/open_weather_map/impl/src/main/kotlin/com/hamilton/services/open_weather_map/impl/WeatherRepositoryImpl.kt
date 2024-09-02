package com.hamilton.services.open_weather_map.impl

import com.hamilton.services.open_weather_map.api.OpenWeatherMapApi
import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.services.open_weather_map.api.domain.TemperatureData
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.services.open_weather_map.api.domain.WindData
import com.hamilton.services.open_weather_map.api.domain.WindDirection.Companion.mapToWindDirection
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class WeatherRepositoryImpl(
    private val openWeatherMapApi: OpenWeatherMapApi
): WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, long: Double): WeatherData {
         val response = openWeatherMapApi.getCurrentWeather(
            lat = lat,
            long = long
        )

        return with(response) {
            WeatherData(
                cityName = name,
                windData = WindData(
                    speed = wind.speed,
                    direction = wind.deg.mapToWindDirection()
                ),
                temperatureData = TemperatureData(
                    temperature = main.temp,
                    temperatureHigh = main.temp_max,
                    temperatureLow = main.temp_min,
                    feelsLike = main.feels_like
                ),
                date = convertUnixTimestampToLocalDateTime(dt.toLong(), timezone),
                weatherIcon = weather.first().icon,
                weatherDescription = weather.first().description
            )
        }
    }

    private fun convertUnixTimestampToLocalDateTime(unixTime: Long, timezoneShiftInSeconds: Int): LocalDateTime {
        val instant = Instant.fromEpochSeconds(unixTime + timezoneShiftInSeconds)
        return instant.toLocalDateTime(TimeZone.UTC)
    }
}