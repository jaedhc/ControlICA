package com.example.controlica.domain.repository

import io.github.jan.supabase.auth.user.UserSession

interface AuthRepository {

    suspend fun login(email:String, password:String): Result<UserSession?>

    suspend fun signUp(email:String, password:String): Result<UserSession?>

    suspend fun getCurrentSession(): Result<UserSession?>

    suspend fun logOut()

}