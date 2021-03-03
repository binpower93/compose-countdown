package com.example.androiddevchallenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import java.util.*
import java.util.concurrent.TimeUnit

class CountdownViewModel: ViewModel() {
    private val tickerChannel = ticker(500L, 0L, Dispatchers.IO)

    val seconds = MutableLiveData(0)

    private val aim: Date = Calendar.getInstance().apply {
        add(Calendar.SECOND, 55)
    }.time

    val currentTime = tickerChannel.consumeAsFlow()
        .transform {
            emit(Date())
        }
        .onEach {
            val difference = aim.time - it.time

            seconds.postValue(
                TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS).toInt()
            )

            Log.d("DateTime", "${it}")
        }
        .launchIn(viewModelScope)
}