package com.example.controlica.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.controlica.data.model.graphs.CatStatusDTO
import com.example.controlica.data.model.graphs.CatStatusRequest
import com.example.controlica.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
): ViewModel(){

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _catCharResult = MutableStateFlow(Result.success(emptyList<CatStatusDTO>()))
    val catCharResult: StateFlow<Result<List<CatStatusDTO>>> = _catCharResult

    suspend fun getCatStatus(month: Int, year: Int){
        _isLoading.value = true
        try {
            _catCharResult.value = dashboardRepository.getCategoryChart(
                CatStatusRequest(
                    inputMonth = month,
                    inputYear = year
                )
            )
        } finally {
            _isLoading.value = false
        }
    }

}