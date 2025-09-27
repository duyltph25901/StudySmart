package com.example.studysmart.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.studysmart.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("delete from tasktable where taskId like :id")
    suspend fun deleteTaskById(id: Int)

    @Query("delete from tasktable where taskSubjectId like :id")
    suspend fun deleteTaskBySubjectId(id: Int)

    @Query("select * from tasktable where taskId like :id limit 1")
    suspend fun getTaskById(id: Int): Task?

    @Query("select * from TaskTable where taskSubjectId like :id")
    fun getTasksForSubject(id: Int): Flow<List<Task>>

    @Query("select * from tasktable")
    fun getAllTasks(): Flow<List<Task>>

}