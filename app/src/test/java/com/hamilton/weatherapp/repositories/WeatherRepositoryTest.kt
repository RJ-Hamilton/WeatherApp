package com.hamilton.weatherapp.repositories

import com.hamilton.services.open_weather_map.api.OpenWeatherMapApi
import com.hamilton.services.open_weather_map.api.domain.WeatherDataMapper
import com.hamilton.services.open_weather_map.impl.WeatherRepositoryImpl
import com.hamilton.weatherapp.test_helpers.WeatherTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryImplTest {

    private val mockOpenWeatherMapApi = mockk<OpenWeatherMapApi>()
    private val weatherRepository = WeatherRepositoryImpl(mockOpenWeatherMapApi)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getCurrentWeather should return WeatherData when API call is successful`() = runTest {
        // GIVEN
        val weatherResponse = WeatherTestData.weatherResponse

        coEvery {
            mockOpenWeatherMapApi.getCurrentWeather(
                lat = any(),
                long = any()
            )
        } returns Response.success(weatherResponse)

        mockkObject(WeatherDataMapper)

        every {
            WeatherDataMapper.mapFromWeatherResponse(weatherResponse)
        } returns WeatherTestData.weatherData

        // WHEN
        val result = weatherRepository.getCurrentWeather(37.7749, -122.4194)

        // THEN
        assertEquals(WeatherTestData.weatherData, result)
    }

    @Test
    fun `getCurrentWeather should throw RuntimeException when API response is unsuccessful`() =
        runTest {
            // GIVEN
            val errorBody = "Error occurred".toResponseBody()

            coEvery {
                mockOpenWeatherMapApi.getCurrentWeather(lat = any(), long = any())
            } throws RuntimeException(errorBody.string())

            // WHEN & THEN
            val exception = assertFailsWith<RuntimeException> {
                weatherRepository.getCurrentWeather(37.7749, -122.4194)
            }
            assertEquals("Error occurred", exception.message)
        }

    @Test
    fun `getForecastWeather should return list of WeatherData when API call is successful`() =
        runTest {
            // GIVEN
            val forecastResponse = WeatherTestData.forecastResponse

            coEvery {
                mockOpenWeatherMapApi.getForecastWeather(
                    lat = any(),
                    long = any(),
                    numberOfTimestamps = any()
                )
            } returns Response.success(forecastResponse)

            mockkObject(WeatherDataMapper)

            every {
                WeatherDataMapper.mapFromWeatherDetails(WeatherTestData.weatherDetails)
            } returns WeatherTestData.weatherData

            // WHEN
            val result = weatherRepository.getForecastWeather(
                lat = 37.7749,
                long = -122.4194,
                numberOfTimeStamps = 5
            )

            // THEN
            assertEquals(listOf(WeatherTestData.weatherData, WeatherTestData.weatherData), result)
        }

    @Test
    fun `getForecastWeather should throw RuntimeException when API response is unsuccessful`() =
        runTest {
            // GIVEN
            val errorBody = "Error occurred".toResponseBody()

            coEvery {
                mockOpenWeatherMapApi.getForecastWeather(
                    lat = any(),
                    long = any(),
                    numberOfTimestamps = any()
                )
            } throws RuntimeException(errorBody.string())

            // WHEN & THEN
            val exception = assertFailsWith<RuntimeException> {
                weatherRepository.getForecastWeather(37.7749, -122.4194, 2)
            }
            assertEquals("Error occurred", exception.message)
        }
}
