package com.myproject.dietproject.presentation.ui.info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.usecase.GetUserEmailUseCase
import com.myproject.dietproject.domain.usecase.GetUserRecommendKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserTodayKcalUseCase
import com.myproject.dietproject.domain.usecase.GetUserWeekKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(

    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val getUserRecommendKcalUseCase: GetUserRecommendKcalUseCase,
    private val getUserWeekKcalUseCase: GetUserWeekKcalUseCase

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

    fun getUserMaxKcal(userId: String) {

        viewModelScope.launch {

            getUserWeekKcalUseCase(userId).addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val iterator = snapshot.children.iterator()

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
/*                        var max = 0
                        Log.d("numerousChildren", snapshot.value.toString())

                        for (data in snapshot.children) {

                            val kcal = snapshot.child("kcal").value

                            if(kcal > max)
                                max = kcal
                            Log.d("numerousKcal", snapshot.children.iterator().next())
                            Log.d("numerousFood", kcal.toString())
                        }
                        _mostNumerousFood = max*/
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }

    }


}

