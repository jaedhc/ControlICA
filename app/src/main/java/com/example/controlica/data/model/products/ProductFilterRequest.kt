package com.example.controlica.data.model.products

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductFilterRequest(
    @SerialName("p_min_price") val pMinPrice: Double?,
    @SerialName("p_max_price") val pMaxPrice: Double?,
    @SerialName("p_expiration_date") val pExpirationDate: LocalDate?,
    @SerialName("p_category_id") val pCategory: List<Int>?,
    @SerialName("p_type_id") val pType: Int?
)
