package com.example.controlica.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.controlica.core.di.AppSession
import com.example.controlica.data.model.users.CurrentEmployeeData
import com.example.controlica.domain.use_case.auth.CurrentSessionUseCase
import com.example.controlica.domain.use_case.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val currentSessionUseCase: CurrentSessionUseCase,
    private val appSession: AppSession
): ViewModel() {

    // LOGIN SCREEN --------------------------------------------------------------------------------
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableStateFlow<Result<CurrentEmployeeData?>>(Result.success(null))
    val loginResult: StateFlow<Result<CurrentEmployeeData?>> = _loginResult

    fun onLoginChanged(email: String, password: String){
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPass(password)
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPass(password: String): Boolean = password.length > 5

    suspend fun authenticateUser(){
        _isLoading.value = true
        _loginResult.value = loginUseCase(_email.value.toString(), _password.value.toString())
        _loginResult.value.getOrNull()?.employee?.let {
            appSession.setUser(
                it.name,
                it.id,
                it.photoUrl,
                it.role == "admin"
            )
        }
        _isLoading.value = false
    }

    //SPLASH SCREEN --------------------------------------------------------------------------------

    private val _isFetching = MutableStateFlow<Boolean>(true)
    val isFetching: StateFlow<Boolean> = _isFetching

    private val _currentSession = MutableStateFlow<Result<CurrentEmployeeData?>>(Result.success(null))
    val currentSession: StateFlow<Result<CurrentEmployeeData?>> = _currentSession

    suspend fun getCurrentSesion(){
        _isFetching.value = true
        _currentSession.value = currentSessionUseCase()
        _currentSession.value.getOrNull()?.employee?.let {
            appSession.setUser(
                it.name,
                it.id,
                it.photoUrl,
                it.role == "admin"
            )
        }
        _isFetching.value = false
    }

}