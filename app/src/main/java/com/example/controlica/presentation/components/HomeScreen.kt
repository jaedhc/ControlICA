package com.example.controlica.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.controlica.presentation.components.common.BottomNavBarAnimated
import com.example.controlica.presentation.components.common.TopBar
import com.example.controlica.presentation.components.manage_products.AddProduct
import com.example.controlica.presentation.components.manage_products.StockScren
import com.example.controlica.presentation.components.manage_users.AddUserScreen
import com.example.controlica.presentation.components.manage_users.ManageUsersScreen
import com.example.controlica.presentation.viewmodel.DashboardViewModel
import com.example.controlica.presentation.viewmodel.manage_users.ManageUsersViewModel
import com.example.controlica.presentation.viewmodel.UserViewModel
import com.example.controlica.presentation.viewmodel.manage_users.AddUserViewModel
import com.example.controlica.presentation.viewmodel.products.AddProductViewModel
import com.example.controlica.presentation.viewmodel.products.ManageProdcutsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    startDestination: String = "dashboard",
    userViewModel: UserViewModel,
    manageUsersViewModel: ManageUsersViewModel,
    addUserViewModel: AddUserViewModel,
    manageProdcutsViewModel: ManageProdcutsViewModel,
    addProductViewModel: AddProductViewModel,
    dashboardViewModel: DashboardViewModel
){
    var selectedItem by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
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
            modifier = Modifier.background(Color(0xFFE6E6E6)),
            navController = navHostController,
            startDestination = startDestination){
            composable("dashboard"){
                DashboardScreen(
                    modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize(),
                    dashboardViewModel
                )
            }
            composable("stock") {
                StockScren(modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize(),
                    navHostController,
                    manageProdcutsViewModel = manageProdcutsViewModel
                    )
            }
            composable("manage_users") {
                ManageUsersScreen(modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize(),
                    navHostController,
                    manageUsersViewModel)
            }
            composable("user"){
                UserScreen(modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFE6E6E6))//Color(0xFFE6E6E6)
                    .fillMaxSize(), userViewModel = userViewModel)
            }
            composable("add_user") {
                AddUserScreen(modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color(0xFFE6E6E6))
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                    addUserViewModel = addUserViewModel,
                    navHostController = navHostController
                )
            }
            composable("add_product") {
                AddProduct(modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color(0xFFE6E6E6))
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                    addProductViewModel = addProductViewModel,
                    navHostController = navHostController
                )
            }
        }
    }
}