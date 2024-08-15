package com.myproject.dietproject.domain.repository

import androidx.lifecycle.LiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.myproject.dietproject.domain.model.KcalDataForCalendar

interface FirebaseRepository {

    // 사용에 따라 여기서 선언해도 되는 변수가 있고 아닌 변수가 있다. 정신 차리고 해야함.
    // 안그러면 별 에러가 다 생김.
    val firebaseStorageReference: StorageReference

    val todayKcal: LiveData<Int>

    val homeDateText: LiveData<String> // 날짜 text -> 12-09 (금)

    val homeDateTextByDate: LiveData<String> // 전, 후 날짜 변경, 날짜 기준 데이터 -> 2023-12-09, 실제 날짜

    val calculTodayKcal: Int

    val recommendKcal: LiveData<Int> // 일일 권장 칼로리

    val scarceKcal: LiveData<Int> // 부족한 섭취량 (Kcal)

    val calculRecommendKcal: Int // 연산용도 권장 칼로리

    val homeDateTextByDate: LiveData<String> // 전, 후 날짜 변경

    val dayKcal: LiveData<Int>

    val dayKcalList: LiveData<ArrayList<KcalDataForCalendar?>>

    suspend fun test(value: String)

    suspend fun addUser(userId: String, userEmail: String)

    suspend fun getUser(userId: String) : DatabaseReference

    suspend fun getUserTodayKcal(userId: String)

    suspend fun getUserPreviousDateKcal(userId: String)

    suspend fun getUserNextDateKcal(userId: String)

    suspend fun getCalendarDetailData(userId: String, date: String)

    suspend fun getDayKcalList(): ArrayList<KcalDataForCalendar?>?

    suspend fun getDayKcal(): Int?

    suspend fun getUserWeekKcal(userId: String) : DatabaseReference

    suspend fun getUserName(userId: String) : DatabaseReference

    suspend fun getUserEmail(userId: String) : DatabaseReference

    suspend fun getUserRecommendKcal(userId: String)

    suspend fun getUserTargetWeight(userId: String) : DatabaseReference

    suspend fun getUserWeight(userId: String) : DatabaseReference

    suspend fun getUserOverKcal(userId: String) : DatabaseReference

    suspend fun getUserActivity(userId: String) : DatabaseReference

    suspend fun addUserInfo(userId: String,
                            name: String,
                            gender: String,
                            age: Int,
                            height: Float,
                            weight: Float,
                            targetWeight: Float,
                            recommendKcal: Int,
                            activity: String)

    suspend fun addTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String, date: String)

    suspend fun addOverKcal(userId: String, overKcal: Int)

    suspend fun getProfileImage(userId: String, path: String) : StorageReference

    suspend fun addProfileImage(userId: String, path: String)

}