package com.hamilton.services.open_weather_map.api

import com.hamilton.services.open_weather_map.api.domain.WeatherData

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, long: Double): WeatherData
}