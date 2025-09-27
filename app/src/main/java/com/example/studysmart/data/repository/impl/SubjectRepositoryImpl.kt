package com.example.studysmart.data.repository.impl

import com.example.studysmart.data.database.dao.SubjectDao
import com.example.studysmart.data.repository.SubjectRepository
import com.example.studysmart.domain.model.Subject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao
): SubjectRepository {
    override suspend fun upsertSubject(s: Subject) =
        subjectDao.upsertSubject(s)

    override fun getTotalSubjectCount(): Flow<Int> =
        subjectDao.getTotalSubjectCount()

    override fun getTotalGoalHours(): Flow<Float> =
        subjectDao.getTotalGoalHours()

    override suspend fun deleteSubjectById(id: Int) =
        subjectDao.deleteSubjectById(id)

    override suspend fun getSubjectById(id: Int): Subject? =
        subjectDao.getSubjectById(id)

    override fun getAllSubjects(): Flow<List<Subject>> =
        subjectDao.getAllSubjects()
}