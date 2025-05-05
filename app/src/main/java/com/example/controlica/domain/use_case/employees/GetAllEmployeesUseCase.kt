package com.example.controlica.domain.use_case.employees

import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.repository.EmployeeRepository
import javax.inject.Inject

// domain/usecase/GetAllEmployeesUseCase.kt
class GetAllEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(): Result<List<Employee>> {
        return employeeRepository.getAllEmployees()
    }
}