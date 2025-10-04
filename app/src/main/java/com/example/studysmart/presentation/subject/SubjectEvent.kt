package com.example.studysmart.presentation.subject

import androidx.compose.ui.graphics.Color
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Task

sealed class SubjectEvent {
    data object UpdateSubject: SubjectEvent()
    data object DeleteSubject: SubjectEvent()
    data object DeleteSession: SubjectEvent()
    data object UpdateProgress: SubjectEvent()
    data class OnTaskIsCompleteChange(val task: Task): SubjectEvent()
    data class OnSubjectCardColorChange(val colors: List<Color>): SubjectEvent()
    data class OnSubjectNameChange(val newName: String): SubjectEvent()
    data class OnGoalStudyHoursChange(val newGoalHours: String): SubjectEvent()
    data class OnDeleteSessionButtonClick(val session: Session): SubjectEvent()
}