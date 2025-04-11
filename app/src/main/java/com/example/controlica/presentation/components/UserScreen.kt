package com.example.controlica.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controlica.R
import com.example.controlica.presentation.view.AuthActivity
import com.example.controlica.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    modifier: Modifier,
    userViewModel: UserViewModel
){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(modifier = modifier){
        Box (modifier = Modifier.padding(40.dp), contentAlignment = Alignment.Center){
            LogOutButton(enabled = true) {
                coroutineScope.launch {
                    userViewModel.logOut()
                    val intent = Intent(context, AuthActivity::class.java)
                    context.startActivity(intent)
                    if(context is Activity) context.finish()
                }
            }
        }
    }
}


@Composable
fun LogOutButton(enabled:Boolean, onLogOutSelected: () -> Unit){
    Button(
        onClick = { onLogOutSelected() },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFBF4747),
            disabledBackgroundColor = Color(0xFFC9C9C9),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = enabled
    ) {
        Text(
            text = "CERRAR SESIÓN",
            fontFamily = FontFamily(Font(R.font.inter)),
            fontWeight = FontWeight.Bold)
    }
}