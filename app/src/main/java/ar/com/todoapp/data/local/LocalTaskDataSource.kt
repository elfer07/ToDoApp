package ar.com.todoapp.data.local

import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class LocalTaskDataSource(private val taskDao: TaskDao) {

    suspend fun getTaskList(): Resource<List<Task>>{
        return Resource.Success(taskDao.getTaskList())
    }

    suspend fun saveTask(task: Task){
        taskDao.saveTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.update(task)
    }
}