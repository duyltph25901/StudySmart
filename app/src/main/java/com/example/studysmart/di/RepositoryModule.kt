package com.example.studysmart.di

import com.example.studysmart.data.repository.SessionRepository
import com.example.studysmart.data.repository.SubjectRepository
import com.example.studysmart.data.repository.TaskRepository
import com.example.studysmart.data.repository.impl.SessionRepositoryImpl
import com.example.studysmart.data.repository.impl.SubjectRepositoryImpl
import com.example.studysmart.data.repository.impl.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        impl: SubjectRepositoryImpl
    ): SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository
}