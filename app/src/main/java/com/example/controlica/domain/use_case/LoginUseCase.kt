package com.example.controlica.domain.use_case

import com.example.controlica.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email:String, password:String): Boolean{
        return authRepository.login(email, password)
    }
}