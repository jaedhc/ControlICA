package com.example.controlica.domain.use_case.products

import androidx.startup.StartupLogger.e
import com.example.controlica.data.model.products.ProductDTO
import com.example.controlica.data.model.products.ProductFilterRequest
import com.example.controlica.domain.repository.ProductRepository
import javax.inject.Inject

class GetFilteredProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(productFilterRequest: ProductFilterRequest):Result<List<ProductDTO>>{

        val exception = validateRequest((productFilterRequest))

        return if(exception == null){
            productRepository.getProductsFiltered(productFilterRequest)
        } else {
            Result.failure(IllegalArgumentException(exception))
        }
    }

    private fun validateRequest(request: ProductFilterRequest): String?{
        return when {
            request.pMinPrice!! < 0 -> "Precio mínimo no válido"
            request.pMaxPrice!! < request.pMinPrice -> "El precio máximo no puede ser menor que el mínimo"
            else -> { null }
        }
    }
}