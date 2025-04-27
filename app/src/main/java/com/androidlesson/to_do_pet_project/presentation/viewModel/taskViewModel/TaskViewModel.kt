package com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase
import kotlinx.coroutines.launch

class TaskViewModel(private val addTaskUseCase: AddTaskUseCase, private val getTasksUseCase: GetTasksUseCase, private val isCompletedUseCase: IsCompletedUseCase) : ViewModel() {

    private val taskListMutableLiveData: MutableLiveData<List<TaskModel>> = MutableLiveData()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            getTasksUseCase.execute().collect { tasks ->
                taskListMutableLiveData.postValue(tasks)
            }
        }
    }

    suspend fun isCompleted(taskModel: TaskModel, boolean: Boolean){
        isCompletedUseCase.execute(taskModel, boolean)
    }

    suspend fun addTask(taskModel: TaskModel){
        addTaskUseCase.execute(taskModel)
    }

    fun getTaskListLiveData(): LiveData<List<TaskModel>>{
        return taskListMutableLiveData
    }
}