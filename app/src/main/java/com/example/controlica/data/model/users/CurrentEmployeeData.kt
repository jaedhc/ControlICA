package com.example.controlica.data.model.users

import io.github.jan.supabase.auth.user.UserSession

data class CurrentEmployeeData(
    val employee: EmployeeDto,
    val userSession: UserSession?
)
