package com.example.studysmart.data.repository.impl

import com.example.studysmart.data.database.dao.TaskDao
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.domain.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {
    override suspend fun upsertTask(task: Task) =
        taskDao.upsertTask(task)

    override suspend fun deleteTaskById(id: Int) =
        taskDao.deleteTaskById(id)

    override suspend fun getTaskById(id: Int): Task? =
        taskDao.getTaskById(id)

    override fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedTasksForSubject(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks()
}