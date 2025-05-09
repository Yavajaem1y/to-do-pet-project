package com.androidlesson.domain.repository

import com.androidlesson.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun addTask(task: TaskModel)
    fun getTasks(): Flow<List<TaskModel>>
    suspend fun isCompleted(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
    suspend fun editTask(task: TaskModel)
}