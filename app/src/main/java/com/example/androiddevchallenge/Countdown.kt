package com.example.androiddevchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountDown(modifier: Modifier = Modifier, hours: Int, minutes: Int, seconds: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {

        val showHours = hours > 0
        AnimatedVisibility(showHours) {
            CountDownUnit(
                time = hours,
                unit = "hours",
            )
        }
        AnimatedVisibility(showHours) {
            CountdownDivider()
        }

        val showMinutes = showHours || minutes > 0
        AnimatedVisibility(showMinutes) {
            CountDownUnit(
                time = minutes,
                unit = "minutes",
            )
        }
        AnimatedVisibility(showMinutes) {
            CountdownDivider()
        }

        val completed = showMinutes || seconds > 0
        AnimatedVisibility(visible = completed) {
            CountDownUnit(
                time = seconds,
                unit = "seconds",
            )
        }
        AnimatedVisibility(visible = !completed) {
            Text(
                text = "Finished",
                style = MaterialTheme.typography.h3
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
fun CountDownUnit(time: Int, unit: String) {
    Column {
        Row {
            CountDownDigit(digit = time / 10)
            Spacer(modifier = Modifier.width(8.dp))
            CountDownDigit(digit = time % 10)
        }

        Text(
            text = unit,
            style = MaterialTheme.typography.h6,
        )
    }
}

@Composable
fun CountDownDigit(digit: Int) {
    Crossfade(targetState = digit, animationSpec = spring()) { digit ->
        Card(
            shape = CutCornerShape(4.dp)
        )
        {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "$digit",
                style = MaterialTheme.typography.h4,
            )
        }
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
        unit = "Mins",
    )
}

@Preview
@Composable
fun CountdownDigitPreview() {
    CountDownDigit(digit = 8)
}