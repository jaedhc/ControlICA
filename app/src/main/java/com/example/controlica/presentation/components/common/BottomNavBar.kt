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
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomNavBarAnimated(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    navHostController: NavHostController
){
    val items = listOf(
        Icons.Default.Home,
        Icons.Default.Groups,
        Icons.Default.Person,
        Icons.Default.Person
    )

    val names = listOf(
        "dashboard",
        "stock",
        "manage_users",
        "user"
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
                            modifier = Modifier.height(36.dp).width(36.dp),
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