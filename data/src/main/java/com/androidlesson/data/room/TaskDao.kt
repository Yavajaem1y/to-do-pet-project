package com.androidlesson.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidlesson.domain.models.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET isDone = :isDone WHERE id= :taskId")
    suspend fun isCompleted(taskId: Int, isDone: Boolean)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}