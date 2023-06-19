package com.myproject.dietproject.presentation.ui.calendar

import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.TodayKcalForCalendar
import com.myproject.dietproject.presentation.databinding.CalendarDetailListItemBinding

class CalendarDetailViewHolder(
    private val binding: CalendarDetailListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(listItem: TodayKcalForCalendar){

        binding.todayKcal = listItem

    }

}