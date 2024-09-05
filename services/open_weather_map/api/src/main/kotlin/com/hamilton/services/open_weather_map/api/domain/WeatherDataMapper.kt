package com.hamilton.services.open_weather_map.api.domain

import com.hamilton.services.open_weather_map.api.domain.WindDirection.Companion.mapToWindDirection
import com.hamilton.services.open_weather_map.api.models.WeatherDetails
import com.hamilton.services.open_weather_map.api.models.WeatherResponse
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime as JavaLocalDateTime

object WeatherDataMapper {
    fun mapFromWeatherResponse(weatherResponse: WeatherResponse): WeatherData {
        return WeatherData(
            cityName = weatherResponse.name,
            windData = WindData(
                speed = weatherResponse.wind.speed,
                direction = weatherResponse.wind.deg.mapToWindDirection()
            ),
            temperatureData = TemperatureData(
                temperature = weatherResponse.main.temp,
                temperatureHigh = weatherResponse.main.tempMax,
                temperatureLow = weatherResponse.main.tempMin,
                feelsLike = weatherResponse.main.feelsLike
            ),
            date = convertUnixTimestampToLocalDateTime(
                unixTime = weatherResponse.dt.toLong(),
                timezoneShiftInSeconds = weatherResponse.timezone
            ),
            weatherIcon = weatherResponse.weather.first().icon,
            weatherDescription = weatherResponse.weather.first().description
        )
    }

    fun mapFromWeatherDetails(weatherDetails: WeatherDetails): WeatherData {
        return WeatherData(
            cityName = "",
            windData = WindData(
                speed = weatherDetails.wind.speed,
                direction = weatherDetails.wind.deg.mapToWindDirection()
            ),
            temperatureData = TemperatureData(
                temperature = weatherDetails.main.temp,
                temperatureHigh = weatherDetails.main.tempMax,
                temperatureLow = weatherDetails.main.tempMin,
                feelsLike = weatherDetails.main.feelsLike
            ),
            date = convertToLocalDateTime(weatherDetails.dateText),
            weatherIcon = weatherDetails.weather.first().icon,
            weatherDescription = weatherDetails.weather.first().description
        )
    }

    private fun convertUnixTimestampToLocalDateTime(
        unixTime: Long,
        timezoneShiftInSeconds: Int
    ): LocalDateTime {
        val instant = Instant.fromEpochSeconds(unixTime + timezoneShiftInSeconds)
        return instant.toLocalDateTime(TimeZone.UTC)
    }

    private fun convertToLocalDateTime(dateTimeString: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val javaLocalDateTime = JavaLocalDateTime.parse(dateTimeString, formatter)
        return javaLocalDateTime.toKotlinLocalDateTime()
    }
}