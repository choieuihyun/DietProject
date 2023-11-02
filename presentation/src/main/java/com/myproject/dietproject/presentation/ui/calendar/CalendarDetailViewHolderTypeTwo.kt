package com.myproject.dietproject.presentation.ui.calendar

import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.KcalDataForCalendar
import com.myproject.dietproject.presentation.databinding.CalendarDetailListItemTypeTwoBinding

class CalendarDetailViewHolderTypeTwo(
    private val binding: CalendarDetailListItemTypeTwoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(listItem: KcalDataForCalendar) {

        binding.todayKcal = listItem

    }

}