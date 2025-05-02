package com.androidlesson.domain.usecase

import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.repository.TasksRepository

class EditTaskUseCase(private var tasksRepository: TasksRepository) {
    suspend fun execute(taskModel: TaskModel){
        tasksRepository.editTask(taskModel)
    }
}