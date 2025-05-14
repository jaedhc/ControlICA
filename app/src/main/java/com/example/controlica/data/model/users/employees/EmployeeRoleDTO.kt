package com.example.controlica.data.model.users.employees

import com.example.controlica.data.model.users.EmployeeDto
import kotlinx.serialization.Serializable

@Serializable
data class EmployeeRoleDTO(
    val employees: PEmployeeDTO,
    val roles: RoleDTO
)

fun EmployeeRoleDTO.toEmployeeDTO() = EmployeeDto(
    id = this.employees.id,
    name = employees.name,
    employeeNumber = employees.employeeNumber,
    role = roles.name,
    photoUrl = employees.photoUrl
)
