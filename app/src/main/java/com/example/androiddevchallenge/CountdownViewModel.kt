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
import kotlin.math.min

class CountdownViewModel: ViewModel() {
    @OptIn(ObsoleteCoroutinesApi::class)
    private val tickerChannel = ticker(100L, 0L, Dispatchers.IO)

    val hours = MutableLiveData(0)
    val minutes = MutableLiveData(0)
    val seconds = MutableLiveData(0)
    val progress = MutableLiveData(0f)
    val editing = MutableLiveData(true)

    private var difference = 0L
    private var aim: Date? = Date()

    init {
        modifySecs(30)
//        start()

        tickerChannel.consumeAsFlow()
            .transform {
                emit(Date())
            }
            .onEach {
                val aim = aim ?: return@onEach

                if(editing.value == true) {
                    hours.postValue(
                        TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS).toInt()
                    )
                    minutes.postValue(
                        TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS).toInt() % 60
                    )
                    seconds.postValue(
                        TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS).toInt() % 60
                    )

                    return@onEach
                }

                val currentDifference = aim.time - it.time

                Log.d("Diff", "$currentDifference")
                val currentProgress = ((this.difference - currentDifference.toFloat()) / this.difference.toFloat())
                Log.d("Diff", "$currentProgress")
                progress.postValue(min(currentProgress, 1f))

                hours.postValue(
                    TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS).toInt()
                )
                minutes.postValue(
                    TimeUnit.MINUTES.convert(currentDifference, TimeUnit.MILLISECONDS).toInt() % 60
                )
                seconds.postValue(
                    TimeUnit.SECONDS.convert(currentDifference, TimeUnit.MILLISECONDS).toInt() % 60
                )

                Log.d("DateTime", "${it}")
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun modifyHours(by: Long) {
        difference += TimeUnit.MILLISECONDS.convert(by, TimeUnit.HOURS)
    }

    fun modifyMins(by: Long) {
        difference += TimeUnit.MILLISECONDS.convert(by, TimeUnit.MINUTES)
    }

    fun modifySecs(by: Long) {
        difference += TimeUnit.MILLISECONDS.convert(by, TimeUnit.SECONDS)
    }

    fun start() {
        Log.d("Start", "$difference")
        aim = Calendar.getInstance().apply {
            add(Calendar.MILLISECOND, difference.toInt())
        }.time
        editing.value = false
    }
}