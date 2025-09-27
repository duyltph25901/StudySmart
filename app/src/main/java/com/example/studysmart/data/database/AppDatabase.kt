package com.example.studysmart.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studysmart.data.database.converter.ColorsConverter
import com.example.studysmart.data.database.dao.SessionDao
import com.example.studysmart.data.database.dao.SubjectDao
import com.example.studysmart.data.database.dao.TaskDao
import com.example.studysmart.domain.model.Session
import com.example.studysmart.domain.model.Subject
import com.example.studysmart.domain.model.Task

@Database(
    entities = [
        Session::class,
        Subject::class,
        Task::class
    ], version = 1,
    exportSchema = false
)
@TypeConverters(ColorsConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
}