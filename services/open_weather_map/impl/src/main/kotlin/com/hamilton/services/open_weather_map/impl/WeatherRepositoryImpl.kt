package com.hamilton.services.open_weather_map.impl

import com.hamilton.services.open_weather_map.api.OpenWeatherMapApi
import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.services.open_weather_map.api.domain.TemperatureData
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.services.open_weather_map.api.domain.WeatherDataMapper
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class WeatherRepositoryImpl(
    private val openWeatherMapApi: OpenWeatherMapApi
) : WeatherRepository {
    override suspend fun getCurrentWeather(lat: Double, long: Double): WeatherData {
        val response = openWeatherMapApi.getCurrentWeather(
            lat = lat,
            long = long
        )

        return response.body()?.let {
            WeatherDataMapper.mapFromWeatherResponse(it)
        } ?: throw RuntimeException(response.errorBody().toString())
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

        return response.body()?.let {
            it.list.map { weatherDetails ->
                WeatherDataMapper.mapFromWeatherDetails(weatherDetails)
            }
        } ?: throw RuntimeException(response.errorBody().toString())
    }

    override suspend fun getDailyForecast(
        lat: Double,
        long: Double
    ): List<WeatherData> {
        val response = openWeatherMapApi.getForecastWeather(
            lat = lat,
            long = long
        )

        val weatherDataList = response.body()?.let {
            it.list.map { weatherDetails ->
                WeatherDataMapper.mapFromWeatherDetails(weatherDetails)
            }
        } ?: throw RuntimeException(response.errorBody().toString())

        val groupedWeatherData = weatherDataList.drop(1).groupBy { it.date.date }

        return groupedWeatherData.map { (date, listOfWeatherData) ->
            WeatherData(
                cityName = "",
                windData = listOfWeatherData.maxByOrNull { it.windData.speed }?.windData
                    ?: listOfWeatherData.first().windData,
                temperatureData = TemperatureData(
                    temperature = 0.0,
                    temperatureHigh = findHighestValue(
                        listOfWeatherData.map {
                            it.temperatureData.temperatureHigh
                        }
                    ),
                    temperatureLow = findLowestValue(
                        listOfWeatherData.map {
                            it.temperatureData.temperatureLow
                        }
                    ),
                    feelsLike = 0.0
                ),
                date = LocalDateTime(date = date, time = LocalTime(hour = 12, minute = 0)),
                weatherIcon = findMostCommonType(
                    listOfWeatherData.map { it.weatherIcon }
                ),
                weatherDescription = findMostCommonType(
                    listOfWeatherData.map { it.weatherDescription }
                )
            )
        }
    }

    /**
     * Finds and returns the most common element in the given list. If there are ties for the most
     * common element, the first such element encountered will be returned. If the list is empty,
     * the first element in the list will be returned.
     *
     * @param listOfType The list of elements of any type `T` from which the most common element
     * will be found.
     * @return The most common element in the list. If there are multiple elements with the same
     * highest frequency, the first encountered is returned. If the list is empty,
     * `listOfType.first()` is returned.
     */
    private fun <T> findMostCommonType(listOfType: List<T>): T {
        return listOfType
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }?.key ?: listOfType.first()
    }

    private fun findHighestValue(listOfType: List<Double>): Double {
        return listOfType.maxOrNull() ?: listOfType.first()
    }

    private fun findLowestValue(listOfType: List<Double>): Double {
        return listOfType.minOrNull() ?: listOfType.first()
    }
}