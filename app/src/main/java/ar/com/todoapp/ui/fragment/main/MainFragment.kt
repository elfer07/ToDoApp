package ar.com.todoapp.ui.fragment.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.todoapp.R
import ar.com.todoapp.core.Resource
import ar.com.todoapp.data.local.AppDatabase
import ar.com.todoapp.data.local.LocalTaskDataSource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.databinding.FragmentMainBinding
import ar.com.todoapp.presentation.TaskViewModel
import ar.com.todoapp.presentation.TaskViewModelFactory
import ar.com.todoapp.repository.TaskRepositoryImpl
import ar.com.todoapp.ui.fragment.main.adapter.MainAdapter

class MainFragment : Fragment(R.layout.fragment_main), MainAdapter.OnEditClickListener, MainAdapter.OnDeleteClickListener {

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

        setupRecyclerView()

        binding.fabAddTask.setOnClickListener {
            createTask()
        }
    }

    private fun setupRecyclerView() {
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())

        viewModel.fetchTaskList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    if (it.data.isEmpty()){
                    } else {
                        binding.rvTasks.adapter = MainAdapter(requireContext(), it.data, this, this)

                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "ERROR: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun createTask() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_create_task)
        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.show()
        val etTitle = dialog.findViewById<EditText>(R.id.et_task_title)
        val etDescription = dialog.findViewById<EditText>(R.id.et_task_description)
        val btnCreate = dialog.findViewById<Button>(R.id.btn_create_task)
        btnCreate.setOnClickListener {
            val title = etTitle.text.toString()
            val description = etDescription.text.toString()
            if (title.isEmpty()){
                etTitle.error = "Title is Empty"
                return@setOnClickListener
            }
            if (description.isEmpty()){
                etDescription.error = "Description is Empty"
                return@setOnClickListener
            }
            val task = Task(0, title, description)
            viewModel.saveTask(task)
            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    override fun onEditTaskClick(item: Task, position: Int) {
        val action = MainFragmentDirections.actionMainFragmentToDialogEditFragment(
            item.id,
            item.title,
            item.description
        )
        findNavController().navigate(action)
    }

    override fun onDeleteTaskClick(item: Task, position: Int) {
        viewModel.deleteTask(item)
        Toast.makeText(requireContext(), "Deleted!", Toast.LENGTH_SHORT).show()
        binding.rvTasks.adapter?.notifyItemRemoved(position)
        viewModel.fetchTaskList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.rvTasks.adapter = MainAdapter(requireContext(), it.data, this, this)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "ERROR: ${it.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}