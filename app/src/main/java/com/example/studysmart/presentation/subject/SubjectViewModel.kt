package com.example.studysmart.presentation.subject

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysmart.data.repository.SessionRepository
import com.example.studysmart.data.repository.SubjectRepository
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.presentation.navArgs
import com.example.studysmart.util.SnackBarEvent
import com.example.studysmart.util.toHour
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

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
            SubjectEvent.UpdateProgress -> {
                val goalStudyHours =
                    _state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (_state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }
            SubjectEvent.DeleteSession -> TODO()
            SubjectEvent.DeleteSubject -> deleteSubject()
            SubjectEvent.UpdateSubject -> updateSubject()
            is SubjectEvent.OnDeleteSessionButtonClick -> TODO()
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.newGoalHours
                    )
                }
            }

            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColor = event.colors
                    )
                }
            }

            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.newName
                    )
                }
            }

            is SubjectEvent.OnTaskIsCompleteChange -> {
                viewModelScope.launch(Dispatchers.IO) {
                    taskRepository.upsertTask(
                        event.task.copy(
                            isComplete = !event.task.isComplete
                        )
                    )
                }
            }
        }
    }

    private fun deleteSubject() = viewModelScope.launch {
        try {
            _state.value.currentSubjectId?.let {
                withContext(Dispatchers.IO) {
                    subjectRepository.deleteSubjectById(it)
                }
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Subject deleted successfully!",
                    )
                )
                _snackBarEventFlow.emit(
                    SnackBarEvent.NavigateUp
                )
            } ?: run {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Subject id is null",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _snackBarEventFlow.emit(
                SnackBarEvent.ShowSnackBar(
                    message = "Couldn't delete subject. ${e.message}",
                    duration = SnackbarDuration.Long
                )
            )
        }
    }

    private fun updateSubject() = viewModelScope.launch {
        try {
            withContext(Dispatchers.IO) {
                subjectRepository.upsertSubject(
                    Subject(
                        subjectId = _state.value.currentSubjectId ?: 0,
                        name = _state.value.subjectName,
                        goalHours = _state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = _state.value.subjectCardColor.map { it.toArgb() }
                    )
                )
            }

            _snackBarEventFlow.emit(
                SnackBarEvent.ShowSnackBar(
                    message = "Subject updated successfully!",
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            _snackBarEventFlow.emit(
                SnackBarEvent.ShowSnackBar(
                    message = "Couldn't update subject. ${e.message}",
                    duration = SnackbarDuration.Long
                )
            )
        }
    }

    private fun fetchSubject() = viewModelScope.launch(Dispatchers.IO) {
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