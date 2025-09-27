package com.example.studysmart.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TaskTable")
data class Task(
    @PrimaryKey(autoGenerate = true)
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