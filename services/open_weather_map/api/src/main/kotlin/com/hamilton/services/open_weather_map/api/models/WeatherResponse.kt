package com.hamilton.services.open_weather_map.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For more detailed information on this response data, see
 * [https://openweathermap.org/current](https://openweathermap.org/current).
 */
@Serializable
data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

@Serializable
data class Clouds(
    val all: Int
)

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Main(
    @SerialName("feels_like")val feelsLike: Double,
    @SerialName("grnd_level") val groundLevel: Int,
    val humidity: Int,
    val pressure: Int,
    @SerialName("sea_level") val seaLevel: Int,
    val temp: Double,
    @SerialName("temp_max") val tempMax: Double,
    @SerialName("temp_min") val tempMin: Double
)

@Serializable
data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)

@Serializable
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

@Serializable
data class Wind(
    val deg: Int = 0,
    val gust: Double = 0.0,
    val speed: Double = 0.00
)