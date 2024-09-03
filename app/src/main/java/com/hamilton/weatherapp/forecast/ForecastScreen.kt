package com.hamilton.weatherapp.forecast

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamilton.weatherapp.R

@Composable
fun ForecastScreen(modifier: Modifier = Modifier) {
    val viewModel: ForecastViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            val view = LayoutInflater.from(context).inflate(R.layout.my_layout, null, false)
//            // do whatever you want...
//            view // return the view
//        },
//        update = { view ->
//            // Update the view if needed
//        }
//    )
}