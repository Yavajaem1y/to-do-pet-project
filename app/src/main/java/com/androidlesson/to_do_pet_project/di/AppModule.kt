package com.androidlesson.to_do_pet_project.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.DeleteTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Provides
    fun provideTaskViewModelFactory(
        addTaskUseCase: AddTaskUseCase,
        getTasksUseCase: GetTasksUseCase,
        isCompletedUseCase: IsCompletedUseCase,
        deleteTaskUseCase: DeleteTaskUseCase
    ): TaskViewModelFactory {
        return TaskViewModelFactory(addTaskUseCase, getTasksUseCase,isCompletedUseCase,deleteTaskUseCase)
    }
}