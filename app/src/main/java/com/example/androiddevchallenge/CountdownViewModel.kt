package com.example.androiddevchallenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.TimeUnit

class CountdownViewModel: ViewModel() {
    @OptIn(ObsoleteCoroutinesApi::class)
    private val tickerChannel = ticker(500L, 0L, Dispatchers.IO)

    val seconds = MutableLiveData(0)
    val minutes = MutableLiveData(0)

    private val aim: Date = Calendar.getInstance().apply {
        add(Calendar.MINUTE, 1)
        add(Calendar.SECOND, 5)
    }.time

    init {

        tickerChannel.consumeAsFlow()
            .transform {
                emit(Date())
            }
            .onEach {
                val difference = aim.time - it.time

                minutes.postValue(
                    TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS).toInt() % 60
                )
                seconds.postValue(
                    TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS).toInt() % 60
                )

                Log.d("DateTime", "${it}")
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }
}