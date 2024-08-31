package com.hamilton.services.open_weather_map.impl

import com.hamilton.services.open_weather_map.api.OpenWeatherMapApi
import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.services.open_weather_map.api.domain.WeatherData

class WeatherRepositoryImpl(
    private val openWeatherMapApi: OpenWeatherMapApi
): WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, long: Double): WeatherData {
         val response = openWeatherMapApi.getCurrentWeather(
            lat = lat,
            long = long
        )

        return WeatherData(
            temp = response.main.temp
        )
    }
}