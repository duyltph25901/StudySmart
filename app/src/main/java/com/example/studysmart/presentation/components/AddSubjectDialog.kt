package com.example.studysmart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.studysmart.R
import com.example.studysmart.domain.model.Subject

@Composable
fun AddSubjectDialog(
    title: String = stringResource(R.string.add_update_subject),
    isOpen: Boolean,
    selectedColors: List<Color>,
    subjectName: String,
    goalHours: String,
    onSubjectNameChange: (String) -> Unit,
    onGoalHoursChange: (String) -> Unit,
    onDismissRequestEvent: () -> Unit,
    onConfirmEvent: () -> Unit,
    onColorChangeEvent: (List<Color>) -> Unit
) {
    var subjectNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var goalHoursError by rememberSaveable { mutableStateOf<String?>(null) }

    subjectNameError = when {
        subjectName.isBlank() || subjectName.isEmpty() -> stringResource(R.string.please_enter_subject_name)
        subjectName.length < 2 -> stringResource(R.string.subject_name_is_too_short)
        subjectName.length >= 20 -> stringResource(R.string.subject_name_is_too_long)
        else -> null
    }
    goalHoursError = when {
        subjectName.isBlank() || subjectName.isEmpty() -> stringResource(R.string.please_enter_goal_study_hours)
        goalHours.toFloatOrNull() == null -> stringResource(R.string.invalid_number)
        goalHours.toFloat() < 1f -> stringResource(R.string.please_set_at_least_1_hour)
        goalHours.toFloat() > 1000f -> stringResource(R.string.please_set_a_maximum_of_1000_hours)
        else -> null
    }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequestEvent.invoke()
            },
            title = {
                Text(
                    text = title
                )
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Subject.subjectCardColors.forEach { colorGradient ->
                            Box(
                                modifier = Modifier.size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.verticalGradient(colorGradient)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (colorGradient == selectedColors)
                                                    Color.Black else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        onColorChangeEvent.invoke(colorGradient)
                                    }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = { newValue ->
                            onSubjectNameChange.invoke(newValue)
                        }, label = {
                            Text(
                                text = stringResource(R.string.subject_name)
                            )
                        }, singleLine = true,
                        isError = subjectNameError != null && subjectName.isNotBlank(),
                        supportingText = {
                            Text(
                                text = subjectNameError.orEmpty()
                            )
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    OutlinedTextField(
                        value = goalHours,
                        onValueChange = { newValue ->
                            onGoalHoursChange.invoke(newValue)
                        }, label = {
                            Text(
                                text = stringResource(R.string.goal_study_hours)
                            )
                        }, singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ), isError = goalHoursError != null && goalHours.isNotBlank(),
                        supportingText = {
                            Text(
                                text = goalHoursError.orEmpty()
                            )
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmEvent.invoke()
                    }, enabled = subjectNameError == null || goalHoursError == null
                ) {
                    Text(
                        text = stringResource(R.string.save)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequestEvent.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        )
    }
}