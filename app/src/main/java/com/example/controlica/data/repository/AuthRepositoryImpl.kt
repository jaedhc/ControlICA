package com.example.controlica.data.repository

import com.example.controlica.domain.repository.AuthRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWith(Email){
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception){
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.signUpWith(Email){
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception){
            false
        }
    }
}