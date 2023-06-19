package com.myproject.dietproject.presentation.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.usecase.GetKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserUseCase
import com.myproject.dietproject.presentation.ui.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val getKcalUseCase: GetKcalUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getUserTodayKcalUseCase: GetUserTodayKcalUseCase,
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase
    private val addUserTodayKcalUseCase: AddUserTodayKcalUseCase
) : ViewModel() {

    private val _kcalData = MutableLiveData<List<Kcal>?>()
    val kcalData: LiveData<List<Kcal>?> = _kcalData

    // 1회 섭취 총 칼로리 평균내서 음식 누르면 추가하게 하려고 했는뎅..
    private var _kcalNUTRCONT = MutableLiveData<Float?>()
    val kcalNUTRCONT: LiveData<Float?> = _kcalNUTRCONT

    private val _showToast: MutableLiveData<Event<String>> = MutableLiveData()
    val showToast: LiveData<Event<String>> = _showToast

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = _isError

    private var _todayKcal: MutableLiveData<Float> = MutableLiveData()
    val todayKcal: LiveData<Float> = _todayKcal

    private var _recommendKcal: MutableLiveData<Int> = MutableLiveData()
    val recommendKcal: LiveData<Int> = _recommendKcal

    private var _scarceKcal: MutableLiveData<Int> = MutableLiveData()
    val scarceKcal: LiveData<Int> = _scarceKcal

    private var calculTodayKcal: Float = 0.0F
    private var calculRecommendKcal = 0

    fun getUserTodayKcalData(userId: String) {

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormat.format(calendar.time)

        viewModelScope.launch(Dispatchers.IO) {

            var sum = 0.0F

            getUserTodayKcalUseCase(userId, today).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        val dataDate = data.key?.substring(0, 10)

                        if (dataDate == today) {
                            val kcal = data.child("kcal").value
                            sum += kcal.toString().toFloat()
                        }
                    }
                    _todayKcal.value = sum
                    calculTodayKcal = sum
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }

    }

    fun getRecommendKcalData(userId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            getUserRecommendKcalUseCase(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    _recommendKcal.value = snapshot.value.toString().toInt()
                    calculRecommendKcal = _recommendKcal.value.toString().toInt()
                    _scarceKcal.value = calculRecommendKcal - calculTodayKcal.toInt()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })

        }

    }




}

