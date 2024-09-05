package com.hamilton.weatherapp.view_models

import com.hamilton.services.open_weather_map.api.WeatherRepository
import com.hamilton.services.open_weather_map.api.domain.WeatherData
import com.hamilton.weatherapp.landing.LandingScreenState
import com.hamilton.weatherapp.landing.LandingViewModel
import com.hamilton.weatherapp.landing.LatLong
import com.hamilton.weatherapp.landing.models.CurrentWeatherUiModelMapper
import com.hamilton.weatherapp.landing.models.HourlyWeatherUiModelMapper
import com.hamilton.weatherapp.test_helpers.WeatherTestData.weatherData
import com.hamilton.weatherapp.utils.LocationManager
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
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
class LandingViewModelTest {
    private lateinit var viewModel: LandingViewModel
    private val mockWeatherRepository: WeatherRepository = mockk()
    private val mockLocationManager: LocationManager = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        viewModel = LandingViewModel(
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
    fun `getCurrentWeather should update UI state with correct data`() = runTest {
        // GIVEN
        coEvery {
            mockWeatherRepository.getCurrentWeather(any(), any())
        } answers {
            weatherData
        }

        val testResults = mutableListOf<LandingScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getCurrentWeather(37.7749, -122.4194)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = LandingScreenState()
        testResults[1].run {
            assertEquals(true, isLoading)
        }
        verify(exactly = 1) {
            mockLocationManager.saveLocation(37.7749, -122.4194)
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getCurrentWeather(37.7749, -122.4194)
        }
        assertEquals(
            LandingScreenState(
                isLoading = false,
                isRefreshing = false,
                errorMessage = null,
                latLong = LatLong(latitude = 37.7749, longitude = -122.4194),
                currentWeatherUiModel = CurrentWeatherUiModelMapper.fromWeatherData(
                    weatherData = weatherData
                ),
                hourlyWeatherUiModels = emptyList()
            ),
            testResults[2]
        )
    }

    @Test
    fun `getCurrentWeather with isRefreshing should update UI state with correct data`() = runTest {
        // GIVEN
        coEvery {
            mockWeatherRepository.getCurrentWeather(any(), any())
        } answers {
            weatherData
        }

        val testResults = mutableListOf<LandingScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getCurrentWeather(37.7749, -122.4194, isRefreshing = true)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = LandingScreenState()
        testResults[1].run {
            assertEquals(false, isLoading)
            assertEquals(true, isRefreshing)
        }
        verify(exactly = 0) {
            mockLocationManager.saveLocation(37.7749, -122.4194)
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getCurrentWeather(37.7749, -122.4194)
        }
        assertEquals(
            LandingScreenState(
                isLoading = false,
                isRefreshing = false,
                errorMessage = null,
                latLong = LatLong(latitude = 37.7749, longitude = -122.4194),
                currentWeatherUiModel = CurrentWeatherUiModelMapper.fromWeatherData(
                    weatherData = weatherData
                ),
                hourlyWeatherUiModels = emptyList()
            ),
            testResults[2]
        )
    }

    @Test
    fun `getCurrentWeather should handle exceptions and update state correctly`() = runTest {
        // GIVEN
        coEvery {
            mockWeatherRepository.getCurrentWeather(any(), any())
        } throws RuntimeException("Error")

        val testResults = mutableListOf<LandingScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getCurrentWeather(37.7749, -122.4194)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = LandingScreenState()
        testResults[1].run {
            assertEquals(true, isLoading)
        }
        verify(exactly = 1) {
            mockLocationManager.saveLocation(37.7749, -122.4194)
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getCurrentWeather(37.7749, -122.4194)
        }
        testResults[2].run {
            assertEquals(false, isRefreshing)
            assertEquals(false, isLoading)
        }
    }

    @Test
    fun `getForecastWeather should update UI state with forecast data`() = runTest {
        // GIVEN
        val listOfWeatherData = listOf(weatherData, weatherData, weatherData)
        coEvery {
            mockWeatherRepository.getForecastWeather(any(), any())
        } returns listOfWeatherData

        val testResults = mutableListOf<LandingScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getForecastWeather(37.7749, -122.4194)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = LandingScreenState()
        testResults[1].run {
            assertEquals(true, isLoading)
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getForecastWeather(any(), any())
        }
        assertEquals(
            LandingScreenState(
                isLoading = false,
                isRefreshing = false,
                errorMessage = null,
                latLong = LatLong(latitude = 0.0, longitude = 0.0),
                hourlyWeatherUiModels = listOfWeatherData.map {
                    HourlyWeatherUiModelMapper.fromWeatherData(it)
                }
            ),
            testResults[2]
        )
    }

    @Test
    fun `getForecastWeather with isRefreshing should update UI state with forecast data`() =
        runTest {
            // GIVEN
            val listOfWeatherData = listOf(weatherData, weatherData, weatherData)
            coEvery {
                mockWeatherRepository.getForecastWeather(any(), any())
            } returns listOfWeatherData

            val testResults = mutableListOf<LandingScreenState>()

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.toList(testResults)
            }

            // WHEN
            viewModel.getForecastWeather(37.7749, -122.4194, isRefreshing = true)

            // THEN
            assertEquals(3, testResults.size)
            testResults[0] = LandingScreenState()
            testResults[1].run {
                assertEquals(false, isLoading)
                assertEquals(true, isRefreshing)
            }
            coVerify(exactly = 1) {
                mockWeatherRepository.getForecastWeather(any(), any())
            }
            assertEquals(
                LandingScreenState(
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = null,
                    latLong = LatLong(latitude = 0.0, longitude = 0.0),
                    hourlyWeatherUiModels = listOfWeatherData.map {
                        HourlyWeatherUiModelMapper.fromWeatherData(it)
                    }
                ),
                testResults[2]
            )
        }

    @Test
    fun `getForecastWeather should handle exceptions and update state correctly`() = runTest {
        // GIVEN
        coEvery {
            mockWeatherRepository.getForecastWeather(any(), any())
        } throws RuntimeException("Error")

        val testResults = mutableListOf<LandingScreenState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(testResults)
        }

        // WHEN
        viewModel.getForecastWeather(37.7749, -122.4194)

        // THEN
        assertEquals(3, testResults.size)
        testResults[0] = LandingScreenState()
        testResults[1].run {
            assertEquals(true, isLoading)
        }
        coVerify(exactly = 1) {
            mockWeatherRepository.getForecastWeather(any(), any())
        }
        assertEquals(
            LandingScreenState(
                isLoading = false,
                isRefreshing = false
            ),
            testResults[2]
        )
    }

    @Test
    fun `fetchData should call both getCurrentWeather and getForecastWeather`() = runTest {
        // GIVEN
        val mockWeatherData = mockk<WeatherData>()
        coEvery { mockWeatherRepository.getCurrentWeather(any(), any()) } returns mockWeatherData
        coEvery { mockWeatherRepository.getForecastWeather(any(), any()) } returns emptyList()

        // WHEN
        viewModel.fetchData(37.7749, -122.4194)

        // THEN
        coVerify(exactly = 1) { mockWeatherRepository.getCurrentWeather(37.7749, -122.4194) }
        coVerify(exactly = 1) { mockWeatherRepository.getForecastWeather(37.7749, -122.4194) }
    }
}
