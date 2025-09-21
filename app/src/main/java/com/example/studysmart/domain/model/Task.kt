package com.example.studysmart.domain.model

data class Task(
    var taskId: Int = 0,
    var taskSubjectId: Int = 0,
    var title: String,
    var description: String,
    var duDate: Long,
    var priority: Int,
    var relatedToSubject: String,
    var isComplete: Boolean
) {
}