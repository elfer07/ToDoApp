package ar.com.todoapp.presentation.ui.fragment.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class TaskViewModel(private val repo: TaskRepository): ViewModel() {

    private val _tasks = MutableStateFlow<Resource<List<Task>>>(Resource.Success(emptyList()))
    val tasks: StateFlow<Resource<List<Task>>> = _tasks

    init {
        viewModelScope.launch {
            repo.getTaskList()
                .collect {
                    _tasks.value = it
                }
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