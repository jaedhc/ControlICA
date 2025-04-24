package com.example.controlica.data.model

import com.example.controlica.domain.model.Employee
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// data/model/EmployeeDto.kt
@Serializable
data class EmployeeDto(
    val id: String,
    val name: String,
    @SerialName("employee_number") val employeeNumber: Int,
    val role: String,
    @SerialName("photo_url") val photoUrl: String?
) {
    fun toDomainModel(): Employee {
        return Employee(
            id = id,
            name = name,
            employeeNumber = employeeNumber,
            role = role,
            photoUrl = photoUrl
        )
    }
}

fun Employee.toDataModel(): EmployeeDto {
    return EmployeeDto(
        id = id,
        name = name,
        employeeNumber = employeeNumber,
        role = role,
        photoUrl = photoUrl
    )
}