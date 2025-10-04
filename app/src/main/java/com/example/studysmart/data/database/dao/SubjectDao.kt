package com.example.studysmart.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.studysmart.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Query("select * from subjecttable where subjectId like :id limit 1")
    suspend fun getSubjectById(id: Int): Subject?

    @Query("delete from subjecttable where subjectId like :subjectId")
    suspend fun deleteSubjectById(subjectId: Int)

    @Query("select * from subjecttable")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("select count(*) from subjecttable")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("select sum(goalHours) from subjecttable")
    fun getTotalGoalHours(): Flow<Float>
}