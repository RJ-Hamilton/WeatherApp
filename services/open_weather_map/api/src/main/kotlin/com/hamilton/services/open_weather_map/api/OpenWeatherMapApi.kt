package com.hamilton.services.open_weather_map.api

import com.hamilton.services.open_weather_map.api.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "9acc9f35dafbae0d80c1bccb3d3f1706"

interface OpenWeatherMapApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String = API_KEY
    ): WeatherResponse
}
