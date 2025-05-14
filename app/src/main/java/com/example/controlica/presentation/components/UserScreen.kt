package com.example.controlica.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.controlica.R
import com.example.controlica.core.di.AppSessionEntryPoint
import com.example.controlica.presentation.view.AuthActivity
import com.example.controlica.presentation.viewmodel.UserViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    modifier: Modifier,
    userViewModel: UserViewModel
){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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

    Column(modifier = modifier){

        Row(
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp, start = 30.dp, end = 30.dp)
        ){
            AsyncImage(
                model = userPhoto, // URL de la imagen
                contentDescription = "Imagen del usuario",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.def_profile_pic)
            )

            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically).padding(start = 10.dp)
            )
        }

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