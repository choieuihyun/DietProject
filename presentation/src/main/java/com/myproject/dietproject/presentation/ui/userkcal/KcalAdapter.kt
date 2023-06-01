package com.myproject.dietproject.presentation.ui.userkcal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.myproject.dietproject.domain.model.Kcal
import androidx.recyclerview.widget.ListAdapter
import com.myproject.dietproject.presentation.databinding.KcalListItemBinding

class KcalAdapter : ListAdapter<Kcal, KcalViewHolder>(kcalDataDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KcalViewHolder {
        return KcalViewHolder(
            KcalListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: KcalViewHolder, position: Int) {
        val userKcal = currentList[position]
        holder.bind(userKcal)
    }

    companion object {

        private val kcalDataDiffCallback = object : DiffUtil.ItemCallback<Kcal>() {

            override fun areItemsTheSame(oldItem: Kcal, newItem: Kcal): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Kcal, newItem: Kcal): Boolean {
                return oldItem == newItem
            }


        }

    }



}