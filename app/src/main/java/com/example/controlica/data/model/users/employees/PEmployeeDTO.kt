package com.example.controlica.data.model.users.employees

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PEmployeeDTO(
    val id: String,
    val name: String,
    @SerialName("employee_number") val employeeNumber: Int,
    @SerialName("photo_url") val photoUrl: String? = null
)
