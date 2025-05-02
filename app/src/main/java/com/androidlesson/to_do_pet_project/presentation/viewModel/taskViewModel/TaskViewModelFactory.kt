package com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.DeleteTaskUseCase
import com.androidlesson.domain.usecase.EditTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase

class TaskViewModelFactory(private val application: Application,
                           private val addTaskUseCase: AddTaskUseCase,
                           private val getTasksUseCase: GetTasksUseCase,
                           private val isCompletedUseCase: IsCompletedUseCase,
                           private val deleteTaskUseCase: DeleteTaskUseCase,
                           private val editTaskUseCase: EditTaskUseCase
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(application,addTaskUseCase, getTasksUseCase, isCompletedUseCase,deleteTaskUseCase,editTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}