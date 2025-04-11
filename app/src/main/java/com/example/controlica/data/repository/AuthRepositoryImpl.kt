package com.example.controlica.data.repository

import android.util.Log
import com.example.controlica.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession

import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserSession?>{
        return try{
            auth.signInWith(Email){
                this.email = email
                this.password = password
            }
            val userSession:UserSession? = auth.currentSessionOrNull()
            Result.success(userSession)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Result<UserSession?> {
        return try {
            auth.signUpWith(Email){
                this.email = email
                this.password = password
            }
            val userSession:UserSession? = auth.currentSessionOrNull()
            Result.success(userSession)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getCurrentSession(): Result<UserSession?> {
        return try {
            Result.success(auth.currentSessionOrNull())
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun logOut() {
        try{
            if(auth.currentSessionOrNull() != null){
                auth.signOut()
            }
        } catch (e: Exception){
            Log.d("Auth Error", "${e.message}")
        }
    }
}