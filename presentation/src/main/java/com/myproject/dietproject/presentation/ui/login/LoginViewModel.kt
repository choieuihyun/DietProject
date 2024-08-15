package com.myproject.dietproject.presentation.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.myproject.dietproject.domain.usecase.AddUserInfoUseCase
import com.myproject.dietproject.domain.usecase.AddUserUseCase
import com.myproject.dietproject.domain.usecase.GetUserActivityUseCase
import com.myproject.dietproject.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getUserActivityUseCase: GetUserActivityUseCase,
    private val addUserInfoUseCase: AddUserInfoUseCase
): ViewModel() {

    private var _userGender: String = ""
    val userGender: String
        get() = _userGender

    private var _userAge: Int = 0
    val userAge: Int
        get() = _userAge

    private var _userHeight: Float = 0.0F
    val userHeight: Float
        get() = _userHeight

    private var _userWeight: Float = 0.0F
    val userWeight: Float
        get() = _userWeight

    private var _userActivity: String = ""
    val userActivity: String
        get() = _userActivity

    private val _loginUserActivity = MutableLiveData<Any>()
    val loginUserActivity: LiveData<Any>
        get() = _loginUserActivity

    fun addUser(userId: String, userEmail: String) {

        viewModelScope.launch {

            addUserUseCase(userId, userEmail)

        }
    }

    fun getUser(userId: String) {

        viewModelScope.launch {

            getUserUseCase(userId).addValueEventListener(object :
                ValueEventListener { // 데이터 변화를 감지하는 리스너이기 때문에
                // 회원가입에만 반응해야 하는데 왜 구글 로그인에는 다 반응함?
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("getUserByViewModel", snapshot.getValue(UserModel::class.java).toString())

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }

    fun getUserActivity(userId: String) {

        viewModelScope.launch {

                getUserActivityUseCase(userId).get().addOnSuccessListener {

                    //_loginUserActivity = it.value
                    _loginUserActivity.postValue(it.value)

                }.addOnFailureListener {
                    // 실패 처리
                }


        }

    }

    }

