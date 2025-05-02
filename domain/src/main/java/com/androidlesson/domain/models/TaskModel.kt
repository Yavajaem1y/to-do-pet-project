package com.androidlesson.domain.models

import java.io.Serializable

data class TaskModel(
    val id: Int=0,
    var taskTitle: String,
    var category: String,
    var date: String,
    var time: String,
    val notes: String,
    var isDone: Boolean
) : Serializable
