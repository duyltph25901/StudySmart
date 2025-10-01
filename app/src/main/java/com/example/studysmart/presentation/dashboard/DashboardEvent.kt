package com.example.studysmart.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Task

sealed class DashboardEvent {
    data object SaveSubject: DashboardEvent()
    data object DeleteSession: DashboardEvent()

    data class OnDeleteSessionEvent(val session: Session): DashboardEvent()
    data class OnTaskIsCompleteChange(val task: Task): DashboardEvent()
    data class OnSubjectCardColorChange(val colors: List<Color>): DashboardEvent()
    data class OnSubjectNameChange(val subjectName: String): DashboardEvent()
    data class OnGoalStudyHoursChange(val goalHours: String): DashboardEvent()
}