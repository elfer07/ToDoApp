package ar.com.todoapp.ui.fragment.dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.com.todoapp.R
import ar.com.todoapp.data.local.AppDatabase
import ar.com.todoapp.data.local.LocalTaskDataSource
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.databinding.DialogEditTaskBinding
import ar.com.todoapp.presentation.TaskViewModel
import ar.com.todoapp.presentation.TaskViewModelFactory
import ar.com.todoapp.repository.TaskRepositoryImpl

class DialogEditFragment : Fragment(R.layout.fragment_dialog_edit) {

    private lateinit var binding: DialogEditTaskBinding

    private val viewModel by viewModels<TaskViewModel> {
        TaskViewModelFactory(
            TaskRepositoryImpl(
                LocalTaskDataSource(AppDatabase.getDatabase(requireContext()).taskDao())
            )
        )
    }

    private val args by navArgs<DialogEditFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogEditTaskBinding.bind(view)
        val uId = args.id
        val uTitle = args.title
        val uDescription = args.description
        binding.etEditTaskTitle.setText(uTitle)
        binding.etEditTaskDescription.setText(uDescription)
        val etTitle = binding.etEditTaskTitle
        val etDescription = binding.etEditTaskDescription

        binding.btnEditTask.setOnClickListener {
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
            val uTask = Task(uId, title, description)
            viewModel.updateTask(uTask)
            Toast.makeText(requireContext(), "Edited!", Toast.LENGTH_SHORT).show()
            val action = DialogEditFragmentDirections.actionDialogEditFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }
}