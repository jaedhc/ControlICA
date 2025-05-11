package com.example.controlica.data.repository

import com.example.controlica.data.model.products.NewProductRequest
import com.example.controlica.data.model.products.ProductDTO
import com.example.controlica.data.model.products.ProductFilterRequest
import com.example.controlica.domain.model.NewProduct
import com.example.controlica.domain.repository.ProductRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.exp

class ProductRepositoryImpl @Inject constructor(
    @Named("supabase_public_client") private val supabaseClient: SupabaseClient,
): ProductRepository {

    override suspend fun getProductsFiltered(productFilterRequest: ProductFilterRequest): Result<List<ProductDTO>> {
        return try{
            val productsResponse = supabaseClient.postgrest
                .rpc("filter_products",productFilterRequest)
                .decodeList<ProductDTO>()

            Result.success(productsResponse)
        }catch (e: Exception){
                Result.failure(e)
        }
    }

    override suspend fun addProduct(product: NewProduct): Result<Unit> {
        return try{
            val photoResult = product.photo?.let {
                supabaseClient.storage
                    .from("products")
                    .upload("${product.code}_${product.name}.jpg", it){
                        upsert = false
                    }
            }

            val photoUrl = photoResult?.path?.let {
                supabaseClient.storage.from("products").publicUrl(it)
            }

            supabaseClient.postgrest.from("product")
                .insert(NewProductRequest(
                    photo = photoUrl,
                    code = product.code,
                    name = product.name,
                    stock = product.stock,
                    expiration = product.expiration,
                    price = product.price,
                    categoryId = product.categoryId,
                    typeId = product.typeId,
                    totalStock = product.totalStock
                ))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }

    override suspend fun deleteProduct(productId: Int): Result<Unit> {
        return try {
            supabaseClient.postgrest
                .from("product")
                .delete{
                    filter {
                        eq("id", productId)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(e.message))
        }
    }
}