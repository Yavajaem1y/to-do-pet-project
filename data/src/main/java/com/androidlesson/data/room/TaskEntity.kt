package com.androidlesson.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val taskTitle: String,
    val category: String,
    val date: String,
    val time: String,
    val notes: String,
    val isDone: Boolean
)

