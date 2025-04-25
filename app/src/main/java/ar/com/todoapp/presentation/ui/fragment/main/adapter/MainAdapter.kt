package ar.com.todoapp.presentation.ui.fragment.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.com.todoapp.core.BaseViewHolder
import ar.com.todoapp.data.model.Task
import ar.com.todoapp.databinding.TaskItemBinding

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
class MainAdapter(
    private val context: Context,
    private val taskList: List<Task>,
    private val itemClickEditListener: OnEditClickListener,
    private val itemClickDeleteListener: OnDeleteClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface OnEditClickListener {
        fun onEditTaskClick(item: Task, position: Int)
    }

    interface OnDeleteClickListener{
        fun onDeleteTaskClick(item: Task, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = TaskViewHolder(itemBinding, parent.context)

        itemBinding.ivDeleteTask.setOnClickListener {
            val position = holder.adapterPosition
            itemClickDeleteListener.onDeleteTaskClick(taskList[position], position)
            notifyItemRemoved(position)
        }

        itemBinding.ivEditTask.setOnClickListener {
            val position = holder.adapterPosition
            itemClickEditListener.onEditTaskClick(taskList[position], position)
        }
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                holder.bind(taskList[position])

            }
        }
    }

    override fun getItemCount(): Int = taskList.size

    private inner class TaskViewHolder(val binding: TaskItemBinding, val context: Context) :
        BaseViewHolder<Task>(binding.root) {
        val tvTitle = binding.tvTitleTask
        val tvDescription = binding.tvDescriptionTask

        override fun bind(item: Task) {
            tvTitle.text = item.title
            tvDescription.text = item.description
        }
    }
}