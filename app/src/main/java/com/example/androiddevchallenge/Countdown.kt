package com.example.androiddevchallenge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CountDown(hours: Int, minutes: Int, seconds: Int) {
    Row {
        CountDownUnit(
            time = hours,
            unit = "hours",
        )
        CountdownDivider()
        CountDownUnit(
            time = minutes,
            unit = "minutes",
        )
        CountdownDivider()
        CountDownUnit(
            time = seconds,
            unit = "seconds",
        )
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