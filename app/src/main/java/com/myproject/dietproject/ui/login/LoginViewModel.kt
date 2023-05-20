package com.myproject.dietproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myproject.dietproject.data.db.remote.response.kcalresponse.User
import com.myproject.dietproject.domain.usecase.GetUserDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDBUseCase: GetUserDB
): ViewModel(){


    fun getUserDB(userId: String, user: User) {

        viewModelScope.launch {

            userDBUseCase(userId, user)

        }
    }

}