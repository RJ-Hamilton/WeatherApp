package com.hamilton.services.open_weather_map.impl.di

import com.hamilton.services.open_weather_map.api.OpenWeatherMapApi
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val OPEN_WEATHER_MAP_VERSION = "2.5"
private const val BASE_URL = "https://api.openweathermap.org/data/$OPEN_WEATHER_MAP_VERSION"

@Module
object OpenWeatherMapModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val networkJson = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                networkJson.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherMapApi(retrofit: Retrofit): OpenWeatherMapApi {
        return retrofit.create(OpenWeatherMapApi::class.java)
    }
}