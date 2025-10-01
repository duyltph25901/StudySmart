package com.example.studysmart.data.repository.impl

import com.example.studysmart.data.database.dao.TaskDao
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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

    override fun getAllUpComingTask(): Flow<List<Task>> =
        taskDao.getAllTasks().map { tasks ->
            tasks.filter { !it.isComplete }
        }.map { tasks ->
            sortTasks(tasks)
        }

    private fun sortTasks(tasks: List<Task>): List<Task> =
        tasks.sortedWith(
            compareBy<Task> { it.duDate }.thenByDescending { it.priority }
        )
}