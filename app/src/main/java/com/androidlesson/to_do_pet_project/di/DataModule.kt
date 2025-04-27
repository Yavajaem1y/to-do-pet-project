package com.androidlesson.to_do_pet_project.di

import android.app.Application
import com.androidlesson.data.repository.TasksRepositoryImpl
import com.androidlesson.data.room.TaskDao
import com.androidlesson.data.room.TaskDatabase
import com.androidlesson.domain.repository.TasksRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideDatabase(app: Application): TaskDatabase {
        return TaskDatabase.getDatabase(app)
    }

    @Provides
    fun provideTaskDao(db: TaskDatabase): TaskDao {
        return db.taskDao()
    }

    //Repository
    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TasksRepository{
        return TasksRepositoryImpl(taskDao)
    }
}