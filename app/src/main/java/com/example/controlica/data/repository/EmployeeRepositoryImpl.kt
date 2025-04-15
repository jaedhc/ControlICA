package com.example.controlica.data.repository

import com.example.controlica.domain.model.Employee
import com.example.controlica.domain.repository.EmployeeRepository
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : EmployeeRepository {

    override suspend fun getEmployeeById(userId: String): Result<Employee> = try {
        val response = supabaseClient.postgrest["employees"]
            .select()
            .eq("id", userId)
            .single()
            .executeAndGetSingle<EmployeeDto>()

        Result.success(response.toDomainModel())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAllEmployees(): Result<List<Employee>> = try {
        val response = supabaseClient.postgrest["employees"]
            .select()
            .executeAndGetList<EmployeeDto>()

        Result.success(response.map { it.toDomainModel() })
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createEmployee(employee: Employee): Result<Employee> = try {
        val employeeDto = employee.toDataModel()
        val response = supabaseClient.postgrest["employees"]
            .insert(employeeDto)
            .executeAndGetSingle<EmployeeDto>()

        Result.success(response.toDomainModel())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateEmployee(employee: Employee): Result<Employee> = try {
        val employeeDto = employee.toDataModel()
        val response = supabaseClient.postgrest["employees"]
            .update(employeeDto)
            .eq("id", employee.id)
            .executeAndGetSingle<EmployeeDto>()

        Result.success(response.toDomainModel())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteEmployee(employeeId: String): Result<Unit> = try {
        supabaseClient.postgrest["employees"]
            .delete()
            .eq("id", employeeId)
            .execute()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}