package com.example.controlica.presentation.components.common.widgets

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateInput(
    selectedDate: kotlinx.datetime.LocalDate?,
    onDateSelected: (kotlinx.datetime.LocalDate) -> Unit,
    placeholder: String = "Selecciona una fecha",
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val formattedDate = selectedDate?.toString() ?: ""

    Box(
        modifier = modifier
            .background(Color.White)
            .border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 16.dp)
            .clickable { showDatePicker = true }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (formattedDate.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = formattedDate,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha",
                tint = Color.Gray
            )
        }
    }

    if (showDatePicker) {

        val millis = selectedDate?.atStartOfDayIn(TimeZone.currentSystemDefault())?.toEpochMilliseconds()

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = millis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val ktxInstant = Instant.fromEpochMilliseconds(selectedMillis)

                        val localDateTime = ktxInstant.toLocalDateTime(TimeZone.currentSystemDefault())
                        val ktxDate = localDateTime.date

                        onDateSelected(ktxDate)
                        showDatePicker = false
                    }
                }) {
                    Text("Seleccionar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


