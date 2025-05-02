package com.androidlesson.to_do_pet_project.presentation.viewModel.showTaskDialogFragmentViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShowTaskDialogFragmentViewModelFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShowTaskDialogFragmentViewModel(application) as T
    }
}