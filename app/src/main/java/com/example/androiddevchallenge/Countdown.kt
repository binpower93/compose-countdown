package com.example.androiddevchallenge

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountDown(
    modifier: Modifier = Modifier,
    hours: Int,
    minutes: Int,
    seconds: Int,
    progress: Float = 1.0f,
    isEditing: Boolean = true,
    modifyHours: (Long) -> Unit = { },
    modifyMins: (Long) -> Unit = { },
    modifySecs: (Long) -> Unit = { },
    start: () -> Unit = { },
    stop: () -> Unit = { },
) {

    Log.d("Progress", "$progress")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        AnimatedVisibility(visible = !isEditing) {
            CircularProgressIndicator(
                modifier = Modifier.padding(64.dp).fillMaxWidth().aspectRatio(1f),
                progress = progress,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            val showHours = hours > 0 || isEditing
            AnimatedVisibility(showHours) {
                EditableCountDownUnit(
                    isEditing = isEditing,
                    modify = modifyHours,
                    value = hours,
                )
            }
            AnimatedVisibility(showHours) {
                CountdownDivider()
            }

            val showMinutes = showHours || minutes > 0 || isEditing
            AnimatedVisibility(showMinutes) {
                EditableCountDownUnit(
                    isEditing,
                    modifyMins,
                    minutes,
                )
            }
            AnimatedVisibility(showMinutes) {
                CountdownDivider()
            }

            val inProgress = progress < 1
            AnimatedVisibility(visible = inProgress) {
                EditableCountDownUnit(
                    isEditing = isEditing,
                    modify = modifySecs,
                    value = seconds,
                )
            }
            AnimatedVisibility(visible = !inProgress) {
                Text(
                    text = "Finished",
                    style = MaterialTheme.typography.h3,
                    textAlign = TextAlign.Center,
                )

            }
        }
        AnimatedVisibility(visible = isEditing) {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                onClick = { start() },
                enabled = seconds > 0 || minutes > 0 || hours > 0,
            ) {
                Text(text = "Start")
            }
        }
        val isNotFinished = hours > 0 && minutes > 0 && seconds > 0
        AnimatedVisibility(visible = !isEditing && isNotFinished) {
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { stop() },
            ) {
                Text(text = "Stop")
            }
        }
        AnimatedVisibility(visible = !isEditing && !isNotFinished) {
            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { stop() },
            ) {
                Text(text = "Reset")
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun EditableCountDownUnit(
    isEditing: Boolean = true,
    modify: (Long) -> Unit,
    value: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = isEditing) {
            CountDownIncreaseButton(
                onClick = {
                    modify(1)
                },
            )
        }
        CountDownUnit(
            modifier = Modifier.padding(vertical = 8.dp),
            time = value,
        )
        AnimatedVisibility(visible = isEditing) {
            CountDownDecreaseButton(
                onClick = {
                    modify(-1)
                },
                enabled = value > 0
            )
        }
    }
}

@Composable
private fun CountdownDivider() {
    Text(
        modifier = Modifier.padding(16.dp),
        text = ":",
        style = MaterialTheme.typography.h4,
    )
}

@Composable
fun CountDownUnit(modifier: Modifier = Modifier, time: Int) {
    Row(modifier = modifier) {
        CountDownDigit(digit = time / 10)
        Spacer(modifier = Modifier.width(8.dp))
        CountDownDigit(digit = time % 10)
    }
}


@Composable
fun CountDownDigit(digit: Int) {
    Card(
        shape = CutCornerShape(4.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "$digit",
            style = MaterialTheme.typography.h4,
        )
    }
}

@Composable
fun CountDownIncreaseButton(
    onClick: () -> Unit,
) {
    Button(onClick = onClick) {
        Text(text = "+")
    }
}

@Composable
fun CountDownDecreaseButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(onClick = onClick, enabled = enabled) {
        Text(text = "-")
    }
}

@Preview
@Composable
fun CountdownPreview() {
    CountDown(
        hours = 55,
        minutes = 55,
        seconds = 55,
    )
}


@Preview
@Composable
fun CountdownUnitPreview() {
    CountDownUnit(
        time = 15,
    )
}

@Preview
@Composable
fun CountdownDigitPreview() {
    CountDownDigit(digit = 8)
}