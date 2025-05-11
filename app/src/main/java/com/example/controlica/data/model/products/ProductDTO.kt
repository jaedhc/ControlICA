package com.example.controlica.data.model.products

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val id: Int,
    val photo: String,
    val code: String,
    val name: String,
    val stock: Int,
    @SerialName("total_stock") val totalStock: Int,
    val expiration: LocalDate,
    val price: Double,
    @SerialName("category_name") val categoryName: String,
    @SerialName("type_name") val typeName: String,
    @SerialName("days_to_expire") val daysToExpire: Int,
)
