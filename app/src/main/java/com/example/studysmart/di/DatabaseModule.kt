package com.example.studysmart.di

import android.content.Context
import androidx.room.Room
import com.example.studysmart.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctx: Context) =
        Room.databaseBuilder(
            ctx,
            AppDatabase::class.java,
            "AppDatabase.db"
        ).build()

    @Provides
    @Singleton
    fun provideSubjectDao(db: AppDatabase) = db.subjectDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideSessionDao(db: AppDatabase) = db.sessionDao()

}