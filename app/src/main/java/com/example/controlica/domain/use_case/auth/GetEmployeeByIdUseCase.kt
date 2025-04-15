package com.example.controlica.domain.use_case.auth

import javax.inject.Inject

class GetEmployeeByIdUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(userId: String): Result<Employee> {
        return employeeRepository.getEmployeeById(userId)
    }
}