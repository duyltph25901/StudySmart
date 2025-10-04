package com.example.studysmart.presentation.task

import com.example.studysmart.domain.model.Subject
import com.example.studysmart.util.Priority

sealed class TaskEvent {
    data class OnTitleChange(val title: String): TaskEvent()
    data class OnDescriptionChange(val description: String): TaskEvent()
    data class OnDueDateChange(val dueDate: Long?): TaskEvent()
    data class OnPriorityChange(val priority: Priority): TaskEvent()
    data class OnRelatedSubjectSelect(val subject: Subject): TaskEvent()
    data object OnIsCompleteChange: TaskEvent()
    data object SaveTask: TaskEvent()
    data object DeleteTask: TaskEvent()
}