package com.hamilton.services.open_weather_map.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * For more detailed information on this response data, see
 * [https://openweathermap.org/forecast5](https://openweathermap.org/forecast5).
 */
@Serializable
data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherDetails>,
    val city: City
)

@Serializable
data class WeatherDetails(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    @SerialName("dt_txt") val dateText: String
)

@Serializable
data class City(
    val id: Int,
    val name: String,
    @SerialName("coord") val coordinates: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)
