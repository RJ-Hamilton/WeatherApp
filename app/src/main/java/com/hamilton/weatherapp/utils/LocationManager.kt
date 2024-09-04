package com.hamilton.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences

class LocationManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "location_prefs"
        private const val LATITUDE_KEY = "latitude"
        private const val LONGITUDE_KEY = "longitude"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLocation(latitude: Double, longitude: Double) {
        val editor = sharedPreferences.edit()
        editor.putString(LATITUDE_KEY, latitude.toString())
        editor.putString(LONGITUDE_KEY, longitude.toString())
        editor.apply()
    }

    fun getLatitude(): Double {
        val latitudeString = sharedPreferences.getString(LATITUDE_KEY, null)
        return latitudeString?.toDouble() ?: 0.0
    }

    fun getLongitude(): Double {
        val longitudeString = sharedPreferences.getString(LONGITUDE_KEY, null)
        return longitudeString?.toDouble() ?: 0.0
    }
}