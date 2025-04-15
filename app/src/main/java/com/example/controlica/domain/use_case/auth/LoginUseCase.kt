package com.example.controlica.domain.use_case.auth

import com.example.controlica.domain.repository.AuthRepository
import io.github.jan.supabase.auth.user.UserSession
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email:String, password:String): Result<UserSession?>{
        if (email.isBlank()) {
            return Result.failure(Exception("El email no puede estar vacío"))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("La contraseña no puede estar vacía"))
        }

        return authRepository.login(email, password)
    }
}