package com.myproject.dietproject.data.mapper

import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity
import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.model.Kcal

fun FoodDiaryEntity.toModel() = FoodDiaryModel(

    id = id,
    kcal = kcal,
    foodName = foodName,
    makerName = makerName,
    favoriteButtonState = favoriteButtonState

)


// 사실 위아래의 매핑이 같은 이유는 비즈니스 모델에 뭐 별로 달라지는게 없어서임. 실제로 달라진다면 내가 커스텀해서 바꿔야겠지.
fun FoodDiaryModel.toEntity() = FoodDiaryEntity(

    id = id,
    kcal = kcal,
    foodName = foodName,
    makerName = makerName,
    favoriteButtonState = favoriteButtonState

)
