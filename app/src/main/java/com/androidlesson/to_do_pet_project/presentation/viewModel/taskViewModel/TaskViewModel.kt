package com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.usecase.AddTaskUseCase
import com.androidlesson.domain.usecase.DeleteTaskUseCase
import com.androidlesson.domain.usecase.GetTasksUseCase
import com.androidlesson.domain.usecase.IsCompletedUseCase
import kotlinx.coroutines.launch

class TaskViewModel(
    application: Application,
    private val addTaskUseCase: AddTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val isCompletedUseCase: IsCompletedUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : AndroidViewModel(application) {
    private val taskListMutableLiveData: MutableLiveData<List<TaskModel>> = MutableLiveData(ArrayList())
    private val currentDateMutableLiveData: MutableLiveData<String> = MutableLiveData("")

    init {
        observeTasks()
        Log.d("task","vm created")
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

    suspend fun deleteTask(taskModel: TaskModel){
        deleteTaskUseCase.execute(taskModel)
    }

    fun setCurrentDate(date : String){
        currentDateMutableLiveData.postValue(date)
    }

    fun getTaskListLiveData(): LiveData<List<TaskModel>>{
        return taskListMutableLiveData
    }

    fun getCurrentDateLiveData(): LiveData<String>{
        return currentDateMutableLiveData
    }
}