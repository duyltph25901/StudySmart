package com.example.studysmart.data.repository

import com.example.studysmart.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun upsertTask(task: Task)

    suspend fun deleteTaskById(id: Int)

    suspend fun getTaskById(id: Int): Task?

    fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<Task>>

    fun getCompletedTasksForSubject(subjectId: Int): Flow<List<Task>>

    fun getAllTasks(): Flow<List<Task>>

    fun getAllUpComingTask(): Flow<List<Task>>

}