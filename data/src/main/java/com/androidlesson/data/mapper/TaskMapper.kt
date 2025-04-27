package com.androidlesson.data.mapper

import com.androidlesson.data.room.TaskEntity
import com.androidlesson.domain.models.TaskModel

fun TaskModel.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        taskTitle = this.taskTitle,
        category = this.category,
        date = this.date,
        time = this.time,
        notes = this.notes,
        isDone=this.isDone
    )
}

fun TaskEntity.toModel(): TaskModel {
    return TaskModel(
        id = this.id,
        taskTitle = this.taskTitle,
        category = this.category,
        date = this.date,
        time = this.time,
        notes = this.notes,
        isDone=this.isDone
    )
}