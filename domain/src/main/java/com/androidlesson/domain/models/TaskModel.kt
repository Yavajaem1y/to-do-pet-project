package com.androidlesson.domain.models

import java.io.Serializable

data class TaskModel(
    val id: Int=0,
    val taskTitle: String,
    val category: String,
    val date: String,
    val time: String,
    val notes: String,
    var isDone: Boolean
) : Serializable
