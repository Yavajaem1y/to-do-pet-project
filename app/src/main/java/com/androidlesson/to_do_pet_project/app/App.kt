package com.androidlesson.to_do_pet_project.app

import android.app.Application
import com.androidlesson.to_do_pet_project.di.AppComponent
import com.androidlesson.to_do_pet_project.di.AppModule
import com.androidlesson.to_do_pet_project.di.DaggerAppComponent

class App : Application() {
    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            ?.appModule(AppModule(this))
            ?.build()
    }
}