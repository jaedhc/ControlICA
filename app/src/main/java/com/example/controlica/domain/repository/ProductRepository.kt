package com.example.controlica.domain.repository

import com.example.controlica.data.model.products.NewProductRequest
import com.example.controlica.data.model.products.ProductDTO
import com.example.controlica.data.model.products.ProductFilterRequest
import com.example.controlica.domain.model.NewProduct

interface ProductRepository {

    suspend fun getProductsFiltered(productFilterRequest: ProductFilterRequest):Result<List<ProductDTO>>

    suspend fun addProduct(product: NewProduct):Result<Unit>

    suspend fun deleteProduct(productId: Int):Result<Unit>

    suspend fun updateStock(productId: Int, newStock: Int): Result<Unit>

}