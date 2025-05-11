package com.example.controlica.data.model.users

import com.example.controlica.domain.model.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRoleDTO(
    @SerialName("user_id") val userId: String,
    @SerialName("role_id") val roleId: Int,
){
    fun toDomain(): UserRole {
        return UserRole(
            userId = userId,
            roleId = roleId
        )
    }
}
