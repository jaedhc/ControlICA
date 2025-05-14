package com.example.controlica.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.controlica.core.di.AppSessionEntryPoint
import dagger.hilt.android.EntryPointAccessors

@Composable
fun BottomNavBarAnimated(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    navHostController: NavHostController
){

    val context = LocalContext.current.applicationContext

    // Obtiene la instancia singleton de AppSession
    val appSession = remember {
        EntryPointAccessors.fromApplication(
            context,
            AppSessionEntryPoint::class.java
        ).appSession()
    }

    val userName by appSession.userName.collectAsState()
    val userPhoto by appSession.userPhoto.collectAsState()
    val isAdmin by appSession.isAdmin.collectAsState()

    val items = listOfNotNull(
        Icons.Default.Home,
        Icons.Default.Inventory,
        if (isAdmin) Icons.Default.Groups else null,
        Icons.Default.Person
    )

    val names = listOfNotNull(
        "dashboard",
        "stock",
        if (isAdmin) "manage_users" else null,
        "user"
    )

    val sizes = listOfNotNull(
        36.dp,
        30.dp,
        if(isAdmin) 36.dp else null,
        36.dp
    )

    Box(
        Modifier.background(Color(0xFFE6E6E6))
    ){
        BottomNavigation(
            backgroundColor = Color.White,
            elevation = 8.dp,
            modifier = Modifier.height(64.dp)
        ) {
            items.forEachIndexed { index, icon ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            modifier = Modifier.height(sizes[index]).width(sizes[index]),
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selectedItem == index) Color(0xFF002E6D) else Color.Gray
                        )
                    },
                    selected = selectedItem == index,
                    onClick = {
                        onItemSelected(index)
                        navHostController.navigate(names[index])
                    },
                    selectedContentColor = Color(0xFF002E6D),
                    unselectedContentColor = Color.Gray
                )
            }
        }
    }

}