package com.example.controlica.domain.use_case.auth

import com.example.controlica.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        return authRepository.logOut()
    }
}