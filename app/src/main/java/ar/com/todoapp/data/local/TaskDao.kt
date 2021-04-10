package ar.com.todoapp.data.local

import androidx.room.*
import ar.com.todoapp.data.model.Task

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
@Dao
interface TaskDao {

    @Query("SELECT * FROM Tasks")
    suspend fun getTaskList(): List<Task>

    @Insert
    suspend fun saveTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun update(vararg task: Task)
}