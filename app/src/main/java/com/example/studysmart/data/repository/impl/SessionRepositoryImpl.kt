package com.example.studysmart.data.repository.impl

import com.example.studysmart.data.database.dao.SessionDao
import com.example.studysmart.data.repository.SessionRepository
import com.example.studysmart.domain.model.Session
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
): SessionRepository {
    override suspend fun insertSession(s: Session) =
        sessionDao.insertSession(s)

    override suspend fun deleteSession(s: Session) =
        sessionDao.deleteSession(s)

    override fun getAllSessions(): Flow<List<Session>> =
        sessionDao.getAllSessions()

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionsDurationBySubjectId(subjectId: Int): Flow<Long> {
        TODO("Not yet implemented")
    }


}