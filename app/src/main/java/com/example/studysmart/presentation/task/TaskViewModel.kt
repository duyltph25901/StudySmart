package com.example.studysmart.presentation.task

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studysmart.data.repository.SubjectRepository
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.domain.model.Task
import com.example.studysmart.presentation.navArgs
import com.example.studysmart.util.Priority
import com.example.studysmart.util.SnackBarEvent
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
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val navArgs: TaskScreenNavGraphsArgs =
        savedStateHandle.navArgs()
    private val _state = MutableStateFlow(TaskState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects(),
    ) { state, subjects ->
        state.copy(
            subjects = subjects
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000
        ),
        initialValue = TaskState()
    )
    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        fetchTasks()
        fetchSubject()
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            TaskEvent.DeleteTask -> deleteTask()
            TaskEvent.SaveTask -> saveTask()
            TaskEvent.OnIsCompleteChange -> {
                _state.update {
                    it.copy(
                        isTaskComplete = !_state.value.isTaskComplete
                    )
                }
            }

            is TaskEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            is TaskEvent.OnDueDateChange -> {
                _state.update {
                    it.copy(
                        dueDate = event.dueDate
                    )
                }
            }

            is TaskEvent.OnPriorityChange -> {
                _state.update {
                    it.copy(
                        priority = event.priority
                    )
                }
            }

            is TaskEvent.OnRelatedSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId
                    )
                }
            }

            is TaskEvent.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
        }
    }

    private fun saveTask() = viewModelScope.launch {
        try {
            val state = _state.value
            Log.d("duylt", "saveTask\n\tstate.subjectId: ${state.subjectId}\n\tstate.relatedToSubject: ${state.relatedToSubject}")
            if (state.subjectId == null || state.relatedToSubject == null) {
                _snackBarEvent.emit(
                    SnackBarEvent.ShowSnackBar(
                        message = "Please select subject related to the task.",
                        duration = SnackbarDuration.Long
                    )
                )
                return@launch
            }

            val task = Task(
                taskId = state.currentTaskId ?: 0,
                taskSubjectId = state.subjectId,
                title = state.title,
                description = state.description,
                duDate = state.dueDate ?: Instant.now().toEpochMilli(),
                priority = state.priority.value,
                relatedToSubject = state.relatedToSubject,
                isComplete = state.isTaskComplete
            )
            withContext(Dispatchers.IO) {
                taskRepository.upsertTask(task)
            }

            _snackBarEvent.emit(
                SnackBarEvent.NavigateUp
            )
        } catch (e: Exception) {
            e.printStackTrace()

            _snackBarEvent.emit(
                SnackBarEvent.ShowSnackBar(
                    message = "Couldn't save task. ${e.message}",
                    duration = SnackbarDuration.Long
                )
            )
        }
    }

    private fun deleteTask() = viewModelScope.launch {
        _state.value.currentTaskId?.let {
            withContext(Dispatchers.IO) {
                try {
                    taskRepository.deleteTaskById(it)

                    _snackBarEvent.emit(
                        SnackBarEvent.NavigateUp
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    _snackBarEvent.emit(
                        SnackBarEvent.ShowSnackBar(
                            message = "Couldn't delete task. ${e.message}",
                            duration = SnackbarDuration.Long
                        )
                    )
                }
            }
        } ?: run {
            _snackBarEvent.emit(
                SnackBarEvent.ShowSnackBar(
                    message = "Task id is null",
                    duration = SnackbarDuration.Long
                )
            )
        }
    }

    private fun fetchTasks() = viewModelScope.launch {
        navArgs.taskId?.let {
            taskRepository.getTaskById(it)?.let { task ->
                _state.update { state ->
                    state.copy(
                        title = task.title,
                        description = task.description,
                        dueDate = task.duDate,
                        isTaskComplete = task.isComplete,
                        relatedToSubject = task.relatedToSubject,
                        priority = Priority.fromInt(task.priority),
                        subjectId = task.taskSubjectId,
                        currentTaskId = task.taskId
                    )
                }
            }
        }
    }

    private fun fetchSubject() = viewModelScope.launch {
        navArgs.subjectId?.let { id ->
            subjectRepository.getSubjectById(id)?.let { subject ->
                _state.update { state ->
                    state.copy(
                        subjectId = subject.subjectId,
                        relatedToSubject = subject.name
                    )
                }
            }
        }
    }
}