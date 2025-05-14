package com.example.controlica.domain.use_case.auth

import com.example.controlica.data.model.users.CurrentEmployeeData
import com.example.controlica.domain.repository.AuthRepository
import io.github.jan.supabase.auth.user.UserSession
import javax.inject.Inject

class CurrentSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<CurrentEmployeeData?>{
        return authRepository.getCurrentSession()
    }
}