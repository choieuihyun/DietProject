package com.myproject.dietproject.presentation.ui.userkcal

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.presentation.databinding.KcalListItemTypeTwoBinding

class KcalViewHolderTypeTwo(
    private val binding: KcalListItemTypeTwoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(userKcal: Kcal) {

        binding.kcalData = userKcal

    }

}