package com.example.controlica.data.repository

import android.util.Log
import com.example.controlica.data.model.EmployeeDto
import com.example.controlica.data.model.toDataModel
import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.repository.EmployeeRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : EmployeeRepository {

    override suspend fun getEmployeeById(userId: String): Result<Employee> {
        return try {
            val employeeDto = supabaseClient.postgrest
                .from("employees")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<EmployeeDto>()

            if (employeeDto != null) {
                Result.success(employeeDto.toDomainModel())
            } else {
                Result.failure(Exception("Employee not found"))
            }
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error getting employee: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getAllEmployees(): Result<List<Employee>> {
        return try {
            val employeesDto = supabaseClient.postgrest
                .from("employees")
                .select()
                .decodeList<EmployeeDto>()

            Result.success(employeesDto.map { it.toDomainModel() })
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error getting all employees: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun createEmployee(employee: Employee): Result<Employee> {
        return try {
            val employeeDto = employee.toDataModel()
            val createdEmployee = supabaseClient.postgrest
                .from("employees")
                .insert(employeeDto)
                .decodeSingle<EmployeeDto>()

            Result.success(createdEmployee.toDomainModel())
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error creating employee: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateEmployee(employee: Employee): Result<Employee> {
        return try {
            val employeeDto = employee.toDataModel()
            val updatedEmployee = supabaseClient.postgrest
                .from("employees")
                .update(employeeDto) {
                    filter {
                        eq("id", employee.id)
                    }
                }
                .decodeSingle<EmployeeDto>()

            Result.success(updatedEmployee.toDomainModel())
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error updating employee: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteEmployee(employeeId: String): Result<Unit> {
        return try {
            supabaseClient.postgrest
                .from("employees")
                .delete {
                    filter {
                        eq("id", employeeId)
                    }
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error deleting employee: ${e.message}")
            Result.failure(e)
        }
    }
}