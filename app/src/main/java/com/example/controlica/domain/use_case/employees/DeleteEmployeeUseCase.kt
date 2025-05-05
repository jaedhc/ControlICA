package com.example.controlica.domain.use_case.employees

import com.example.controlica.domain.repository.EmployeeRepository
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {

    suspend operator fun invoke(employeeId: String): Result<Unit>{
        return employeeRepository.deleteEmployee(employeeId)
    }

}