package com.example.controlica.data.model.graphs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatStatusRequest(
    @SerialName("input_month") val inputMonth: Int,
    @SerialName("input_year") val inputYear: Int
)
