package ar.com.todoapp.repository

import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
interface TaskRepository {

    suspend fun getTaskList(): Resource<List<Task>>
    suspend fun saveTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun update(task: Task)
}