package com.example.controlica.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.controlica.presentation.components.common.BottomNavBarAnimated
import com.example.controlica.presentation.components.common.TopBar
import com.example.controlica.presentation.viewmodel.UserViewModel

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    startDestination: String = "dashboard",
    userViewModel: UserViewModel
){
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBarAnimated(
                selectedItem = selectedItem,
                onItemSelected = {selectedItem = it},
                navHostController = navHostController
            )
        },
        topBar = { TopBar() }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = startDestination){
            composable("dashboard"){
                DashboardScreen(modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize()
                )
            }
            composable("stock") {
                StockScren(modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize())
            }
            composable("user"){
                UserScreen(modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize(), userViewModel = userViewModel)
            }
        }
    }
}