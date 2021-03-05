package com.example.androiddevchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max
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

        tickerChannel.consumeAsFlow()
            .transform {
                emit(Date())
            }
            .onEach {
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

                val aim = aim ?: return@onEach
                val currentDifference = aim.time - it.time

                if(currentDifference < 0) {
                    hours.postValue(0)
                    minutes.postValue(0)
                    seconds.postValue(0)
                    progress.postValue(1f)
                    return@onEach
                }

                val currentProgress = ((this.difference - currentDifference.toFloat()) / this.difference.toFloat())
                progress.postValue(min(currentProgress, 1f))

                hours.postValue(
                    TimeUnit.HOURS.convert(currentDifference, TimeUnit.MILLISECONDS).toInt()
                )
                minutes.postValue(
                    TimeUnit.MINUTES.convert(currentDifference, TimeUnit.MILLISECONDS).toInt() % 60
                )
                seconds.postValue(
                    TimeUnit.SECONDS.convert(currentDifference, TimeUnit.MILLISECONDS).toInt() % 60
                )
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
        aim = Calendar.getInstance().apply {
            add(Calendar.MILLISECOND, difference.toInt())
        }.time
        editing.value = false
    }

    fun stop() {
        difference = max(0, (aim ?: Date()).time - Date().time)
        aim = null
        progress.value = 0f
        editing.value = true
    }
}