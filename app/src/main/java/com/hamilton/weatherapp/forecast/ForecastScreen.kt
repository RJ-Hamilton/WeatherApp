package com.hamilton.weatherapp.forecast

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamilton.weatherapp.R
import com.hamilton.weatherapp.databinding.ForecastItemBinding
import com.hamilton.weatherapp.forecast.models.ForecastItemUiModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(modifier: Modifier = Modifier) {
    val viewModel: ForecastViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getForecast(isRefreshing = false)
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            viewModel.getForecast(
                isRefreshing = true
            )
        }
    ) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                RecyclerView(context).apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = setupGroupieAdapter(
                        state.forecastItemUiModels
                    )
                    addCustomItemDecoration(context)
                }
            },
            update = { view ->
                view.adapter = setupGroupieAdapter(state.forecastItemUiModels)
            }
        )
    }
}

private fun setupGroupieAdapter(
    forecastItemUiModels: List<ForecastItemUiModel>
): GroupAdapter<GroupieViewHolder<ForecastItemBinding>> {
    val adapter = GroupAdapter<GroupieViewHolder<ForecastItemBinding>>()

    forecastItemUiModels.forEach { forecastItemUiModel ->
        adapter.add(
            ForecastItem(
                hiTemp = forecastItemUiModel.highTemp,
                lowTemp = forecastItemUiModel.lowTemp,
                date = forecastItemUiModel.date,
                windSpeed = forecastItemUiModel.windSpeed,
                windDirection = forecastItemUiModel.windDirection,
                weatherDescription = forecastItemUiModel.weatherDescription,
                weatherIconString = forecastItemUiModel.weatherIconString
            )
        )
    }
    return adapter
}

private fun RecyclerView.addCustomItemDecoration(context: Context) {
    val dividerItemDecoration = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    )

    val dividerDrawable = context.getDrawable(R.drawable.divider)
    if (dividerDrawable != null) {
        dividerItemDecoration.setDrawable(dividerDrawable)
    }

    addItemDecoration(dividerItemDecoration)
}