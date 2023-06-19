package com.myproject.dietproject.presentation.ui.calendar

import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.TodayKcal
import com.myproject.dietproject.domain.model.TodayKcalForCalendar
import com.myproject.dietproject.presentation.databinding.CalendarDetailListItemBinding
import com.myproject.dietproject.presentation.databinding.CalendarDetailListItemTypeTwoBinding

class CalendarDetailViewHolderTypeTwo(
    private val binding: CalendarDetailListItemTypeTwoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(listItem: TodayKcalForCalendar) {

        binding.todayKcal = listItem

    }

}