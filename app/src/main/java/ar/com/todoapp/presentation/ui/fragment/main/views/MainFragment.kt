package ar.com.todoapp.presentation.ui.fragment.main.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ar.com.todoapp.R
import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.local.AppDatabase
import ar.com.todoapp.data.local.LocalTaskDataSource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.databinding.FragmentMainBinding
import ar.com.todoapp.presentation.ui.fragment.main.viewmodels.TaskViewModel
import ar.com.todoapp.presentation.ui.fragment.main.viewmodels.TaskViewModelFactory
import ar.com.todoapp.repository.TaskRepositoryImpl

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    private val viewModel by viewModels<TaskViewModel> {
        TaskViewModelFactory(
            TaskRepositoryImpl(
                LocalTaskDataSource(AppDatabase.getDatabase(requireContext()).taskDao())
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainBinding.bind(view)

        binding.composeView.setContent {
            val taskList by viewModel.tasks.collectAsState()
            var selectedTask by remember { mutableStateOf<Task?>(null) }
            var isDialogOpen by remember { mutableStateOf(false) }

            when (taskList) {
                is Resource.Failure -> {
                    EmptyTasksView()
                }
                is Resource.Success -> {
                    TaskScreen(
                        tasks = (taskList as Resource.Success<List<Task>>).data,
                        onAdd = { isDialogOpen = true },
                        onEdit = {
                            selectedTask = it
                            isDialogOpen = true
                        },
                        onDelete = {
                            viewModel.deleteTask(it)
                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            if (isDialogOpen) {
                TaskDialog(
                    task = selectedTask,
                    onDismiss = {
                        isDialogOpen = false
                        selectedTask = null
                    },
                    onConfirm = { task ->
                        if (task.id == 0) viewModel.saveTask(task)
                        else viewModel.updateTask(task)
                        isDialogOpen = false
                        selectedTask = null
                        Toast.makeText(context, "Edited!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun TaskDialog(
    task: Task? = null,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (task == null) "New Task" else "Edit Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    onConfirm(Task(task?.id ?: 0, title, description))
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EmptyTasksView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "No Tasks")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onAdd: () -> Unit,
    onDelete: (Task) -> Unit,
    onEdit: (Task) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ToDo App",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { pv ->
        LazyColumn(modifier = modifier.padding(pv)) {
            items(tasks) { task ->
                TaskItem(task = task, onEdit = onEdit, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onDelete: (Task) -> Unit,
    onEdit: (Task) -> Unit
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    IconButton(onClick = { onEdit(task) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDelete(task) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}