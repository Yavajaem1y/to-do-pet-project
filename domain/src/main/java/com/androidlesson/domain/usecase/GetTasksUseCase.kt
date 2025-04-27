package com.androidlesson.domain.usecase

import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase (private val repository: TasksRepository){
    fun execute() : Flow<List<TaskModel>>{
        return repository.getTasks()
    }
}