package com.example.studysmart.presentation.dashboard

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysmart.data.repository.SessionRepository
import com.example.studysmart.data.repository.SubjectRepository
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task
import com.example.studysmart.util.SnackBarEvent
import com.example.studysmart.util.toHour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionsDuration()
    ) { state, subjectCount, goalHours, allSubjects, sessionDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = allSubjects,
            totalStudiedHours = sessionDuration.toHour()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000
        ), initialValue = DashboardState()
    )

    val tasks: StateFlow<List<Task>> = taskRepository.getAllUpComingTask()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5000
            ), initialValue = emptyList()
        )

    val recentSessions: StateFlow<List<Session>> = sessionRepository.getRecentFiveSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5000
            ), initialValue = emptyList()
        )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.SaveSubject -> saveSubject()
            DashboardEvent.DeleteSession -> deleteSubject()
            is DashboardEvent.OnDeleteSessionEvent -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }

            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.subjectName
                    )
                }
            }

            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.goalHours
                    )
                }
            }

            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.colors
                    )
                }
            }

            is DashboardEvent.OnTaskIsCompleteChange -> updateTask(event.task)
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.upsertTask(
            task.copy(
                isComplete = !task.isComplete
            )
        )
    }

    private fun saveSubject() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    subjectRepository.upsertSubject(
                        s = Subject(
                            subjectId = 0,
                            name = _state.value.subjectName,
                            goalHours = _state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                            colors = _state.value.subjectCardColors.map { it.toArgb() }
                        )
                    )
                }

                // clear all data before
                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectCardColors.random()
                    )
                }

                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Save Subject!",
                    )
                )
            } catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = e.message ?: "Couldn't save subject",
                    )
                )
            }
        }
    }

    private fun deleteSubject() {

    }

}