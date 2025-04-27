package com.androidlesson.domain.usecase

import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.repository.TasksRepository

class AddTaskUseCase (private val repository: TasksRepository){
    suspend fun execute(taskModel: TaskModel){
        repository.addTask(taskModel)
    }
}