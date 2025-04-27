package com.androidlesson.domain.usecase

import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.repository.TasksRepository

class IsCompletedUseCase(private val repository: TasksRepository) {
    suspend fun execute(taskModel: TaskModel, boolean: Boolean){
        taskModel.isDone=boolean
        repository.isCompleted(taskModel)
    }
}