package com.myproject.dietproject.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.usecase.GetKcalUseCase
import com.myproject.dietproject.ui.util.Event
import com.myproject.dietproject.ui.util.toErrorMessage
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val useCase: GetKcalUseCase
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


    fun getKcalData(descKor : String) {

        viewModelScope.launch {
            _isLoading.postValue(true)
            when (val result = useCase.invoke(descKor)) {
                is NetworkResult.Success -> {
                    _kcalData.value = result.data
                    _isLoading.postValue(false)
                    _isError.postValue(false)
                }

                is NetworkResult.Error -> {
                    val msg = result.errorType.toErrorMessage(getApplication(application).applicationContext)
                    _showToast.value = Event(msg)
                    _isLoading.postValue(false)
                    _isError.postValue(true)
                }
            }
        }
    }

}

