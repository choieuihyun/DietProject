package com.myproject.dietproject.presentation.ui.userkcal

import androidx.recyclerview.widget.RecyclerView
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.presentation.R
import com.myproject.dietproject.presentation.databinding.KcalFavoriteListItemBinding
import com.myproject.dietproject.presentation.databinding.KcalFavoriteListItemTypeTwoBinding

class KcalFavoriteViewHolderTypeTwo (
    private val binding: KcalFavoriteListItemTypeTwoBinding
) : RecyclerView.ViewHolder(binding.root) {

    val imageButton = binding.favoriteButton

    fun bind(foodDiaryModel: FoodDiaryModel) {

        binding.foodDiaryModel = foodDiaryModel
        if(foodDiaryModel.favoriteButtonState) {
            imageButton.isSelected = true
            imageButton.setBackgroundResource(R.drawable.star_filled)
        } else {
            imageButton.isSelected = false
            imageButton.setBackgroundResource(R.drawable.star_border)
        }

    }

}