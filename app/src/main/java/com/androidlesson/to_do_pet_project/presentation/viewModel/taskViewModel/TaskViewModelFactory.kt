package com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.DeleteTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase

class TaskViewModelFactory(private val addTaskUseCase: AddTaskUseCase,
                           private val getTasksUseCase: GetTasksUseCase,
                           private val isCompletedUseCase: IsCompletedUseCase,
                            private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(addTaskUseCase, getTasksUseCase, isCompletedUseCase,deleteTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}