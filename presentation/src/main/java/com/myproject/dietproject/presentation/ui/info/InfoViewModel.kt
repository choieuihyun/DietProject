package com.myproject.dietproject.presentation.ui.info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.usecase.GetUserEmailUseCase
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserWeekKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(

    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase,
    private val getUserNumerousKcalByFoodUseCase: GetUserWeekKcalUseCase,
    private val getUserNumerousKcalByDateUseCase: GetUserWeekKcalUseCase

) : ViewModel() {

    private var _email: MutableLiveData<String> = MutableLiveData<String>()
    val email : LiveData<String>
        get() = _email

    private var _recommendKcal: MutableLiveData<String> = MutableLiveData<String>()
    val recommendKcal : LiveData<String>
        get() = _recommendKcal

    private var _mostNumerousFood: MutableLiveData<Int> = MutableLiveData()
    val mostNumerousFood: MutableLiveData<Int>
        get() = _mostNumerousFood

    private var _mostNumerousDate: MutableLiveData<String?> = MutableLiveData()
    val mostNumerousDate: MutableLiveData<String?>
        get() = _mostNumerousDate

    private var _dayKcal: MutableLiveData<Int?> = MutableLiveData()
    val dayKcal: MutableLiveData<Int?>
        get() = _dayKcal


    fun getUserEmail(userId: String) {

        viewModelScope.launch {

            getUserEmailUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    _email.postValue(snapshot.value.toString())
                    Log.d("infoEmail", email.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }

    fun getUserRecommendKcal(userId: String) {

        viewModelScope.launch {

            getUserRecommendKcalUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    _recommendKcal.postValue(snapshot.value.toString())
                    Log.d("infoEmail", recommendKcal.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }

    fun getUserMaxKcal(userId: String) { // 원래 이런곳에서 다 해도 되는데 그러면 유지보수할 때 머리가 띵할듯.

        viewModelScope.launch {

            getUserNumerousKcalByFoodUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val iterator = snapshot.children.iterator() // NumerousDate 처럼 구현해도 되고 Iterator를 사용해서 구현해도 된다. 좀 더 명시적일뿐? 차이 없어보임.

                        var max = 0

                        while(iterator.hasNext()) {

                            val childSnapshot = iterator.next()

                            val childValue = childSnapshot.child("kcal").value.toString().toInt()

                            if(childValue > max)
                                max = childValue

                            Log.d("iterator", childValue.toString())
                        }

                        _mostNumerousFood.postValue(max)
                        Log.d("iteratorMax", _mostNumerousFood.value.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }

    }

    fun getMostNumerousDate(userId: String) {

        viewModelScope.launch {

            getUserNumerousKcalByDateUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val dateTotalMap: Multimap<String, Int?> = ArrayListMultimap.create() // Guava multiMap

                    for (childSnapshot in snapshot.children) { // Iterator를 사용할 필요가 없음.

                        val date = childSnapshot.key.toString()
                        val kcal = childSnapshot.child("kcal").value.toString().toInt()

                        val dateSubstring = date.substring(0,10)
                        dateTotalMap.put(dateSubstring, kcal)

                    }


                    val mergedMap = mutableMapOf<String, Int?>()

                    for(entry in dateTotalMap.entries()) {

                        val date = entry.key
                        val kcal = entry.value
                        val sum = mergedMap[date]?.plus(kcal!!) ?: kcal
                        mergedMap[date] = sum

                    }


                    var maxDate: String? = null
                    var maxKcal = 0
                    var sumKcal = 0

                    for((date, kcal) in mergedMap) {
                        if(kcal != null && kcal > maxKcal) {
                            maxDate = date
                            maxKcal = kcal
                        }

                        if (kcal != null) {
                            sumKcal += kcal
                        }

                    }

                    _mostNumerousDate.postValue(maxDate)
                    _dayKcal.postValue(sumKcal / mergedMap.size)

                    Log.d("sdfsdf", sumKcal.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

}

