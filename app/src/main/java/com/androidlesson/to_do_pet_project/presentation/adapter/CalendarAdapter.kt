package com.androidlesson.to_do_pet_project.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidlesson.to_do_pet_project.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TreeSet

class CalendarAdapter(
    var dates: List<Date>,
    initialSelectedPosition: Int,
    var setDates: TreeSet<String>,
    private val onDateSelected: (Date) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    var selectedPosition = initialSelectedPosition


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return CalendarViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: CalendarViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val date = dates[position]
        val calendar = Calendar.getInstance().apply { time = date }

        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val stringDate= formatter.format(date)

        holder.tvDate.text = calendar.get(Calendar.DAY_OF_MONTH).toString()


        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_button)
        } else {
            if (setDates.contains(stringDate)){
                holder.itemView.setBackgroundResource(R.drawable.bg_violet_calendar_item)
            }
            else holder.itemView.setBackgroundResource(R.drawable.bg_grey_circle)
        }

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onDateSelected(date)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectDate(date: Date) {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val selectedDateStr = formatter.format(date)

        val index = dates.indexOfFirst {
            formatter.format(it) == selectedDateStr
        }

        if (index != -1 && index != selectedPosition) {
            val previousSelected = selectedPosition
            selectedPosition = index
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onDateSelected(dates[selectedPosition])
        }
    }

    override fun getItemCount(): Int = dates.size

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }
}