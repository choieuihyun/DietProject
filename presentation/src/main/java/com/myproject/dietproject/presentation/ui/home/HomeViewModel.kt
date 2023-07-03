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

    private var _todayKcal: MutableLiveData<Float> = MutableLiveData() // 오늘 섭취한 총 칼로리
    val todayKcal: LiveData<Float> = _todayKcal

    private var _recommendKcal: MutableLiveData<Int> = MutableLiveData() // 일일 권장 칼로리
    val recommendKcal: LiveData<Int> = _recommendKcal

    private var _scarceKcal: MutableLiveData<Int> = MutableLiveData() // 부족한 섭취량 (Kcal)
    val scarceKcal: LiveData<Int> = _scarceKcal

    private var _homeDateText: MutableLiveData<String> = MutableLiveData() // 날짜 text
    val homeDateText: LiveData<String>
        get() = _homeDateText

    private var _homeDataByDate: MutableLiveData<String> = MutableLiveData() // 날짜 기준 데이터
    val homeDataByDate: LiveData<String>
        get() = _homeDataByDate

    private var _imageResultLiveData: MutableLiveData<Int> = MutableLiveData()
    val imageResultLiveData: LiveData<Int>
        get() = _imageResultLiveData

    private var calculTodayKcal: Float = 0.0F // 연산용도 오늘 섭취한 칼로리
    private var calculRecommendKcal = 0 // 연산용도 권장 칼로리

    private val calendar = Calendar.getInstance()

    fun getUserTodayKcalData(userId: String) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormat.format(calendar.time)

        var dateText = today.substring(5, 10) // 6-28로 자르기
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when (dayOfWeek) {

            Calendar.MONDAY -> dateText = "$dateText(월)"

            Calendar.TUESDAY -> dateText = "$dateText(화)"

            Calendar.WEDNESDAY -> dateText = "$dateText(수)"

            Calendar.THURSDAY -> dateText = "$dateText(목)"

            Calendar.FRIDAY -> dateText = "$dateText(금)"

            Calendar.SATURDAY -> dateText = "$dateText(토)"

            Calendar.SUNDAY -> dateText = "$dateText(일)"

        }

        Log.d("sdfsdf", dateText.toString())

        viewModelScope.launch(Dispatchers.IO) {

            var sum = 0.0F

            getUserTodayKcalUseCase(userId, today).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        val dataDate = data.key?.substring(0, 10)

                        if (dataDate == today) {
                            val kcal = data.child("kcal").value
                            sum += kcal.toString().toFloat()
                        }
                    }
                    _todayKcal.value = sum
                    calculTodayKcal = sum // 위에꺼랑 같은데?

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            _homeDateText.postValue(dateText)

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

    fun movePreviousDate(userId: String) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        calendar.add(Calendar.DAY_OF_MONTH, -1)

        val previousDataByDate = dateFormat.format(calendar.time)
        var previousDateText = dateFormat.format(calendar.time).substring(5, 10)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        Log.d("sdfsdf4", previousDateText)
        Log.d("sdfsdf5", previousDataByDate)

        viewModelScope.launch {

            var sum = 0.0F

            getUserTodayKcalUseCase(userId, previousDataByDate).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.children) {
                        val dataDate = data.key?.substring(0, 10)

                        if (dataDate == previousDataByDate) {
                            val kcal = data.child("kcal").value
                            sum += kcal.toString().toFloat()
                        }
                    }
                    _todayKcal.value = sum
                    calculTodayKcal = sum
                    _scarceKcal.value = calculRecommendKcal - calculTodayKcal.toInt()

                    imageSetting()
                    Log.d("sdeeee3", calculTodayKcal.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            when (dayOfWeek) {

                Calendar.MONDAY -> previousDateText = "$previousDateText(월)"

                Calendar.TUESDAY -> previousDateText = "$previousDateText(화)"

                Calendar.WEDNESDAY -> previousDateText = "$previousDateText(수)"

                Calendar.THURSDAY -> previousDateText = "$previousDateText(목)"

                Calendar.FRIDAY -> previousDateText = "$previousDateText(금)"

                Calendar.SATURDAY -> previousDateText = "$previousDateText(토)"

                Calendar.SUNDAY -> previousDateText = "$previousDateText(일)"

            }

            _homeDateText.postValue(previousDateText)
            _homeDataByDate.postValue(previousDataByDate)
        }

    }

    fun moveNextDate() {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val nextDataByDate = dateFormat.format(calendar.time)
        var nextDate = dateFormat.format(calendar.time).substring(5, 10)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        when (dayOfWeek) {

            Calendar.MONDAY -> nextDate = "$nextDate(월)"

            Calendar.TUESDAY -> nextDate = "$nextDate(화)"

            Calendar.WEDNESDAY -> nextDate = "$nextDate(수)"

            Calendar.THURSDAY -> nextDate = "$nextDate(목)"

            Calendar.FRIDAY -> nextDate = "$nextDate(금)"

            Calendar.SATURDAY -> nextDate = "$nextDate(토)"

            Calendar.SUNDAY -> nextDate = "$nextDate(일)"

        }

        viewModelScope.launch {
            _homeDateText.postValue(nextDate)
            _homeDataByDate.postValue(nextDataByDate)
        }

    }

    fun imageSetting() {

        viewModelScope.launch {

            if (_recommendKcal.value!! * 0.6 > _todayKcal.value?.toInt()!!) // 권장 섭취 칼로리 * 0.6 > 오늘 섭취 칼로리

                _imageResultLiveData.postValue(1)// 배고픈 이미지
            else

                _imageResultLiveData.postValue(2) // 배부른 이미지

        }
    }


}

