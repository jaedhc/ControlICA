package com.example.controlica.domain.repository;

import com.example.controlica.domain.model.Employee;
import com.example.controlica.domain.model.NewEmployee
import kotlin.Unit;

// domain/repository/EmployeeRepository.kt
interface EmployeeRepository {
    suspend fun getEmployeeById(userId: String): Result<Employee>
    suspend fun getAllEmployees(): Result<List<Employee>>
    suspend fun createEmployee(employee: Employee): Result<Employee>
    suspend fun updateEmployee(employee: Employee): Result<Employee>
    suspend fun deleteEmployee(employeeId: String): Result<Unit>
    suspend fun addEmployee(employee: NewEmployee): Result<Unit>
}