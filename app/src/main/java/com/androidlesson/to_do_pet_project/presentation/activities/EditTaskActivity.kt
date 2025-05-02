package com.androidlesson.to_do_pet_project.presentation.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.to_do_pet_project.R
import com.androidlesson.to_do_pet_project.app.App
import com.androidlesson.to_do_pet_project.presentation.viewModel.addTaskActivityViewModel.AddTaskActivityViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.editTaskActivityViewModel.EditTaskActivityViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class EditTaskActivity : AppCompatActivity() {
    private lateinit var vm: TaskViewModel
    @Inject
    lateinit var vmFactory: TaskViewModelFactory

    private lateinit var activityVM: EditTaskActivityViewModel

    private lateinit var et_task_title:EditText
    private lateinit var et_task_notes:EditText
    private lateinit var b_pick_date:RelativeLayout
    private lateinit var b_pick_time:RelativeLayout
    private lateinit var b_add_new_task:TextView
    private lateinit var tv_time:TextView
    private lateinit var tv_date:TextView
    private lateinit var iv_pick_category_first:ImageView
    private lateinit var iv_pick_category_second:ImageView
    private lateinit var iv_pick_category_third:ImageView
    private lateinit var iv_back:ImageView

    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        (application as App).appComponent?.injectEditTaskActivity(this)

        // ViewModel
        vm = ViewModelProvider(this, vmFactory).get(TaskViewModel::class.java)
        activityVM = ViewModelProvider(this).get(EditTaskActivityViewModel::class.java)

        // UI
        setBackGround()
        initialization()
        setOnClickListener()
        observable()
    }

    private fun initialization() {
        val task = intent.getSerializableExtra("task") as? TaskModel

        if (task != null) {
            activityVM.taskModel = task
        }

        et_task_notes=findViewById(R.id.et_task_notes)
        et_task_title=findViewById(R.id.et_task_title)
        b_pick_date=findViewById(R.id.b_pick_date)
        b_pick_time=findViewById(R.id.b_pick_time)
        b_add_new_task=findViewById(R.id.b_add_new_task)
        tv_time=findViewById(R.id.tv_time)
        tv_date=findViewById(R.id.tv_date)
        iv_pick_category_third=findViewById(R.id.iv_pick_category_third)
        iv_pick_category_second=findViewById(R.id.iv_pick_category_second)
        iv_pick_category_first=findViewById(R.id.iv_pick_category_first)
        iv_back=findViewById(R.id.iv_back)

        et_task_title.setText(activityVM.taskModel.taskTitle)
        et_task_notes.setText(activityVM.taskModel.notes)
        tv_date.text = activityVM.taskModel.date
        tv_time.text = activityVM.taskModel.time
        activityVM.pickCategory(activityVM.taskModel.category)
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


    private fun setOnClickListener() {
        b_pick_date.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                calendar.set(Calendar.YEAR, y)
                calendar.set(Calendar.MONTH, m)
                calendar.set(Calendar.DAY_OF_MONTH, d)
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                tv_date.text = dateFormat.format(calendar.time)
                activityVM.taskModel.date=dateFormat.format(calendar.time)
            }, year, month, day).show()
        }

        b_pick_time.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, h, m ->
                calendar.set(Calendar.HOUR_OF_DAY, h)
                calendar.set(Calendar.MINUTE, m)

                val timeString = String.format("%02d:%02d", h, m)
                tv_time.text = timeString
                activityVM.taskModel.time=timeString
            }, hour, minute, true).show()
        }

        iv_pick_category_first.setOnClickListener{
            activityVM.pickCategory("first")
        }

        iv_pick_category_second.setOnClickListener{
            activityVM.pickCategory("second")
        }

        iv_pick_category_third.setOnClickListener{
            activityVM.pickCategory("third")
        }

        iv_back.setOnClickListener{
            finish()
        }

        b_add_new_task.setOnClickListener{
            val title=et_task_title.text.toString()
            val notes=et_task_notes.text.toString()
            val date=activityVM.taskModel.date
            val time=activityVM.taskModel.time
            val category=activityVM.getCategoryLiveData().value

            if (title.isNotEmpty() && notes.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && category!!.isNotEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    vm.editTask(TaskModel(activityVM.taskModel.id, title, category, date, time, notes, activityVM.taskModel.isDone))
                    finish()
                }
            }
        }
    }

    private fun observable(){
        activityVM.getCategoryLiveData().observe(this){
                item->pickCategory(item)
        }
    }

    private fun pickCategory(category: String){
        iv_pick_category_first.setBackgroundResource(R.drawable.bg_grey_circle)
        iv_pick_category_second.setBackgroundResource(R.drawable.bg_grey_circle)
        iv_pick_category_third.setBackgroundResource(R.drawable.bg_grey_circle)

        iv_pick_category_first.setColorFilter(Color.WHITE)
        iv_pick_category_second.setColorFilter(Color.WHITE)
        iv_pick_category_third.setColorFilter(Color.WHITE)

        when (category) {
            "first" -> {
                iv_pick_category_first.setBackgroundResource(R.drawable.bg_blue_circle)
                iv_pick_category_first.clearColorFilter()
            }

            "second" -> {
                iv_pick_category_second.setBackgroundResource(R.drawable.bg_violet_circle)
                iv_pick_category_second.clearColorFilter()
            }

            "third" -> {
                iv_pick_category_third.setBackgroundResource(R.drawable.bg_yellow_circle)
                iv_pick_category_third.clearColorFilter()
            }
        }
    }
}