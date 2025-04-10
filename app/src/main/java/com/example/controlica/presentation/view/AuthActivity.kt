package com.example.controlica.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.controlica.presentation.components.AuthScreenController
import com.example.controlica.presentation.components.SplashScreen
import com.example.controlica.presentation.viewmodel.AuthViewModel
import com.example.controlica.ui.theme.ControlICATheme
import com.example.onepieceapp.features.auth.presentation.components.LoginScreen
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
