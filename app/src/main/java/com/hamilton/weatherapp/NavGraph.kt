package com.hamilton.weatherapp

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamilton.weatherapp.forecast.ForecastScreen
import com.hamilton.weatherapp.landing.LandingScreen
import com.hamilton.weatherapp.ui.FORECAST_BUTTON_INDEX
import com.hamilton.weatherapp.ui.HOME_BUTTON_INDEX
import com.hamilton.weatherapp.ui.WeatherBottomNavBar
import com.hamilton.weatherapp.utils.Transitions
import kotlinx.serialization.Serializable

@Serializable
object LandingScreen

@Serializable
object ForecastScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            WeatherBottomNavBar(
                onItemSelected = { newSelectedIndex ->
                    when (newSelectedIndex) {
                        HOME_BUTTON_INDEX -> setupBottomNavigation(
                            route = LandingScreen,
                            navController = navController
                        )

                        FORECAST_BUTTON_INDEX -> setupBottomNavigation(
                            route = ForecastScreen,
                            navController = navController
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = LandingScreen,
            enterTransition = Transitions.EnterLeft,
            exitTransition = Transitions.ExitLeft,
            popEnterTransition = Transitions.EnterRight,
            popExitTransition = Transitions.ExitRight
        ) {
            composable<LandingScreen> {
                BackHandler(enabled = true) {
                    context as ComponentActivity
                    context.finish()
                }
                LandingScreen()
            }
            composable<ForecastScreen> {
                BackHandler(enabled = true) {
                    context as ComponentActivity
                    context.finish()
                }
                ForecastScreen()
            }
        }
    }
}

private fun setupBottomNavigation(route: Any, navController: NavController) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}