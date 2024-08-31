package com.hamilton.services.open_weather_map.api

import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "9acc9f35dafbae0d80c1bccb3d3f1706"

interface OpenWeatherMapApi {
    @GET("/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("appid") apiKey: String = API_KEY
    ): WeatherResponse
}

data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

data class Clouds(
    val all: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Rain(
    val `1h`: Double
)

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)