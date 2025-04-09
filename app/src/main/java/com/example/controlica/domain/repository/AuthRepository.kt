package com.example.controlica.domain.repository

import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession

interface AuthRepository {

    suspend fun login(email:String, password:String): Boolean

    suspend fun signUp(email:String, password:String): Boolean

}