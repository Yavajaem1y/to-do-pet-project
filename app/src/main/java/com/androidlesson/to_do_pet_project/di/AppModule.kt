package com.androidlesson.to_do_pet_project.di

import android.app.Application
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.DeleteTaskUseCase
import com.androidlesson.domain.usecase.EditTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase
import com.androidlesson.to_do_pet_project.presentation.viewModel.showTaskDialogFragmentViewModel.ShowTaskDialogFragmentViewModelFactory
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
        application: Application,
        addTaskUseCase: AddTaskUseCase,
        getTasksUseCase: GetTasksUseCase,
        isCompletedUseCase: IsCompletedUseCase,
        deleteTaskUseCase: DeleteTaskUseCase,
        editTaskUseCase: EditTaskUseCase
    ): TaskViewModelFactory {
        return TaskViewModelFactory(application,addTaskUseCase, getTasksUseCase,isCompletedUseCase,deleteTaskUseCase, editTaskUseCase)
    }

    @Provides
    fun provideShowTaskDialogFragmentViewModelFactory(application: Application): ShowTaskDialogFragmentViewModelFactory{
        return ShowTaskDialogFragmentViewModelFactory(application)
    }
}