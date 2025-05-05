package com.example.controlica.presentation.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.controlica.presentation.components.HomeScreen
import com.example.controlica.presentation.view.ui.theme.ControlICATheme
import com.example.controlica.presentation.viewmodel.manage_users.ManageUsersViewModel
import com.example.controlica.presentation.viewmodel.UserViewModel
import com.example.controlica.presentation.viewmodel.manage_users.AddUserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val manageUsersViewModel: ManageUsersViewModel by viewModels()
    private val addUserViewModel: AddUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControlICATheme {
                HomeScreen(
                    navHostController = rememberNavController(),
                    userViewModel = userViewModel,
                    manageUsersViewModel = manageUsersViewModel,
                    addUserViewModel = addUserViewModel
                    )
            }
        }
    }
}
