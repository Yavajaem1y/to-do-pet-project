package com.androidlesson.to_do_pet_project.presentation.viewModel.showTaskDialogFragmentViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.androidlesson.domain.models.TaskModel

class ShowTaskDialogFragmentViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var taskModel:TaskModel
}