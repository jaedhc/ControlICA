package com.example.controlica.data.model.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseEmployeeDTO(
    val id: String,
    val name: String,
    @SerialName("employee_number") val employeeNumber: Int,
    val role: String,
    @SerialName("photo_url") val photoUrl: String?
)

