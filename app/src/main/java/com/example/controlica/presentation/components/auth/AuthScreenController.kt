package com.example.controlica.presentation.components.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.controlica.presentation.viewmodel.AuthViewModel

@Composable
fun AuthScreenController(
    navHostController: NavHostController,
    startDestination: String = "splash",
    authViewModel: AuthViewModel
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination) {
            composable("home") {
                LoginScreen(modifier = Modifier.padding(innerPadding), navHostController = navHostController, authViewModel)
            }
            composable("splash"){
                SplashScreen(navHostController = navHostController, authViewModel)
            }
        }
    }
}
