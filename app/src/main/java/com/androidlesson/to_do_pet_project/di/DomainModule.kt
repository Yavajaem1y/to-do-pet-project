package com.androidlesson.to_do_pet_project.di

import com.androidlesson.domain.repository.TasksRepository
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.DeleteTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideAddTaskUseCase(tasksRepository: TasksRepository): AddTaskUseCase{
        return AddTaskUseCase(tasksRepository)
    }

    @Provides
    fun provideGetTasksUseCase(tasksRepository: TasksRepository): GetTasksUseCase {
        return GetTasksUseCase(tasksRepository)
    }

    @Provides
    fun provideIsCompletedUseCase(tasksRepository: TasksRepository): IsCompletedUseCase {
        return IsCompletedUseCase(tasksRepository)
    }

    @Provides
    fun provideDeleteTaskUseCase(tasksRepository: TasksRepository): DeleteTaskUseCase {
        return DeleteTaskUseCase(tasksRepository)
    }
}