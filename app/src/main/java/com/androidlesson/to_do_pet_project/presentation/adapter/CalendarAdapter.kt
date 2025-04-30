package com.androidlesson.to_do_pet_project.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidlesson.to_do_pet_project.R
import java.util.Calendar
import java.util.Date

class CalendarAdapter(
    var dates: List<Date>,
    initialSelectedPosition: Int,
    private val onDateSelected: (Date) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition = initialSelectedPosition

    fun updateDates(newDates: List<Date>) {
        val oldSize = dates.size
        dates = newDates
        notifyItemRangeInserted(0, newDates.size - oldSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val date = dates[position]
        val calendar = Calendar.getInstance().apply { time = date }

        holder.tvDate.text = calendar.get(Calendar.DAY_OF_MONTH).toString()


        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_button)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_grey_circle)
        }

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onDateSelected(date)
        }
    }

    override fun getItemCount(): Int = dates.size

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }
}