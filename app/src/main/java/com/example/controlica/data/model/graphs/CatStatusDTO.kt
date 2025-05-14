package com.example.controlica.data.model.graphs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatStatusDTO(
    @SerialName("category_name") val categoryName: String,
    @SerialName("total_merma") val totalMerma: Int,
    @SerialName("total_stock") val totalStock: Int,
    @SerialName("total_sold") val totalSold: Int,
)


//@SerialName("type_name") val typeName: String,
