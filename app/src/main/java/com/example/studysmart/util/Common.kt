package com.example.studysmart.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task
import com.example.studysmart.presentation.theme.Green
import com.example.studysmart.presentation.theme.Orange
import com.example.studysmart.presentation.theme.Red
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class Priority(
    val title: String,
    val color: Color,
    val value: Int,
) {
    LOW(title = "Low", color = Green, value = 0),
    MEDIUM(title = "Medium", color = Orange, value = 1),
    HIGH(title = "High", color = Red, value = 2);

    companion object {
        fun fromInt(value: Int) =
            Priority.entries.firstOrNull { it.value == value } ?: MEDIUM
    }
}

val dummySubjectData = listOf(
    Subject(0, "English", 10f, Subject.subjectCardColors[0].map { it.toArgb() }),
    Subject(1, "Physic", 10f, Subject.subjectCardColors[1].map { it.toArgb() }),
    Subject(2, "Math", 10f, Subject.subjectCardColors[2].map { it.toArgb() }),
    Subject(3, "Fine Arts", 10f, Subject.subjectCardColors[3].map { it.toArgb() }),
    Subject(4, "Music", 10f, Subject.subjectCardColors[4].map { it.toArgb() }),
)

val dummyTasksData = listOf(
    Task(0, 0, "Prepare Note", "", 0L, Priority.MEDIUM.value, "", false),
    Task(1, 1, "Prepare Note", "", 0L, Priority.HIGH.value, "", true),
    Task(2, 2, "Prepare Note", "", 0L, Priority.LOW.value, "", true),
    Task(3, 3, "Prepare Note", "", 0L, Priority.MEDIUM.value, "", false),
)

val dummyTaskSession = listOf(
    Session(0, "English", 0L, 0L, 0),
    Session(0, "English", 0L, 0L, 0),
    Session(0, "English", 0L, 0L, 0),
    Session(0, "English", 0L, 0L, 0),
    Session(0, "English", 0L, 0L, 0),
    Session(0, "English", 0L, 0L, 0),
)

fun Long?.changeMillisToDateString(): String {
    val date = this?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } ?: LocalDate.now()

    return date?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: ""
}

fun Long.toHour(): Float {
    val hour = this.toFloat() / 3600f
    return "%.2f".format(hour).toFloat()
}

sealed class SnackBarEvent() {
    data class ShowSnackBar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ): SnackBarEvent()

    data object NavigateUp: SnackBarEvent()
}