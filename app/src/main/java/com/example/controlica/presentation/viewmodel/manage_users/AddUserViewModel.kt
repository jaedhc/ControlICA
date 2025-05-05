package com.example.controlica.presentation.viewmodel.manage_users

import android.media.Image
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.model.NewEmployee
import com.example.controlica.domain.model.Rol
import com.example.controlica.domain.use_case.auth.AddUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Thread.State
import javax.inject.Inject


@HiltViewModel
class AddUserViewModel @Inject constructor(
    val addUserUseCase: AddUserUseCase
): ViewModel(){

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _employeeNum = MutableStateFlow<String>("")
    val employeeNum: StateFlow<String> = _employeeNum

    private val _employeeName = MutableStateFlow<String>("")
    val employeeName: StateFlow<String> = _employeeName

    private val _employeeRol = MutableStateFlow<Rol?>(null)
    val employeeRol: StateFlow<Rol?> = _employeeRol

    private val _addEnable = MutableStateFlow<Boolean>(false)
    val addEnable: StateFlow<Boolean> = _addEnable

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _employeePhoto = MutableStateFlow<ByteArray?>(null)
    val employeePhoto: StateFlow<ByteArray?> = _employeePhoto

    private val _creationResult = MutableStateFlow<Result<Unit?>>(Result.success(null))
    val creationResult: StateFlow<Result<Unit?>> = _creationResult


    fun loadImage(image: ByteArray){
        _employeePhoto.value = image
    }

    fun onFormChanged(
        email: String,
        password: String,
        emploeeNum: String,
        employeeName: String,
        employeeRol: Rol?
        ){
        _email.value = email
        _password.value = password
        _employeeNum.value = emploeeNum
        _employeeName.value = employeeName
        _employeeRol.value = employeeRol
        _addEnable.value = isValidEmail(email) && isValidPass(password) && emploeeNum.isNotEmpty() && employeeName.isNotEmpty() && employeeRol != null
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPass(password: String): Boolean = password.length > 5

    suspend fun createEmployee(){
        val newEmployee = NewEmployee(
            id = null,
            name = employeeName.value,
            employeeNumber = employeeNum.value.toInt(),
            role = employeeRol.value?.name.toString(),
            password = password.value,
            photo = employeePhoto.value,
            email = email.value
        )

        _isLoading.value = true
        try {
            _creationResult.value = addUserUseCase(newEmployee)
            if (_creationResult.value.isSuccess) clearValues()
        } finally {
            _isLoading.value = false
        }
    }

    private fun clearValues(){
        _email.value = ""
        _password.value = ""
        _employeeNum.value = ""
        _employeeName.value = ""
        _employeeRol.value = Rol(1, "employee")
        _addEnable.value = false

    }

    fun resetResult(){
        _creationResult.value = Result.success(null)
    }


}