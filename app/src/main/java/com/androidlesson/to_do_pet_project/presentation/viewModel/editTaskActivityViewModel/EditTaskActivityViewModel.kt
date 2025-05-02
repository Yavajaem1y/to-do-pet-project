package com.androidlesson.to_do_pet_project.presentation.viewModel.editTaskActivityViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidlesson.domain.models.TaskModel

class EditTaskActivityViewModel : ViewModel(){
    lateinit var taskModel: TaskModel

    private val categoryMutableLiveData: MutableLiveData<String> = MutableLiveData("")

    fun pickCategory(category : String){
        categoryMutableLiveData.value=category
        taskModel.category=category
    }

    fun getCategoryLiveData(): LiveData<String> {
        return categoryMutableLiveData
    }
}