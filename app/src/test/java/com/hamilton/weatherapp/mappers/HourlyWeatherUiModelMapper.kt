package com.hamilton.weatherapp.mappers

import com.hamilton.services.open_weather_map.api.domain.TemperatureData
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModelMapper
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.datetime.toJavaLocalDateTime
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class HourlyWeatherUiModelMapperTest {
    @Test
    fun `fromWeatherData should correctly map WeatherData to HourlyWeatherUiModel`() {
        // GIVEN: Mocked WeatherData
        val mockWeatherData = mockk<WeatherData>()
        val mockTemperatureData = mockk<TemperatureData>()
        val expectedDate = LocalDateTime.of(2024, 9, 4, 14, 30) // September 4, 2024, 2:30 PM

        every { mockWeatherData.date.toJavaLocalDateTime() } returns expectedDate
        every { mockTemperatureData.temperature } returns 22.5 // 22.5 degrees Celsius
        every { mockWeatherData.temperatureData } returns mockTemperatureData
        every { mockWeatherData.weatherIcon } returns "sunny" // Weather icon name

        // WHEN: Mapping from WeatherData to HourlyWeatherUiModel
        val result = HourlyWeatherUiModelMapper.fromWeatherData(mockWeatherData)

        // THEN: Expected results
        val expectedFormattedTime = expectedDate.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(
            FormatStyle.SHORT))
        assertEquals(expectedFormattedTime, result.time) // Verify the time is formatted correctly
        assertEquals(22, result.temperature) // Verify the temperature is converted to an integer
        assertEquals("sunny", result.weatherIconName) // Verify the weather icon name is mapped
    }

    @Test
    fun `fromWeatherData should correctly format time according to SHORT format`() {
        // GIVEN: Mocked WeatherData with specific time
        val mockWeatherData = mockk<WeatherData>()
        val mockTemperatureData = mockk<TemperatureData>()
        val expectedDate = LocalDateTime.of(2024, 9, 4, 9, 45) // September 4, 2024, 9:45 AM

        every { mockWeatherData.date.toJavaLocalDateTime() } returns expectedDate
        every { mockTemperatureData.temperature } returns 18.0
        every { mockWeatherData.temperatureData } returns mockTemperatureData
        every { mockWeatherData.weatherIcon } returns "cloudy"

        // WHEN: Mapping from WeatherData to HourlyWeatherUiModel
        val result = HourlyWeatherUiModelMapper.fromWeatherData(mockWeatherData)

        // THEN: Verify the time is formatted as "9:45 AM"
        val expectedFormattedTime = expectedDate.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(
            FormatStyle.SHORT))
        assertEquals(expectedFormattedTime, result.time)
    }
}