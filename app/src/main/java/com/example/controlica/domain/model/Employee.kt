package com.example.controlica.domain.model

// domain/model/Employee.kt
data class Employee(
    val id: String?,
    val name: String,
    val employeeNumber: Int,
    val role: String,
    val photoUrl: String?,
    val password: String?,
    val email: String?
)
