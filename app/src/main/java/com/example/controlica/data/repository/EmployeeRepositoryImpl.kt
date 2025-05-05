package com.example.controlica.data.repository

import android.util.Log
import com.example.controlica.BuildConfig
import com.example.controlica.data.model.EmployeeDto
import com.example.controlica.data.model.NewEmployeeDTO
import com.example.controlica.data.model.toDataModel
import com.example.controlica.domain.model.Employee
import com.example.controlica.data.model.RolDTO
import com.example.controlica.data.model.UserRoleDTO
import com.example.controlica.domain.model.NewEmployee
import com.example.controlica.domain.repository.EmployeeRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Named


class EmployeeRepositoryImpl @Inject constructor(
    @Named("supabase_public_client") private val supabaseClient: SupabaseClient,
    @Named("supabase_admin_client") private val supabaseAdminClient: SupabaseClient,
    private val auth: Auth
) : EmployeeRepository {

    override suspend fun getEmployeeById(userId: String): Result<Employee> {
        return try {
            val employeeDto = supabaseClient.postgrest
                .from("employee_with_role")
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
                .from("employee_with_role")
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
                        eq("id", employee.id!!)
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
            supabaseAdminClient.auth.admin.deleteUser(employeeId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error deleting employee: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun addEmployee(employee: NewEmployee): Result<Unit>{
        return try{
            supabaseAdminClient.auth.importAuthToken(BuildConfig.SUPABASE_SERVICE_ROLE_KEY)
            val signUpResult = supabaseAdminClient.auth.admin.createUserWithEmail{
                email = employee.email.toString()
                password = employee.password.toString()
                autoConfirm = true
                userMetadata = buildJsonObject {
                    put("name", employee.name)
                }
            }
            val userId = signUpResult.id

            val photoResult = employee.photo?.let {
                supabaseAdminClient.storage
                    .from("users")
                    .upload("${userId}.jpg", it){
                        upsert = false
                    }
            }

            val photoURL = photoResult?.path?.let {
                supabaseAdminClient.storage.from("users").publicUrl(it)
            }

            val roleResult = supabaseClient.postgrest.from("roles")
                .select {
                    filter {
                        eq("name", employee.role)
                    }
                }.decodeSingleOrNull<RolDTO>()

            val roleId = roleResult?.id ?: return Result.failure(Exception("Rol no encontrado"))

            supabaseClient.postgrest.from("user_roles")
                .insert(UserRoleDTO(userId = userId, roleId = roleId))

            supabaseClient.postgrest.from("employees")
                .insert(NewEmployeeDTO(
                        id = userId,
                        name = employee.name,
                        employeeNumber = employee.employeeNumber,
                        photoUrl = photoURL)
                )

            Result.success(Unit)
        } catch (e: Exception) {
            when (e){
                is AuthRestException -> {
                    val e_msg = when{
                        e.errorDescription == "email rate limit exceeded" -> "Has alcanzado el límite de correos."
                        else -> "${e.message}"
                    }
                    Result.failure(Exception(e_msg))
                }
                else -> {
                    Result.failure(Exception("Error de autenticación: ${e.message}"))
                }
            }
        }
    }
}