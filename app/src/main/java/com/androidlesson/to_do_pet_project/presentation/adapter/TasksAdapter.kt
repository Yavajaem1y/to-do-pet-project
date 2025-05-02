package com.androidlesson.to_do_pet_project.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.to_do_pet_project.R
import com.androidlesson.to_do_pet_project.presentation.callback.OnTaskCheckListener
import com.androidlesson.to_do_pet_project.presentation.fragments.ShowTaskDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class TasksAdapter(
    private val onTaskCheckListener: OnTaskCheckListener,
    private val coroutineScope: CoroutineScope,
    private val fragmentManager: FragmentManager,
    var currentDate: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    sealed class TaskListItem {
        data class TaskItem(val task: TaskModel) : TaskListItem()
        object Divider : TaskListItem()
    }

    companion object {
        private const val TYPE_TASK = 0
        private const val TYPE_DIVIDER = 1
    }

    var items: List<TaskListItem> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TaskListItem.TaskItem -> TYPE_TASK
            is TaskListItem.Divider -> TYPE_DIVIDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TASK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
                TaskViewHolder(view, onTaskCheckListener, coroutineScope)
            }
            TYPE_DIVIDER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_completed_header, parent, false)
                DividerViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is TaskListItem.TaskItem -> {
                (holder as TaskViewHolder).bind(item.task, item.task.isDone)

                holder.itemView.setOnClickListener {
                    val dialog = ShowTaskDialogFragment.newInstance(item.task)
                    dialog.show(fragmentManager, "ObjectInfoDialog")
                }
            }

            is TaskListItem.Divider -> {}
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTasksList(tasks: List<TaskModel>) {
        val sorted = tasks.sortedBy { it.isDone }
        val activeTasks = sorted.filter { !it.isDone && it.date.equals(currentDate)}
        val completedTasks = sorted.filter { it.isDone && it.date.equals(currentDate)}

        items = when {
            activeTasks.isNotEmpty() && completedTasks.isNotEmpty() -> {
                activeTasks.map { TaskListItem.TaskItem(it) } +
                        listOf(TaskListItem.Divider) +
                        completedTasks.map { TaskListItem.TaskItem(it) }
            }
            else -> {
                (activeTasks + completedTasks).map { TaskListItem.TaskItem(it) }
            }
        }

        notifyDataSetChanged()
    }

    class TaskViewHolder(
        view: View,
        private val onTaskCheckListener: OnTaskCheckListener,
        private val coroutineScope: CoroutineScope
    ) : RecyclerView.ViewHolder(view) {

        private val checkbox: CheckBox = view.findViewById(R.id.checkbox_done)
        private val title: TextView = view.findViewById(R.id.tv_task_title)
        private val time: TextView = view.findViewById(R.id.tv_time)
        private val image: ImageView = view.findViewById(R.id.iv_task_image)

        fun bind(task: TaskModel, isDone: Boolean) {
            title.text = task.taskTitle
            time.text = task.time

            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = isDone
            if (isDone) {
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                time.paintFlags = time.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                time.paintFlags = time.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                coroutineScope.launch {
                    onTaskCheckListener.onTaskCheck(task, isChecked)
                }
            }

            when (task.category){
                "first" -> {
                    image.setBackgroundResource(R.drawable.bg_blue_circle)
                    image.setImageResource(R.drawable.ic_first_category)
                }

                "second" -> {
                    image.setBackgroundResource(R.drawable.bg_violet_circle)
                    image.setImageResource(R.drawable.ic_second_category)
                }

                "third" -> {
                    image.setBackgroundResource(R.drawable.bg_yellow_circle)
                    image.setImageResource(R.drawable.ic_third_category)
                }
            }
        }
    }

    class DividerViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
