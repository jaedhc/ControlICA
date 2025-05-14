package com.example.controlica.domain.repository

import com.example.controlica.data.model.graphs.CatStatusDTO
import com.example.controlica.data.model.graphs.CatStatusRequest

interface DashboardRepository {

    suspend fun getCategoryChart(data: CatStatusRequest): Result<List<CatStatusDTO>>

}