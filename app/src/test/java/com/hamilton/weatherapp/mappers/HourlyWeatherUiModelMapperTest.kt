package com.hamilton.weatherapp.mappers

import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModel
import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModelMapper
import com.hamilton.weatherapp.test_helpers.WeatherTestData
import kotlinx.datetime.toJavaLocalDateTime
import org.junit.Test
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.test.assertEquals

class HourlyWeatherUiModelMapperTest {
    @Test
    fun `fromWeatherData should correctly map WeatherData to HourlyWeatherUiModel`() {
        // GIVEN
        val weatherData = WeatherTestData.weatherData
        val expectedTemperature = WeatherTestData.weatherData.temperatureData.temperature.toInt()
        val expectedDate = WeatherTestData.currentDateTime

        // WHEN
        val result = HourlyWeatherUiModelMapper.fromWeatherData(weatherData)

        // THEN
        val expectedFormattedTime = expectedDate.toJavaLocalDateTime().toLocalTime().format(
            DateTimeFormatter.ofLocalizedTime(
                FormatStyle.SHORT
            )
        )
        assertEquals(
            result,
            HourlyWeatherUiModel(
                time = expectedFormattedTime,
                temperature = expectedTemperature,
                weatherIconName = "10d"
            )
        )
    }
}