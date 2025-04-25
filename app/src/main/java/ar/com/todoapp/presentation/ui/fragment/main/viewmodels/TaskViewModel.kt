package ar.com.todoapp.presentation.ui.fragment.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class TaskViewModel(private val repo: TaskRepository): ViewModel() {

    fun fetchTaskList() = liveData(Dispatchers.Main){
        try {
            emit(repo.getTaskList())
        }catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun saveTask(task: Task){
        viewModelScope.launch {
            repo.saveTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            repo.deleteTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            repo.update(task)
        }
    }
}