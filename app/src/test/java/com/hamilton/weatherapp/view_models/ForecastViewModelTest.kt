package com.hamilton.weatherapp.view_models

import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.weatherapp.forecast.ForecastScreenState
import com.hamilton.weatherapp.forecast.ForecastViewModel
import com.hamilton.weatherapp.forecast.models.ForecastItemUiModelMapper
import com.hamilton.weatherapp.test_helpers.WeatherTestData.weatherData
import com.hamilton.weatherapp.utils.LocationManager
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {
    private lateinit var viewModel: ForecastViewModel
    private val mockWeatherRepository: WeatherRepository = mockk()
    private val mockLocationManager: LocationManager = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        viewModel = ForecastViewModel(
            weatherRepository = mockWeatherRepository,
            locationManager = mockLocationManager,
            coroutineDispatcher = testDispatcher
        )
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getForecast should update UI state with correct data`() = runTest {
        // GIVEN
        val listOfWeatherData = listOf(weatherData, weatherData, weatherData)

        coEvery {
            mockWeatherRepository.getDailyForecast(any(), any())
        } returns listOfWeatherData

        every {
            mockLocationManager.getLatitude()
        } returns 37.7749

        every {
            mockLocationManager.getLongitude()
        } returns -122.4194

        val testResults = mutableListOf<ForecastScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getForecast()

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = ForecastScreenState()
        testResults[1].run {
            assertEquals(true, isLoading)
        }
        verify(exactly = 1) {
            mockLocationManager.getLatitude()
        }
        verify(exactly = 1) {
            mockLocationManager.getLongitude()
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getDailyForecast(37.7749, -122.4194)
        }
        assertEquals(
            ForecastScreenState(
                isLoading = false,
                isRefreshing = false,
                errorMessage = null,
                forecastItemUiModels = listOfWeatherData.map {
                    ForecastItemUiModelMapper.mapFromWeatherData(it)
                }
            ),
            testResults[2]
        )
    }

    @Test
    fun `getForecast with isRefreshing should update UI state with correct data`() = runTest {
        // GIVEN
        val listOfWeatherData = listOf(weatherData, weatherData, weatherData)

        coEvery {
            mockWeatherRepository.getDailyForecast(any(), any())
        } returns listOfWeatherData

        every {
            mockLocationManager.getLatitude()
        } returns 37.7749

        every {
            mockLocationManager.getLongitude()
        } returns -122.4194

        val testResults = mutableListOf<ForecastScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getForecast(isRefreshing = true)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = ForecastScreenState()
        testResults[1].run {
            assertEquals(false, isLoading)
            assertEquals(true, isRefreshing)
        }
        verify(exactly = 1) {
            mockLocationManager.getLatitude()
        }
        verify(exactly = 1) {
            mockLocationManager.getLongitude()
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getDailyForecast(37.7749, -122.4194)
        }
        assertEquals(
            ForecastScreenState(
                isLoading = false,
                isRefreshing = false,
                errorMessage = null,
                forecastItemUiModels = listOfWeatherData.map {
                    ForecastItemUiModelMapper.mapFromWeatherData(it)
                }
            ),
            testResults[2]
        )
    }

    @Test
    fun `getForecast should handle exceptions and update state correctly`() = runTest {
        // GIVEN
        coEvery {
            mockWeatherRepository.getDailyForecast(any(), any())
        } throws RuntimeException("Error")

        every {
            mockLocationManager.getLatitude()
        } returns 37.7749

        every {
            mockLocationManager.getLongitude()
        } returns -122.4194

        val testResults = mutableListOf<ForecastScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getForecast(isRefreshing = true)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = ForecastScreenState()
        testResults[1].run {
            assertEquals(false, isLoading)
            assertEquals(true, isRefreshing)
        }
        verify(exactly = 1) {
            mockLocationManager.getLatitude()
        }
        verify(exactly = 1) {
            mockLocationManager.getLongitude()
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getDailyForecast(37.7749, -122.4194)
        }
        assertEquals(
            ForecastScreenState(
                isLoading = false,
                isRefreshing = false
            ),
            testResults[2]
        )
    }
}