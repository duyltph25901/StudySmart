package com.example.studysmart.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    state: DatePickerState,
    isOpen: Boolean,
    confirmText: String,
    dismissText: String,
    onDismissEvent: () -> Unit,
    onConfirmEvent: () -> Unit
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = {
                onDismissEvent.invoke()
            }, confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmEvent.invoke()
                    }
                ) {
                    Text(
                        text = confirmText
                    )
                }
            }, dismissButton = {
                TextButton(
                    onClick = {
                        onDismissEvent.invoke()
                    }
                ) {
                    Text(
                        text = dismissText
                    )
                }
            }, content = {
                DatePicker(
                    state = state,
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object FeatureOrPresentSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        utcTimeMillis >= System.currentTimeMillis()

    override fun isSelectableYear(year: Int): Boolean =
        year >= LocalDate.now().year
}