package com.hamilton.weatherapp.forecast

import android.view.View
import coil.load
import com.hamilton.weatherapp.R
import com.hamilton.weatherapp.databinding.ForecastItemBinding
import com.xwray.groupie.viewbinding.BindableItem

class ForecastItem(
    private val hiTemp: Int,
    private val lowTemp: Int,
    private val date: String,
    private val windSpeed: Int,
    private val windDirection: String,
    private val weatherDescription: String,
    private val weatherIconString: String
) : BindableItem<ForecastItemBinding>() {
    override fun bind(viewBinding: ForecastItemBinding, position: Int) {
        val context = viewBinding.root.context

        viewBinding.textHiTemp.text =
            context.getString(R.string.forecast_screen_temperature_content_hi_temp, hiTemp)
        viewBinding.textLowTemp.text =
            context.getString(R.string.forecast_screen_temperature_content_low_temp, lowTemp)
        viewBinding.textDate.text = date
        viewBinding.textWindSpeedTitle.setText(R.string.forecast_screen_wind_speed_title)
        viewBinding.textWindSpeed.text =
            context.getString(R.string.forecast_screen_wind_speed, windSpeed)
        viewBinding.textWindDirection.text = windDirection
        viewBinding.textWeatherDescription.text = weatherDescription
        viewBinding.imageWeatherIcon.load(
            context.getString(
                R.string.weather_icon_url,
                weatherIconString
            )
        ) {
            placeholder(R.drawable.ic_launcher_foreground) // Optional placeholder image
            error(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getLayout(): Int {
        return R.layout.forecast_item
    }

    override fun initializeViewBinding(view: View): ForecastItemBinding {
        return ForecastItemBinding.bind(view)
    }
}