package com.myproject.dietproject.data.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDiaryDao {

//    @Query("SELECT * from foodDiary")
//    // Delete시에 UI의 즉각적인 변경을 위한 LiveData 반환을 하려했는데 Dao가 LiveData 반환하면 다 Null이 뜨는데?
//    fun getAllKcal(): List<FoodDiaryEntity>

    @Query("SELECT * from foodDiary")
    // Delete시에 UI의 즉각적인 변경을 위한 LiveData 반환을 하려했는데 Dao가 LiveData 반환하면 다 Null이 뜨는데?
    fun getAllKcal(): Flow<List<FoodDiaryEntity>>

    // 충돌 무시하는 파라미터가 있는데 내 앱의 구조 상 충돌이 일어날 것 같진 않다.
    @Insert
    suspend fun insertKcal(foodDiary: FoodDiaryEntity)

    @Query("DELETE FROM foodDiary WHERE foodName = :foodName")
    suspend fun deleteKcal(foodName: String)

}