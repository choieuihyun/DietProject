package com.myproject.dietproject.presentation.ui.userkcal

import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalListItemTypeTwoBinding

class KcalViewHolderTypeTwo(
    private val binding: KcalListItemTypeTwoBinding
) : RecyclerView.ViewHolder(binding.root) {

    val imageButton = binding.favoriteButton
    fun bind(userKcal: Kcal) {

        binding.kcalData = userKcal
        if(userKcal.favoriteButtonState) {
            imageButton.isSelected = true
            imageButton.setBackgroundResource(R.drawable.star_filled)
        } else {
            imageButton.isSelected = false
            imageButton.setBackgroundResource(R.drawable.star_border)
        }

    }

}