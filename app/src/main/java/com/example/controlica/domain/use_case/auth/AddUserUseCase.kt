package com.example.controlica.domain.use_case.auth

import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.model.NewEmployee
import com.example.controlica.domain.repository.EmployeeRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
){

    suspend operator fun invoke(employee: NewEmployee): Result<Unit>{
        return employeeRepository.addEmployee(employee)
    }

}