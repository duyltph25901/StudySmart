package com.example.studysmart.presentation.subject

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysmart.data.repository.SessionRepository
import com.example.studysmart.data.repository.SubjectRepository
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.presentation.navArgs
import com.example.studysmart.util.toHour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val navArgs: SubjectScreenNavGraphsArgs =
        savedStateHandle.navArgs()
    private val _state =
        MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(navArgs.subjectId),
        taskRepository.getCompletedTasksForSubject(navArgs.subjectId),
        sessionRepository.getRecentTenSessionsForSubject(navArgs.subjectId),
        sessionRepository.getTotalSessionsDurationBySubjectId(navArgs.subjectId),
    ) { state, upcomingTasks, completedTasks, recentSessions, totalSessionsDuration ->
        state.copy(
            upComingTasks = upcomingTasks,
            completedTasks = completedTasks,
            recentSessions = recentSessions,
            studiedHours = totalSessionsDuration.toHour()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000
        ),
        initialValue = SubjectState()
    )

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            SubjectEvent.DeleteSession -> TODO()
            SubjectEvent.DeleteSubject -> TODO()
            is SubjectEvent.OnDeleteSessionButtonClick -> TODO()
            is SubjectEvent.OnGoalStudyHoursChange -> TODO()
            is SubjectEvent.OnSubjectCardColorChange -> TODO()
            is SubjectEvent.OnSubjectNameChange -> TODO()
            is SubjectEvent.OnTaskIsCompleteChange -> TODO()
            SubjectEvent.UpdateSubject -> TODO()
        }
    }

    private fun fetchSubject() = viewModelScope.launch {
        subjectRepository
            .getSubjectById(navArgs.subjectId)?.let { subject ->
                _state.update { it ->
                    it.copy(
                        subjectName = subject.name,
                        goalStudyHours = subject.goalHours.toString(),
                        subjectCardColor = subject.colors.map { color -> Color(color) },
                        currentSubjectId = subject.subjectId
                    )
                }
            }
    }
}