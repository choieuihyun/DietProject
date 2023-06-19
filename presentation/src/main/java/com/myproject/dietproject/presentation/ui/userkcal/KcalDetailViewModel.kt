package com.myproject.dietproject.presentation.ui.userkcal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.usecase.AddUserTodayKcalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class KcalDetailViewModel @Inject constructor(
    private val addUserTodayKcalUseCase : AddUserTodayKcalUseCase
) : ViewModel() {

    fun plusServingCalculator(serving: Float) : Float {

        var result = 0.0F

        result = serving + 0.5F

        return result

    }

    fun minusServingCalculator(serving: Float) : Float {

        var result = serving - 0.5F

        if(result <= 0)
            result = serving - 0.0F

        return result

    }

    fun plusCalculator(data: Float, number: Float): Float {

        return data * number

    }

    fun minusCalculator(data: Float, data2: Float): Float {

        val result = data - (data2 * 0.5F)

        if(result <= 0)
            return data

        return result

    }

    fun addUserTodayKcal(userId: String, kcal: Float, foodName: String, makerName: String) {

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormat.format(calendar.time)

        viewModelScope.launch {
            addUserTodayKcalUseCase(userId, floor(kcal), foodName, makerName, date.toString())
        }

    }


}