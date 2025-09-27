package com.example.studysmart.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.studysmart.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(s: Session)

    @Delete
    suspend fun deleteSession(s: Session)

    @Query("select * from sessiontable")
    fun getAllSessions(): Flow<List<Session>>

    @Query("select * from sessiontable where sessionSubjectId like :id")
    fun getAllSessionsBySessionSubjectId(id: Int): Flow<List<Session>>

    @Query("select sum(duration) from sessiontable")
    fun getTotalSessionDuration(): Flow<Long>

    @Query("select sum(duration) from sessiontable where sessionSubjectId like :id")
    fun getTotalSessionDurationBySubjectId(id: Int): Flow<Long>

    @Query("delete from sessiontable where sessionSubjectId like :id")
    suspend fun deleteSessionBySessionSubjectId(id: Int)
}