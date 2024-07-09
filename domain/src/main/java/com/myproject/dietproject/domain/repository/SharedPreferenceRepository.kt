package com.myproject.dietproject.domain.repository

/**
 * 원래는 FoodDiaryRepository 내부에 생성할 까 했던 메서드들이지만 다른 클래스에서 사용할 가능성이 있고,
 * 독립적인 기능으로써 존재 해야한다고 생각했기 때문에 따로 만들었다.
 */
interface SharedPreferenceRepository {

    suspend fun setSharedPreferenceFileName(fileName: String)

    suspend fun getSharedPreferenceFavoriteState(key: String): String?

    suspend fun addSharedPreferenceFavoriteState(key: String, value: String?)

    suspend fun deleteSharedPreferenceFavoriteState(key: String)

}