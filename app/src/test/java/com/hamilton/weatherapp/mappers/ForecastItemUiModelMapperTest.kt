package com.hamilton.weatherapp.mappers

import com.hamilton.weatherapp.landing.models.CurrentWeatherUiModel
import com.hamilton.weatherapp.landing.models.CurrentWeatherUiModelMapper
import com.hamilton.weatherapp.test_helpers.WeatherTestData
import org.junit.Test
import kotlin.test.assertEquals

class ForecastItemUiModelMapperTest {
    @Test
    fun `fromWeatherData should correctly map WeatherData to ForecastItemUiModel`() {
        // GIVEN
        val weatherData = WeatherTestData.weatherData

        // WHEN
        val result = CurrentWeatherUiModelMapper.fromWeatherData(weatherData)

        // THEN
        assertEquals(
            result,
            CurrentWeatherUiModel(
                cityName = weatherData.cityName,
                windDirection = weatherData.windData.direction.displayString,
                windSpeed = weatherData.windData.speed.toInt(),
                feelsLikeTemp = weatherData.temperatureData.feelsLike.toInt(),
                highTemp = weatherData.temperatureData.temperatureHigh.toInt(),
                lowTemp = weatherData.temperatureData.temperatureLow.toInt()
            )
        )
    }
}