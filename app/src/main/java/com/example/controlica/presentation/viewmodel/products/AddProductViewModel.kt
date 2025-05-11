package com.example.controlica.presentation.viewmodel.products

import androidx.lifecycle.ViewModel
import com.example.controlica.domain.model.NewProduct
import com.example.controlica.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    val productRepository: ProductRepository
):ViewModel(){

    private val _creationResult = MutableStateFlow<Result<Unit?>>(Result.success(null))
    val creationResult: StateFlow<Result<Unit?>> = _creationResult

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _productPhoto = MutableStateFlow<ByteArray?>(null)
    val productPhoto: StateFlow<ByteArray?> = _productPhoto

    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _stock = MutableStateFlow(0)
    val stock: StateFlow<Int> = _stock

    private val _expiration = MutableStateFlow<LocalDate>(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    val expiration: StateFlow<LocalDate> = _expiration

    private val _price = MutableStateFlow(0.0)
    val price: StateFlow<Double> = _price

    private val _category = MutableStateFlow<Pair<Int, String>?>(null)
    val category: StateFlow<Pair<Int, String>?> = _category

    private val _type = MutableStateFlow<Pair<Int, String>?>(null)
    val type: StateFlow<Pair<Int, String>?> = _type

    private val _addEnable = MutableStateFlow<Boolean>(false)
    val addEnable: StateFlow<Boolean> = _addEnable

    fun onFormChanged(
        code: String,
        name: String,
        stock: Int,
        expiration: LocalDate,
        price: Double,
        category: Pair<Int, String>?,
        type: Pair<Int, String>?
    ) {
        _code.value = code
        _name.value = name
        _stock.value = stock
        _expiration.value = expiration
        _price.value = price
        _category.value = category
        _type.value = type

        _addEnable.value = code.isNotEmpty() &&
                name.isNotEmpty() &&
                stock > 0 &&
                price > 0.0 && type != null
    }

    fun loadImage(image: ByteArray){
        _productPhoto.value = image
    }

    suspend fun createProduct(){
        _isLoading.value = true
        val newProduct = NewProduct(
            photo = _productPhoto.value,
            code = _code.value,
            name = _name.value,
            stock = _stock.value,
            expiration = _expiration.value,
            price = _price.value,
            categoryId = _category.value!!.first,
            typeId = _type.value!!.first,
            totalStock = _stock.value
        )

        try{
            _creationResult.value = productRepository.addProduct(newProduct)
            if(_creationResult.value.isSuccess) clearValues()
        } finally {
            _isLoading.value = false
        }
    }

    private fun clearValues() {
        _code.value = ""
        _name.value = ""
        _stock.value = 0
        _expiration.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        _price.value = 0.0
        _category.value = null
        _type.value = null
        _productPhoto.value = null
        _addEnable.value = false
    }

    fun resetResult(){
        _creationResult.value = Result.success(null)
    }


}