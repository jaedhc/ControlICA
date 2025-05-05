package com.example.controlica.data.model

import com.example.controlica.domain.model.Rol
import kotlinx.serialization.Serializable

@Serializable
data class RolDTO(
    val id: Int,
    val name: String
){
    fun toDomain(): Rol {
        return Rol(
            id = id,
            name = name
        )
    }
}