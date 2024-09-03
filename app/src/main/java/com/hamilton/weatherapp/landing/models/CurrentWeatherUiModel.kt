package com.hamilton.weatherapp.landing.models

import com.hamilton.services.open_weather_map.api.domain.WeatherData

data class CurrentWeatherUiModel(
    val cityName: String = "",
    val windDirection: String = "",
    val windSpeed: Int = 0,
    val feelsLikeTemp: Int = 0,
    val highTemp: Int = 0,
    val lowTemp: Int = 0
)

object CurrentWeatherUiModelMapper {
    fun toCurrentWeatherUiModel(weatherData: WeatherData): CurrentWeatherUiModel {
        return CurrentWeatherUiModel(
            cityName = weatherData.cityName,
            windDirection = weatherData.windData.direction.displayString,
            windSpeed = weatherData.windData.speed.toInt(),
            feelsLikeTemp = weatherData.temperatureData.feelsLike.toInt(),
            highTemp = weatherData.temperatureData.temperatureHigh.toInt(),
            lowTemp = weatherData.temperatureData.temperatureLow.toInt()
        )
    }
}