package com.example.controlica.presentation.components.common

import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.controlica.R

@Composable
fun TopBar(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(Color(0xFF193B7A)),
        contentAlignment = Alignment.BottomCenter
    ){
        Box(modifier = Modifier.padding(10.dp)){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "ControlICA",
                modifier = Modifier.size(40.dp))
        }
    }
}