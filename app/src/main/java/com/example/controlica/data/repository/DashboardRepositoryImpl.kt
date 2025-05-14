package com.example.controlica.data.repository

import com.example.controlica.data.model.graphs.CatStatusDTO
import com.example.controlica.data.model.graphs.CatStatusRequest
import com.example.controlica.domain.repository.DashboardRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import javax.inject.Inject
import javax.inject.Named

class DashboardRepositoryImpl @Inject constructor(
    @Named("supabase_public_client") private val supabaseClient: SupabaseClient
): DashboardRepository {

    override suspend fun getCategoryChart(data: CatStatusRequest): Result<List<CatStatusDTO>> {
        return try{
            val chartResponse = supabaseClient
                .postgrest
                .rpc(
                    "calculate_category_status",
                    data
                ).decodeList<CatStatusDTO>()

            Result.success(chartResponse)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}