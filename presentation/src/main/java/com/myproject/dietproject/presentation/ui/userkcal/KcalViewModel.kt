package com.myproject.dietproject.presentation.ui.userkcal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.domain.error.NetworkResult
import com.myproject.dietproject.domain.model.FoodDiaryModel
import com.myproject.dietproject.domain.model.Kcal
import com.myproject.dietproject.domain.usecase.AddFoodDiaryListUseCase
import com.myproject.dietproject.domain.usecase.AddSharedPreferenceFavoriteStateUseCase
import com.myproject.dietproject.domain.usecase.DeleteFoodDiaryUseCase
import com.myproject.dietproject.domain.usecase.DeleteSharedPreferenceFavoriteState
import com.myproject.dietproject.domain.usecase.GetFoodDiaryListUseCase
import com.myproject.dietproject.domain.usecase.GetKcalUseCase
import com.myproject.dietproject.domain.usecase.GetSharedPreferenceFavoriteStateUseCase
import com.myproject.dietproject.domain.usecase.SetPreferenceFileNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class KcalViewModel @Inject constructor(
    private val useCase: GetKcalUseCase,
    private val addFoodDiaryUseCase: AddFoodDiaryListUseCase,
    private val deleteFoodDiaryUseCase: DeleteFoodDiaryUseCase,
    private val getFavoriteListUseCase: GetFoodDiaryListUseCase,
    private val setSharedPreferenceFavoriteStateUseCase: SetPreferenceFileNameUseCase,
    private val addSharedPreferenceFavoriteStateUseCase: AddSharedPreferenceFavoriteStateUseCase,
    private val getSharedPreferenceFavoriteStateUseCase: GetSharedPreferenceFavoriteStateUseCase,
    private val deleteSharedPreferenceFavoriteStateUseCase: DeleteSharedPreferenceFavoriteState,
) : ViewModel() {

    private val _kcalData = MutableLiveData<List<Kcal>?>()
    val kcalData: LiveData<List<Kcal>?>
        get() = _kcalData

    private val _favoriteList = MutableLiveData<List<FoodDiaryModel>?>()
    val favoriteList: LiveData<List<FoodDiaryModel>?>
        get() = _favoriteList

    // flow로 교체하면서 데이터가 실시간으로 첨삭은 되는데 버튼 상태에 대한 데이터가 갱신이 안된다.
    val flowTest = getFavoriteListUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = _isError

    var favoriteButtonState: Boolean = false

    var userId: String = ""

    // 뷰에 다시 들어올 때 리스트 초기화
    fun getEmptyKcalData() {

        viewModelScope.launch {
            _kcalData.value = emptyList()
        }

    }

    fun getKcalData(dESCKOR: String) {

        viewModelScope.launch {
            _isLoading.postValue(true)

            when (val result = useCase.invoke(dESCKOR)) {
                is NetworkResult.Success -> {

                    for (item in result.data ?: emptyList()) {
                        if (item.dESCKOR == getSharedPreferenceFavoriteStateUseCase(item.dESCKOR!!)) {
                            Log.d(
                                "KcalViewModel",
                                getSharedPreferenceFavoriteStateUseCase(item.dESCKOR!!)!!
                            )
                            item.favoriteButtonState = true
                        }
                    }
                    Log.d("KcalViewModelItem", result.data.toString())
                    _kcalData.value = result.data

                    _isLoading.postValue(false)
                    _isError.postValue(false)
                }

                is NetworkResult.Error -> {
                    //val msg = result.errorType.toErrorMessage(Contexts.getApplication(application).applicationContext)
                    _isLoading.postValue(false)
                    _isError.postValue(true)
                }
            }
        }

    }

//    fun getFavoriteList() {
//
//        /**
//        와 viewModelScope Dispathcers.IO 설정 후
//        postValue로 백그라운드에서 데이터를 삽입해야 그 메인 스레드 블로킹 에러가 안뜸. 현재 해결책이 이거뿐이다.
//        이대로 구현하면 바로 즐겨찾기에 갱신이 안됨. 근데 TabLayout에 Fragment를 끼웠을 때, 왔다갔다 할 시 onPause와 onResume의 생명주기를 거치는데
//        검색 -> 즐겨찾기로 처음 이동할 때는 즐겨찾기의 ViewCreated가 실행되어 즐겨찾기 갱신이 되지만 두번째부터는 onPause와 onResume으로만 작동하기 때문에
//        갱신이 안되는 것 같은데?????
//         **/
//
//
//            val favoriteFoodList = getFavoriteListUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
//
//            favoriteList.value = favoriteFoodList
//
//        }
//

/*    fun getFavoriteList() {

        *//**
        와 viewModelScope Dispathcers.IO 설정 후
        postValue로 백그라운드에서 데이터를 삽입해야 그 메인 스레드 블로킹 에러가 안뜸. 현재 해결책이 이거뿐이다.
        이대로 구현하면 바로 즐겨찾기에 갱신이 안됨. 근데 TabLayout에 Fragment를 끼웠을 때, 왔다갔다 할 시 onPause와 onResume의 생명주기를 거치는데
        검색 -> 즐겨찾기로 처음 이동할 때는 즐겨찾기의 ViewCreated가 실행되어 즐겨찾기 갱신이 되지만 두번째부터는 onPause와 onResume으로만 작동하기 때문에
        갱신이 안되는 것 같은데?????
         **//*
        viewModelScope.launch(Dispatchers.IO) {

            val favoriteFoodList = getFavoriteListUseCase()

            for (item in favoriteFoodList ?: emptyList()) {
                if (item.foodName == getSharedPreferenceFavoriteStateUseCase(item.foodName!!)) {
                    item.favoriteButtonState = true
                }
            }
            Log.d("sdfsdfViewModel", favoriteFoodList.toString())


            withContext(Dispatchers.Main) {
                _favoriteList.value = favoriteFoodList
            }

        }

    }*/

    fun resetFavoriteList() {

        viewModelScope.launch {

                val favoriteList = getFavoriteListUseCase()
                // _favoriteList.postValue(favoriteList)


        }
    }


    /**
     * 즐겨찾기 리스트에 추가하는 메서드. 근데 foodDiary라고 이름 지은 이유가 뭐지?
     */
    fun addFoodDiary(foodDiaryModel: FoodDiaryModel) {

        viewModelScope.launch {

            addFoodDiaryUseCase(foodDiaryModel)

        }

    }

    fun deleteFoodDiary(foodName: String) {

        viewModelScope.launch {

            Log.d("deleteFoodDiary", foodName.toString())
            deleteFoodDiaryUseCase(foodName)

        }

    }

    /**
     * 즉각적인 뷰 갱신을 위한 코드들인데..
     *
     */
    fun addFavoriteItem(foodDiaryModel: FoodDiaryModel) {
        viewModelScope.launch {
            addFoodDiary(foodDiaryModel)
            addSharedPreferenceFavoriteState(foodDiaryModel.foodName!!, foodDiaryModel.foodName)
            updateFavoriteItem(foodDiaryModel)
        }
    }

    fun removeFavoriteItem(foodName: String) {
        viewModelScope.launch {
            deleteFoodDiary(foodName)
            deleteSharedPreferenceFavoriteState(foodName)
            removeFavoriteItemFromList(foodName)
        }
    }

    private fun updateFavoriteItem(updatedItem: FoodDiaryModel) {
        val currentList = _favoriteList.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.foodName == updatedItem.foodName }
        if (index != -1) {
            currentList[index] = updatedItem
            _favoriteList.postValue(currentList)
        } else {
            currentList.add(updatedItem)
            _favoriteList.postValue(currentList)
        }
    }

    private fun removeFavoriteItemFromList(foodName: String) {
        val currentList = _favoriteList.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.foodName == foodName }
        _favoriteList.postValue(currentList)
    }



    fun setSharedPreference(fileName: String) {

        viewModelScope.launch {
            setSharedPreferenceFavoriteStateUseCase(fileName)
        }

    }

    fun getSharedPreferenceFavoriteState(key: String) {

//        viewModelScope.launch {
//            getSharedPreferenceFavoriteStateUseCase(key)
//        }

    }


    fun addSharedPreferenceFavoriteState(key: String, value: String?) {

        viewModelScope.launch {
            addSharedPreferenceFavoriteStateUseCase(key, value)
        }

    }

    fun deleteSharedPreferenceFavoriteState(key: String) {

        viewModelScope.launch {
            deleteSharedPreferenceFavoriteStateUseCase(key)
        }

    }


}