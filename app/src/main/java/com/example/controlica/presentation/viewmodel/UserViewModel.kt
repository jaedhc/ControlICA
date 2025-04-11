package com.example.controlica.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.controlica.domain.use_case.auth.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
): ViewModel(){

    suspend fun logOut(){
        logOutUseCase()
    }

}