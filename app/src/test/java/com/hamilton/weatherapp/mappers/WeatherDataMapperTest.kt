package com.hamilton.weatherapp.mappers

import com.hamilton.services.open_weather_map.api.domain.TemperatureData
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.services.open_weather_map.api.domain.WeatherDataMapper
import com.hamilton.services.open_weather_map.api.domain.WindData
import com.hamilton.services.open_weather_map.api.domain.WindDirection
import com.hamilton.weatherapp.test_helpers.WeatherTestData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.junit.Test
import kotlin.test.assertEquals

class WeatherDataMapperTest {
    @Test
    fun `mapFromWeatherResponse should correctly map WeatherResponse to WeatherData`() {
        // GIVEN
        val weatherResponse = WeatherTestData.weatherResponse
        val expectedDateTime = LocalDateTime(
            year = 2024,
            monthNumber = 9,
            dayOfMonth = 5,
            hour = 16,
            minute = 52,
            second = 6
        )

        // WHEN
        val result = WeatherDataMapper.mapFromWeatherResponse(weatherResponse)

        // THEN
        assertEquals(
            result,
            WeatherData(
                cityName = "Grand Rapids",
                windData = WindData(
                    speed = 80.81,
                    direction = WindDirection.EAST
                ),
                temperatureData = TemperatureData(
                    temperature = 72.73,
                    temperatureHigh = 74.75,
                    temperatureLow = 76.77,
                    feelsLike = 70.71
                ),
                date = expectedDateTime,
                weatherIcon = "10d",
                weatherDescription = "cloudy"
            )
        )
    }

    @Test
    fun `mapFromWeatherDetails should correctly map WeatherDetails to WeatherData`() {
        // GIVEN
        val weatherDetails = WeatherTestData.weatherDetails

        // WHEN
        val result = WeatherDataMapper.mapFromWeatherDetails(weatherDetails)

        // THEN
        assertEquals(
            result,
            WeatherData(
                cityName = "",
                windData = WindData(
                    speed = 80.81,
                    direction = WindDirection.EAST
                ),
                temperatureData = TemperatureData(
                    temperature = 72.73,
                    temperatureHigh = 74.75,
                    temperatureLow = 76.77,
                    feelsLike = 70.71
                ),
                date = LocalDateTime(
                    year = 2024,
                    month = Month.SEPTEMBER,
                    dayOfMonth = 5,
                    hour = 18,
                    minute = 0,
                    second = 0
                ),
                weatherIcon = "10d",
                weatherDescription = "cloudy"
            )
        )
    }
}
