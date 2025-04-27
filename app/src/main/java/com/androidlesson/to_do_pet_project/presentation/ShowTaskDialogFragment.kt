package com.androidlesson.to_do_pet_project.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.androidlesson.domain.models.TaskModel
import com.androidlesson.to_do_pet_project.R
class ShowTaskDialogFragment : DialogFragment() {

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
        task = arguments?.getSerializable(ARG_TASK) as? TaskModel
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private var task: TaskModel? = null


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

        val titleTextView = view.findViewById<TextView>(R.id.tv_task_title)
        val descriptionTextView = view.findViewById<TextView>(R.id.tv_task_notes)

        titleTextView.text = task?.taskTitle
        descriptionTextView.text = task?.notes
    }
}