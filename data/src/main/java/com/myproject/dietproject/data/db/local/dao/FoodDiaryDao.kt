package com.myproject.dietproject.data.db.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity

@Dao
interface FoodDiaryDao {

    @Query("SELECT * from foodDiary")
    fun getAllKcal(): List<FoodDiaryEntity>

    // 충돌 무시하는 파라미터가 있는데 내 앱의 구조 상 충돌이 일어날 것 같진 않다.
    @Insert
    suspend fun insertKcal(foodDiary: FoodDiaryEntity)

    @Delete
    suspend fun deleteKcal(foodDiary: FoodDiaryEntity)

}