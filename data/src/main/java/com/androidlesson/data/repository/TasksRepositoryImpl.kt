package com.androidlesson.data.repository

import com.androidlesson.data.mapper.toEntity
import com.androidlesson.data.mapper.toModel
import com.androidlesson.data.room.TaskDao
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TasksRepositoryImpl(private val taskDao: TaskDao): TasksRepository {
    override suspend fun addTask(task: TaskModel) {
        taskDao.insertTask(task.toEntity())
    }

    override fun getTasks(): Flow<List<TaskModel>> {
        return taskDao.getTasks().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun isCompleted(task: TaskModel) {
        taskDao.isCompleted(task.id,task.isDone);
    }

    override suspend fun deleteTask(task: TaskModel) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun editTask(task: TaskModel) {
        taskDao.editTask(task.toEntity())
    }
}