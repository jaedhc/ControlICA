package com.example.controlica.presentation.viewmodel.products

import androidx.lifecycle.ViewModel
import com.example.controlica.data.model.products.ProductDTO
import com.example.controlica.data.model.products.ProductFilterRequest
import com.example.controlica.domain.use_case.products.GetFilteredProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate
import javax.inject.Inject

@HiltViewModel
class ManageProdcutsViewModel @Inject constructor(
    private val getFilteredProductsUseCase: GetFilteredProductsUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow<Boolean>(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _productsList = MutableStateFlow(Result.success(emptyList<ProductDTO>()))
    val productsList: StateFlow<Result<List<ProductDTO>>> = _productsList

    suspend fun getFilteredProducts(filter: ProductFilterRequest){
        _isLoading.value = true
        val cat = if(filter.pCategory.isNullOrEmpty()) null else filter.pCategory
        _productsList.value = getFilteredProductsUseCase(
            ProductFilterRequest(
                pMinPrice = filter.pMinPrice ?: 1.0,
                pMaxPrice = filter.pMaxPrice ?: 1000.0,
                pExpirationDate = filter.pExpirationDate ?: LocalDate(2026,12,31),
                pCategory = cat,
                pType = filter.pType
            )

        )
        _isLoading.value = false
    }

}