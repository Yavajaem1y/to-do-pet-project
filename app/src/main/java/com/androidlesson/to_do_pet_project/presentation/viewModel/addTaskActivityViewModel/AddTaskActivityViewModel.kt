package com.androidlesson.to_do_pet_project.presentation.viewModel.addTaskActivityViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidlesson.domain.models.TaskModel

class AddTaskActivityViewModel : ViewModel(){
    var date: String =""
    var time: String =""

    private val categoryMutableLiveData:MutableLiveData<String> = MutableLiveData("")

    fun pickCategory(category : String){
        categoryMutableLiveData.value=category
    }

    fun getCategoryLiveData(): LiveData<String>{
        return categoryMutableLiveData
    }
}