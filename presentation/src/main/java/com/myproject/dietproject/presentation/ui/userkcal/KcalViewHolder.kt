package com.myproject.dietproject.presentation.ui.userkcal

import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.presentation.databinding.KcalListItemBinding

class KcalViewHolder(
    private val binding: KcalListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userKcal: Kcal) {

        binding.kcalData = userKcal

    }

}