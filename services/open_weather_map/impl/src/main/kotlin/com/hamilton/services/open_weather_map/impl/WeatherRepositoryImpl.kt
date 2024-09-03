package com.hamilton.services.open_weather_map.impl

import com.hamilton.services.open_weather_map.api.OpenWeatherMapApi
import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.services.open_weather_map.api.domain.WeatherDataMapper

class WeatherRepositoryImpl(
    private val openWeatherMapApi: OpenWeatherMapApi
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, long: Double): WeatherData {
        val response = openWeatherMapApi.getCurrentWeather(
            lat = lat,
            long = long
        )

        return WeatherDataMapper.mapFromWeatherResponse(response)
    }

    override suspend fun getForecastWeather(
        lat: Double,
        long: Double,
        numberOfTimeStamps: Int?
    ): List<WeatherData> {
        val response = openWeatherMapApi.getForecastWeather(
            lat = lat,
            long = long,
            numberOfTimestamps = numberOfTimeStamps
        )

        return response.list.map { weatherDetails ->
            WeatherDataMapper.mapFromWeatherDetails(weatherDetails)
        }
    }
}