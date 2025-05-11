package com.example.controlica.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName

data class NewProduct(
    val photo: ByteArray?,
    val code: String,
    val name: String,
    val stock: Int,
    val expiration: LocalDate,
    val price: Double,
    val categoryId: Int,
    val typeId: Int,
    val totalStock: Int
)
