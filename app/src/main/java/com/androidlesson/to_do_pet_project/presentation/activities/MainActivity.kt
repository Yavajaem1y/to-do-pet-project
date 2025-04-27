package com.androidlesson.to_do_pet_project.presentation.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.to_do_pet_project.R
import com.androidlesson.to_do_pet_project.app.App
import com.androidlesson.to_do_pet_project.presentation.adapter.TasksAdapter
import com.androidlesson.to_do_pet_project.presentation.callback.OnTaskCheckListener
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var vm: TaskViewModel
    @Inject
    lateinit var vmFactory: TaskViewModelFactory

    private lateinit var rv_tasks_holder: RecyclerView
    private lateinit var b_add_new_task: TextView

    private lateinit var adapter:TasksAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent?.injectMainActivity(this)

        // ViewModel
        vm = ViewModelProvider(this, vmFactory).get(TaskViewModel::class.java)

        // UI
        initialization()
        observer()
        setOnClickListener()
    }

    private fun initialization() {
        rv_tasks_holder = findViewById(R.id.rv_tasks_holder)
        b_add_new_task = findViewById(R.id.b_add_new_task)

        var list=ArrayList<TaskModel>()
        list.add(TaskModel(0, "Task 1", "eq", "11.02.2025", "11:00", "Davay davay", false))
        list.add(TaskModel(0, "Task 2", "eq", "14.03.2025", "14:00", "Davay davay", false))
        list.add(TaskModel(0, "Task 2", "eq", "02.04.2025", "11:41", "Davay davay", false))

        adapter = TasksAdapter(object : OnTaskCheckListener {
            override suspend fun onTaskCheck(task: TaskModel, isChecked: Boolean) {
                vm.isCompleted(task, isChecked);
            }
        }, CoroutineScope(Dispatchers.IO), supportFragmentManager)
        rv_tasks_holder.layoutManager = LinearLayoutManager(this)
        rv_tasks_holder.adapter = adapter
        rv_tasks_holder.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter.updateTasksList(list)
    }

    private fun observer() {
        vm.getTaskListLiveData().observe(this){
            taskList->updateAdapter(taskList)
        }
    }

    private fun updateAdapter(tasks: List<TaskModel>){
        adapter.updateTasksList(tasks)
    }

    private fun setOnClickListener() {
        b_add_new_task.setOnClickListener{
            startActivity(Intent(this,AddTaskActivity::class.java))
        }
    }
}