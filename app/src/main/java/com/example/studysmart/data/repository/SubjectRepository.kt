package com.example.studysmart.data.repository

import com.example.studysmart.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {

    suspend fun upsertSubject(s: Subject)

    fun getTotalSubjectCount(): Flow<Int>

    fun getTotalGoalHours(): Flow<Float>

    suspend fun  deleteSubjectById(id: Int)

    suspend fun getSubjectById(id: Int): Subject?

    fun getAllSubjects(): Flow<List<Subject>>

}