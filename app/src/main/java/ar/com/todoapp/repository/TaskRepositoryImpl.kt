package ar.com.todoapp.repository

import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.local.LocalTaskDataSource
import ar.com.todoapp.data.model.Task

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class TaskRepositoryImpl(private val dataSourceLocal: LocalTaskDataSource): TaskRepository {

    override suspend fun getTaskList(): Resource<List<Task>> {
        return dataSourceLocal.getTaskList()
    }

    override suspend fun saveTask(task: Task) {
        dataSourceLocal.saveTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dataSourceLocal.deleteTask(task)
    }

    override suspend fun update(task: Task) {
        dataSourceLocal.updateTask(task)
    }
}