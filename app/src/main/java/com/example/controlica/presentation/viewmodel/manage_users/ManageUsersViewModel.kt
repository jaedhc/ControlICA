package com.example.controlica.presentation.viewmodel.manage_users

import androidx.lifecycle.ViewModel
import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.use_case.auth.GetAllEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    private val getAllEmployeesUseCase: GetAllEmployeesUseCase
): ViewModel(){

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _listEmployees = MutableStateFlow(Result.success(emptyList<Employee>()))
    val listEmployees: StateFlow<Result<List<Employee>>> = _listEmployees

    suspend fun getAllEmployees(){
        _isLoading.value = true
        _listEmployees.value = getAllEmployeesUseCase()
        _isLoading.value = false
    }

}