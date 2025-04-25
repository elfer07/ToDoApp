package ar.com.todoapp.presentation.ui.fragment.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.com.todoapp.repository.TaskRepository

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class TaskViewModelFactory(private val repo: TaskRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TaskRepository::class.java).newInstance(repo)
    }
}