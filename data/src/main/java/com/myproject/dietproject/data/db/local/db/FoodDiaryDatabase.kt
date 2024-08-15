package com.myproject.dietproject.data.db.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myproject.dietproject.data.db.local.dao.FoodDiaryDao
import com.myproject.dietproject.data.db.local.entity.FoodDiaryEntity

@Database(entities = [FoodDiaryEntity::class], version = 4, exportSchema = false)
abstract class FoodDiaryDatabase: RoomDatabase() {
    abstract fun kcalDao(): FoodDiaryDao

}