package com.example.androiddevchallenge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import kotlin.concurrent.fixedRateTimer

class CountdownViewModel: ViewModel() {
    private val tickerChannel = ticker(500L, 0L, Dispatchers.IO)

    val currentTime = tickerChannel.consumeAsFlow()
        .onEach {
            Log.d("DateTime", "${Date()}")
        }
        .launchIn(viewModelScope)
}