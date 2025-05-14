package com.example.controlica.data.repository

import android.util.Log
import com.example.controlica.data.model.users.CurrentEmployeeData
import com.example.controlica.data.model.users.EmployeeDto
import com.example.controlica.data.model.users.employees.EmployeeRoleDTO
import com.example.controlica.data.model.users.employees.toEmployeeDTO
import com.example.controlica.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<CurrentEmployeeData?> {
        return try{
            supabaseClient.auth.signInWith(Email){
                this.email = email
                this.password = password
            }

            val userSession:UserSession? = supabaseClient.auth.currentSessionOrNull()

            val id = userSession?.user?.id

            if(id != null){
                val employee = supabaseClient.postgrest
                    .from("user_roles")
                    .select(
                        Columns.raw(
                            """
                            employees(id, name, employee_number, photo_url),
                            roles(name)
                            """
                        )
                    ){
                        filter {
                            eq("user_id", id.toString())
                        }
                    }.decodeSingle<EmployeeRoleDTO>()
                Result.success(CurrentEmployeeData(employee.toEmployeeDTO(), userSession))
            } else {
                Result.success(null)
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getCurrentSession(): Result<CurrentEmployeeData?> {
        return try {
            val userSession:UserSession? = supabaseClient.auth.currentSessionOrNull()
            val id = userSession?.user?.id
            if(id != null){
                val employee = supabaseClient.postgrest
                    .from("user_roles")
                    .select(
                        Columns.raw(
                            """
                            employees(id, name, employee_number, photo_url),
                            roles(name)
                            """
                        )
                    ){
                        filter {
                            eq("user_id", id.toString())
                        }
                    }.decodeSingle<EmployeeRoleDTO>()
                Result.success(CurrentEmployeeData(employee.toEmployeeDTO(), userSession))
            } else {
                Result.success(null)
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun logOut() {
        try{
            if(supabaseClient.auth.currentSessionOrNull() != null){
                supabaseClient.auth.signOut()
            }
        } catch (e: Exception){
            Log.d("Auth Error", "${e.message}")
        }
    }
}