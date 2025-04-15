package com.example.controlica.domain.use_case.auth

import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.repository.EmployeeRepository
import javax.inject.Inject

// domain/usecase/GetEmployeeByIdUseCase.kt
class GetEmployeeByIdUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(userId: String): Result<Employee> {
        return employeeRepository.getEmployeeById(userId)
    }
}