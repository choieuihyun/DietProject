package com.myproject.dietproject.presentation.ui.calendar

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.myproject.dietproject.domain.model.KcalDataForCalendar

import com.myproject.dietproject.presentation.databinding.CalendarDetailListItemBinding
import com.myproject.dietproject.presentation.databinding.CalendarDetailListItemTypeTwoBinding

class CalendarDetailAdapter : ListAdapter<KcalDataForCalendar, ViewHolder>(kcalDataDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when(viewType) {

            type_one -> {
                CalendarDetailViewHolder(CalendarDetailListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            type_two -> {
                CalendarDetailViewHolderTypeTwo(CalendarDetailListItemTypeTwoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ))
            }
            else -> {
                CalendarDetailViewHolderTypeTwo(CalendarDetailListItemTypeTwoBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ))
            }

        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val todayKcal = currentList[position]

        when(holder) {

            is CalendarDetailViewHolder -> holder.bind(todayKcal)

            is CalendarDetailViewHolderTypeTwo -> holder.bind(todayKcal)

        }

    }


    companion object {

        const val type_one = 1
        const val type_two = 2

        private val kcalDataDiffCallback = object : DiffUtil.ItemCallback<KcalDataForCalendar>() {

            override fun areItemsTheSame(oldItem: KcalDataForCalendar, newItem: KcalDataForCalendar): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: KcalDataForCalendar, newItem: KcalDataForCalendar): Boolean {
                return oldItem == newItem
            }


        }

    }


}