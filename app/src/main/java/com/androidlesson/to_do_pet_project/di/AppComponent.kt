package com.androidlesson.to_do_pet_project.di

import com.androidlesson.to_do_pet_project.presentation.activities.AddTaskActivity
import com.androidlesson.to_do_pet_project.presentation.activities.MainActivity
import dagger.Component

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }

    fun injectMainActivity(mainActivity: MainActivity)

    fun injectAddTaskActivity(addTaskActivity: AddTaskActivity)
}