package com.androidlesson.to_do_pet_project.presentation.callback

import com.androidlesson.domain.models.TaskModel

interface OnTaskCheckListener {
    suspend fun onTaskCheck(task: TaskModel, isChecked: Boolean)
}