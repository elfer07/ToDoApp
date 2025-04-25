package ar.com.todoapp.data.local

import ar.com.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class LocalTaskDataSource(private val taskDao: TaskDao) {

    fun getTaskList(): Flow<List<Task>> = taskDao.getTaskList()

    suspend fun saveTask(task: Task) {
        taskDao.saveTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }
}