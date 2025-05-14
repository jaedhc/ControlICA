package com.example.controlica.core.di

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AppSession @Inject constructor() {
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin


    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _userPhoto = MutableStateFlow<String?>(null)
    val userPhoto: StateFlow<String?> = _userPhoto

    fun setUser(
        userName: String,
        userId: String,
        userPhoto: String?,
        isAdmin: Boolean
    ){
        _isAdmin.value = isAdmin
        _userName.value = userName
        _userId.value = userId
        _userPhoto.value = userPhoto
    }
}