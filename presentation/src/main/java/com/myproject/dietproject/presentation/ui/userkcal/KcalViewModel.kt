package com.myproject.dietproject.presentation.ui.userkcal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.error.NetworkError
import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.usecase.GetKcalUseCase
import com.myproject.dietproject.presentation.ui.util.toErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KcalViewModel @Inject constructor(
    private val useCase: GetKcalUseCase
) : ViewModel() {

    private val _kcalData = MutableLiveData<List<Kcal>?>()
    val kcalData : LiveData<List<Kcal>?>
        get() = _kcalData

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = _isError

    fun getKcalData(dESCKOR : String) {

        viewModelScope.launch {
            _isLoading.postValue(true)
            when (val result = useCase.invoke(dESCKOR)) {
                is NetworkResult.Success -> {
                    _kcalData.value = result.data
                    _isLoading.postValue(true)
                    _isError.postValue(true)
                }
                is NetworkResult.Error -> {

                }
            }
        }

    }

}