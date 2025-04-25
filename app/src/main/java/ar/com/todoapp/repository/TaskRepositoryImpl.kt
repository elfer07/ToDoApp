package ar.com.todoapp.repository

import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.local.LocalTaskDataSource
import ar.com.todoapp.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class TaskRepositoryImpl(private val dataSourceLocal: LocalTaskDataSource): TaskRepository {

    override fun getTaskList(): Flow<Resource<List<Task>>> = flow {
        runCatching {
            dataSourceLocal.getTaskList()
        }.onSuccess {
            it.collect { tasks ->
                emit(Resource.Success(tasks))
            }
        }.onFailure { exception ->
            emit(Resource.Failure(Exception(exception)))
        }
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