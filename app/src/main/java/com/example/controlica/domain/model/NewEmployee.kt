package com.example.controlica.domain.model

data class NewEmployee(
    val id: String?,
    val name: String,
    val employeeNumber: Int,
    val role: String,
    val photo: ByteArray?,
    val password: String,
    val email: String?
)
