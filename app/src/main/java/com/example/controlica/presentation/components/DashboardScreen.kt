package com.example.controlica.presentation.components

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(modifier: Modifier){
    Box(modifier = modifier){
        Box (modifier = Modifier.padding(40.dp)){
            Text("Dashboard", fontSize = 40.sp)
        }
    }
}