package ar.com.todoapp.repository

import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
interface TaskRepository {

    fun getTaskList(): Flow<Resource<List<Task>>>
    suspend fun saveTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun update(task: Task)
}