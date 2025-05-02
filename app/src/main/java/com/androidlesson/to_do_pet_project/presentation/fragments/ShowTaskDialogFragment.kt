package com.androidlesson.to_do_pet_project.presentation.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.to_do_pet_project.R
import com.androidlesson.to_do_pet_project.app.App
import com.androidlesson.to_do_pet_project.presentation.activities.EditTaskActivity
import com.androidlesson.to_do_pet_project.presentation.viewModel.showTaskDialogFragmentViewModel.ShowTaskDialogFragmentViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.showTaskDialogFragmentViewModel.ShowTaskDialogFragmentViewModelFactory
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModel
import com.androidlesson.to_do_pet_project.presentation.viewModel.taskViewModel.TaskViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShowTaskDialogFragment : DialogFragment() {

    private lateinit var iv_delete_task: ImageView
    private lateinit var iv_edit_task: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var tv_date: TextView
    private lateinit var tv_time: TextView

    private lateinit var vm: TaskViewModel
    @Inject
    lateinit var vmFactory: TaskViewModelFactory

    private lateinit var fragmentVM: ShowTaskDialogFragmentViewModel
    @Inject
    lateinit var fragmentVMFactory: ShowTaskDialogFragmentViewModelFactory

    companion object {
        private const val ARG_TASK = "arg_task"

        fun newInstance(task: TaskModel): ShowTaskDialogFragment {
            val fragment = ShowTaskDialogFragment()
            val args = Bundle()
            args.putSerializable(ARG_TASK, task)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getSerializable(ARG_TASK) as TaskModel
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    lateinit var task: TaskModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_dialog_show_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as App).appComponent?.injectShowTaskDialogFragment(this)

        // ViewModel
        vm = ViewModelProvider(requireActivity(), vmFactory).get(TaskViewModel::class.java)
        fragmentVM=ViewModelProvider(requireActivity(),fragmentVMFactory).get(ShowTaskDialogFragmentViewModel::class.java)

        fragmentVM.taskModel=task

        initializtion(view)

        setOnClickListener()
    }

    private fun initializtion(view: View) {
        iv_delete_task=view.findViewById(R.id.iv_delete_task)
        titleTextView = view.findViewById(R.id.tv_task_title)
        descriptionTextView = view.findViewById(R.id.tv_task_notes)
        tv_date=view.findViewById(R.id.tv_date)
        tv_time=view.findViewById(R.id.tv_time)
        iv_edit_task=view.findViewById(R.id.iv_edit_task)

        titleTextView.text = fragmentVM.taskModel.taskTitle
        descriptionTextView.text = fragmentVM.taskModel.notes
        tv_date.text=fragmentVM.taskModel.date
        tv_time.text=fragmentVM.taskModel.time
    }

    private fun setOnClickListener(){
        iv_delete_task.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch { vm.deleteTask(fragmentVM.taskModel) }
            dismiss()
        }

        iv_edit_task.setOnClickListener {
            val intent = Intent(requireActivity(), EditTaskActivity::class.java)
            intent.putExtra("task", fragmentVM.taskModel)
            startActivity(intent)
            dismiss()
        }
    }
}