package com.example.controlica.domain.repository

import com.example.controlica.data.model.users.CurrentEmployeeData
import io.github.jan.supabase.auth.user.UserSession

interface AuthRepository {

    suspend fun login(email:String, password:String): Result<CurrentEmployeeData?>

    suspend fun getCurrentSession(): Result<CurrentEmployeeData?>

    suspend fun logOut()

}