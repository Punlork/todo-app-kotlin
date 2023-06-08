package com.todo.app.ui.app.compose

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
 fun showDatePicker(context: Context, selectedDate: MutableState<LocalDate>) {
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate.value = LocalDate.of(year, month + 1, dayOfMonth)
        },
        selectedDate.value.year, selectedDate.value.monthValue - 1, selectedDate.value.dayOfMonth,
    )
    datePicker.show()
}

@RequiresApi(Build.VERSION_CODES.O)
private fun showTimePicker(context: Context, selectedTime: MutableState<LocalTime>) {
    val timePicker = TimePickerDialog(
        context, { _, hourOfDay, minute ->
            selectedTime.value = LocalTime.of(hourOfDay, minute)
        }, selectedTime.value.hour, selectedTime.value.minute, false
    )
    timePicker.show()
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomSheetContent(focusRequester: FocusRequester) {
    val task = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    val formattedTime = selectedTime.value.format(timeFormatter)
    val formattedDate = selectedDate.value.format(dateFormatter)

    val localContext = LocalContext.current

    val isLoading = remember { mutableStateOf(false) }

    val onClicked: () -> Unit = {
        isLoading.value = true

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        OutlinedTextField(
            value = "Date: $formattedDate",
            onValueChange = {},
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledBorderColor = Color.Black, disabledTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clickable {
                    showDatePicker(localContext, selectedDate)
                },
        )

        OutlinedTextField(
            value = "Time: $formattedTime",
            onValueChange = {},
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledBorderColor = Color.Black, disabledTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clickable {
                    showTimePicker(localContext, selectedTime)
                },
        )

        OutlinedTextField(
            value = task.value,
            onValueChange = {
                task.value = it
            },
            trailingIcon = {
                if (isLoading.value) CircularProgressIndicator(modifier = Modifier.size(15.dp)) else Text(
                    "Send",
                    style = MaterialTheme.typography.labelLarge,
                    color = if (task.value === "") Color.Gray else Color.Black,
                    modifier = Modifier.clickable(
                        enabled = task.value !== "",
                        onClick = onClicked,
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )
    }
}