package com.androidlesson.to_do_pet_project.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.to_do_pet_project.R
import com.androidlesson.to_do_pet_project.app.App
import com.androidlesson.to_do_pet_project.presentation.adapter.CalendarAdapter
import com.androidlesson.to_do_pet_project.presentation.adapter.TasksAdapter
import com.androidlesson.to_do_pet_project.presentation.callback.OnTaskCheckListener
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var vm: TaskViewModel
    @Inject
    lateinit var vmFactory: TaskViewModelFactory

    private lateinit var rv_tasks_holder: RecyclerView
    private lateinit var rv_calendar: RecyclerView
    private lateinit var b_add_new_task: ImageView
    private lateinit var tv_date: TextView

    private lateinit var adapter:TasksAdapter;
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent?.injectMainActivity(this)

        // ViewModel
        vm = ViewModelProvider(this, vmFactory).get(TaskViewModel::class.java)

        // UI
        initialization()
        setupCalendar()
        observer()
        setOnClickListener()
    }

    private fun initialization() {
        rv_tasks_holder = findViewById(R.id.rv_tasks_holder)
        rv_calendar = findViewById(R.id.rv_calendar)
        b_add_new_task = findViewById(R.id.b_add_new_task)
        tv_date = findViewById(R.id.tv_date)

        tv_date.text=getCurrentDateFormatted()

        adapter = TasksAdapter(object : OnTaskCheckListener {
            override suspend fun onTaskCheck(task: TaskModel, isChecked: Boolean) {
                vm.isCompleted(task, isChecked);
            }
        }, CoroutineScope(Dispatchers.IO), supportFragmentManager)
        rv_tasks_holder.layoutManager = LinearLayoutManager(this)
        rv_tasks_holder.adapter = adapter
        rv_tasks_holder.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter.updateTasksList(ArrayList<TaskModel>())
    }

    private fun setupCalendar() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_calendar.layoutManager = layoutManager

        val dates = generateDates()

        val todayIndex = dates.indexOfFirst { date ->
            val cal1 = Calendar.getInstance().apply { time = date }
            val cal2 = Calendar.getInstance()
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
        }

        calendarAdapter = CalendarAdapter(dates, todayIndex) { selectedDate ->
            updateSelectedDate(selectedDate)
        }

        rv_calendar.adapter = calendarAdapter

        rv_calendar.post {
            if (todayIndex != -1) {
                layoutManager.scrollToPosition(todayIndex)
            } else {
                layoutManager.scrollToPosition(dates.size / 2)
            }
        }

        LinearSnapHelper().attachToRecyclerView(rv_calendar)
    }

    private fun generateDates(): List<Date> {
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_MONTH, -14)

        for (i in 0..29) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    private fun updateSelectedDate(date: Date) {
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        tv_date.text = formatter.format(date)
    }

    fun getCurrentDateFormatted(): String {
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        return formatter.format(Date())
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