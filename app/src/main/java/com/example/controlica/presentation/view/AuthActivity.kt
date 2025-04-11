package com.example.controlica.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.controlica.presentation.components.auth.AuthScreenController
import com.example.controlica.presentation.viewmodel.AuthViewModel
import com.example.controlica.ui.theme.ControlICATheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlICATheme {
                AuthScreenController(navHostController = rememberNavController(), authViewModel = authViewModel)
            }
        }
    }
}
