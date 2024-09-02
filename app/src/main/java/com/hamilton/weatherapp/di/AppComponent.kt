package com.hamilton.weatherapp.di

import com.hamilton.services.open_weather_map.impl.di.OpenWeatherMapModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        OpenWeatherMapModule::class
    ]
)
interface AppComponent