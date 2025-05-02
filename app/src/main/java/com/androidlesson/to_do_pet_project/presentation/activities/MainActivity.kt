package com.androidlesson.to_do_pet_project.presentation.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TreeSet
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var vm: TaskViewModel
    @Inject
    lateinit var vmFactory: TaskViewModelFactory

    private lateinit var rv_tasks_holder: RecyclerView
    private lateinit var rv_calendar: RecyclerView
    private lateinit var b_add_new_task: ImageView
    private lateinit var b_back: ImageView
    private lateinit var tv_date: TextView

    private lateinit var tasksAdapter:TasksAdapter;
    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).appComponent?.injectMainActivity(this)

        // ViewModel
        vm = ViewModelProvider(this, vmFactory).get(TaskViewModel::class.java)

        // UI
        setBackGround()
        initialization()
        setupCalendar()
        observer()
        setOnClickListener()
    }

    private fun setBackGround(){
        val statusBarColor = Color.parseColor("#FF4A3780")
        val navBarColor = Color.parseColor("#FFF1F5F9")

        window.statusBarColor = statusBarColor
        window.navigationBarColor = navBarColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }

    private fun initialization() {
        rv_tasks_holder = findViewById(R.id.rv_tasks_holder)
        rv_calendar = findViewById(R.id.rv_calendar)
        b_add_new_task = findViewById(R.id.b_add_new_task)
        tv_date = findViewById(R.id.tv_date)
        b_back = findViewById(R.id.b_back)

        if (vm.getCurrentDateLiveData().value.toString().isEmpty()) vm.setCurrentDate(getCurrentDateFormatted())

        tasksAdapter = TasksAdapter(object : OnTaskCheckListener {
            override suspend fun onTaskCheck(task: TaskModel, isChecked: Boolean) {
                vm.isCompleted(task, isChecked);
            }
        }, CoroutineScope(Dispatchers.IO), supportFragmentManager, vm.getCurrentDateLiveData().value.toString())
        rv_tasks_holder.layoutManager = LinearLayoutManager(this)
        rv_tasks_holder.adapter = tasksAdapter
        rv_tasks_holder.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        tasksAdapter.updateTasksList(ArrayList<TaskModel>())
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

        calendarAdapter = CalendarAdapter(dates, todayIndex, TreeSet()) { selectedDate ->
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
        vm.setCurrentDate(formatter.format(date))
    }

    private fun getCurrentDateFormatted(): String {
        val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        return formatter.format(Date())
    }

    private fun getDateToAdapter(date :String): String{
        val inputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(date, inputFormatter)

        val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(outputFormatter)
    }

    private fun observer() {
        vm.getTaskListLiveData().observe(this){
            taskList->updateAdapter(taskList)
        }

        vm.getCurrentDateLiveData().observe(this) { s ->
            if (s.isNotEmpty()){
                if (s.equals(getCurrentDateFormatted())){
                    b_back.visibility= View.INVISIBLE
                }
                else {
                    b_back.visibility=View.VISIBLE
                }
                tv_date.text = s
                tasksAdapter.currentDate = getDateToAdapter(s)
                vm.getTaskListLiveData().value?.let { tasksAdapter.updateTasksList(it) }
            }
        }
    }

    private fun updateAdapter(tasks: List<TaskModel>){
        tasksAdapter.updateTasksList(tasks)
        var setDates:TreeSet<String> = TreeSet()

        setDates.addAll(tasks.map {it.date})
        calendarAdapter.setDates=setDates

    }

    private fun setOnClickListener() {
        b_add_new_task.setOnClickListener{
            startActivity(Intent(this,AddTaskActivity::class.java))
        }

        b_back.setOnClickListener {
            val currentDate = Date()
            vm.setCurrentDate(getCurrentDateFormatted())

            calendarAdapter.selectDate(currentDate)

            val todayIndex = calendarAdapter.dates.indexOfFirst {
                val cal1 = Calendar.getInstance().apply { time = it }
                val cal2 = Calendar.getInstance()
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                        cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                        cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
            }

            if (todayIndex != -1) {
                rv_calendar.scrollToPosition(todayIndex)
            }
        }
    }
}