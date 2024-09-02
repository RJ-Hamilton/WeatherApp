package com.hamilton.services.open_weather_map.api.domain

import kotlinx.datetime.LocalDateTime


data class WeatherData(
    val cityName: String,
    val windData: WindData,
    val temperatureData: TemperatureData,
    val date: LocalDateTime,
    val weatherIcon: String,
    val weatherDescription: String
)

data class WindData(
    val speed: Double,
    val direction: WindDirection
)

enum class WindDirection(val displayString: String) {
    NORTH("N"),
    NORTH_EAST("NE"),
    EAST("E"),
    SOUTH_EAST("SE"),
    SOUTH("S"),
    SOUTH_WEST("SW"),
    WEST("W"),
    NORTH_WEST("NW");

    companion object {
        /**
         * Maps an integer value representing wind direction in degrees to the corresponding [WindDirection] enum.
         *
         * The mapping is defined as follows:
         * - 0° to 22°: [WindDirection.NORTH]
         * - 23° to 67°: [WindDirection.NORTH_EAST]
         * - 68° to 112°: [WindDirection.EAST]
         * - 113° to 157°: [WindDirection.SOUTH_EAST]
         * - 158° to 202°: [WindDirection.SOUTH]
         * - 203° to 247°: [WindDirection.SOUTH_WEST]
         * - 248° to 292°: [WindDirection.WEST]
         * - 293° to 337°: [WindDirection.NORTH_WEST]
         * - 338° to 360°: [WindDirection.NORTH]
         *
         * @receiver Int The wind direction in degrees, expected to be in the range of 0 to 360.
         * @return [WindDirection] The corresponding [WindDirection] enum value.
         * @throws IllegalArgumentException if the degree value is not within the valid range of 0 to 360.
         */
        fun Int.mapToWindDirection(): WindDirection {
            return when (this) {
                in 0..22, in 338..360 -> NORTH
                in 23..67 -> NORTH_EAST
                in 68..112 -> EAST
                in 113..157 -> SOUTH_EAST
                in 158..202 -> SOUTH
                in 203..247 -> SOUTH_WEST
                in 248..292 -> WEST
                in 293..337 -> NORTH_WEST
                else -> throw IllegalArgumentException("Invalid wind direction degree: $this")
            }
        }
    }
}

data class TemperatureData(
    val temperature: Double,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val feelsLike: Double
)
