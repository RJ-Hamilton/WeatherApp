package com.hamilton.weatherapp.test_helpers

import com.hamilton.services.open_weather_map.api.domain.TemperatureData
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.services.open_weather_map.api.domain.WindData
import com.hamilton.services.open_weather_map.api.domain.WindDirection
import com.hamilton.services.open_weather_map.api.models.City
import com.hamilton.services.open_weather_map.api.models.Clouds
import com.hamilton.services.open_weather_map.api.models.Coord
import com.hamilton.services.open_weather_map.api.models.ForecastResponse
import com.hamilton.services.open_weather_map.api.models.Main
import com.hamilton.services.open_weather_map.api.models.Sys
import com.hamilton.services.open_weather_map.api.models.Weather
import com.hamilton.services.open_weather_map.api.models.WeatherDetails
import com.hamilton.services.open_weather_map.api.models.WeatherResponse
import com.hamilton.services.open_weather_map.api.models.Wind
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal object WeatherTestData {
    val currentDateTime = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()
    )
    val weatherData = WeatherData(
        cityName = "Grand Rapids",
        windData = WindData(
            speed = 28.29,
            direction = WindDirection.SOUTH
        ),
        temperatureData = TemperatureData(
            temperature = 30.31,
            temperatureHigh = 32.33,
            temperatureLow = 34.35,
            feelsLike = 36.37
        ),
        date = currentDateTime,
        weatherIcon = "10d",
        weatherDescription = "cloudy"
    )

    val weatherResponse = WeatherResponse(
        base = "base",
        clouds = Clouds(all = 6573),
        cod = 9171, coord = Coord(
            lat = 66.67,
            lon = 68.69
        ),
        dt = 1725547367,
        id = 3677,
        main = Main(
            feelsLike = 70.71,
            groundLevel = 9240,
            humidity = 2547,
            pressure = 6041,
            seaLevel = 5057,
            temp = 72.73,
            tempMax = 74.75,
            tempMin = 76.77
        ),
        name = "Grand Rapids",
        sys = Sys(
            country = "US",
            id = 3767,
            sunrise = 1733,
            sunset = 1316,
            type = 3785
        ),
        timezone = 7759,
        visibility = 6268,
        weather = listOf(
            Weather(
                description = "cloudy",
                icon = "10d",
                id = 0,
                main = "main"
            )
        ),
        wind = Wind(
            deg = 90,
            gust = 78.79,
            speed = 80.81
        )
    )

    val weatherDetails = WeatherDetails(
        dt = 1725547367,
        main = Main(
            feelsLike = 70.71,
            groundLevel = 9240,
            humidity = 2547,
            pressure = 6041,
            seaLevel = 5057,
            temp = 72.73,
            tempMax = 74.75,
            tempMin = 76.77
        ),
        weather = listOf(
            Weather(
                description = "cloudy",
                icon = "10d",
                id = 0,
                main = "main"
            )
        ),
        clouds = Clouds(all = 0),
        wind = Wind(
            deg = 90,
            gust = 78.79,
            speed = 80.81
        ),
        visibility = 0,
        pop = 0.0,
        dateText = "2024-09-05 18:00:00"
    )

    val forecastResponse = ForecastResponse(
        cod = "",
        message = 0,
        cnt = 0,
        list = listOf(
            weatherDetails,
            weatherDetails
        ),
        city = City(
            id = 0,
            name = "",
            coordinates = Coord(
                lat = 0.0,
                lon = 0.0
            ),
            country = "",
            population = 0,
            timezone = 0,
            sunrise = 0,
            sunset = 0
        )
    )
}