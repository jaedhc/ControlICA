package com.example.controlica.data.model

import com.example.controlica.domain.model.Employee
import kotlinx.serialization.Serializable

// data/model/EmployeeDto.kt
@Serializable
data class EmployeeDto(
    val id: String,
    val name: String,
    val employee_number: String,
    val role: String,
    val photo_url: String?
) {
    fun toDomainModel(): Employee {
        return Employee(
            id = id,
            name = name,
            employeeNumber = employee_number,
            role = role,
            photoUrl = photo_url
        )
    }
}

// Extensión para convertir el modelo de dominio a DTO
fun Employee.toDataModel(): EmployeeDto {
    return EmployeeDto(
        id = id,
        name = name,
        employee_number = employeeNumber,
        role = role,
        photo_url = photoUrl
    )
}