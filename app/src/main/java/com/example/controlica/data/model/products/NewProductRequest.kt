package com.example.controlica.data.model.products

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewProductRequest(
    val photo: String?,
    val code: String,
    val name: String,
    val stock: Int,
    val expiration: LocalDate,
    val price: Double,
    @SerialName("category_id") val categoryId: Int,
    @SerialName("type_id") val typeId: Int,
    @SerialName("total_stock") val totalStock: Int
    )
