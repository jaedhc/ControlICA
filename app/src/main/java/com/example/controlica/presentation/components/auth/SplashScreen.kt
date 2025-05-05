package com.example.controlica.presentation.components.auth

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.controlica.R
import com.example.controlica.presentation.view.HomeActivity
import com.example.controlica.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel
){
    var visible by remember { mutableStateOf(true) }
    val isFetching: Boolean by authViewModel.isFetching.collectAsState(initial = false)
    val currentSession by authViewModel.currentSession.collectAsState()
    val context = LocalContext.current

    val alpha by animateFloatAsState(
        targetValue = if (!isFetching) 1f else -1f,
        animationSpec = tween(durationMillis = 1000), label = "fade"
    )

    LaunchedEffect(Unit) {
        authViewModel.getCurrentSesion()
        delay(700)
    }

    LaunchedEffect(currentSession) {
        currentSession.onSuccess { user ->
            if(user != null){
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            } else {
                navHostController.navigate("home"){
                    popUpTo("splash") { inclusive = true }
                }
            }
        }.onFailure { error ->
            Toast.makeText(context, "Error ${error}", Toast.LENGTH_LONG).show()
            navHostController.navigate("home"){
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Splash(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF224483))
            .graphicsLayer { this.alpha = alpha }
    )
}


@Composable
fun Splash(modifier: Modifier) {
    Box(
        modifier = modifier // color base
    ) {
        // Capas decorativas con Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Curva superior derecha
            rotate(degrees = -20f){
                drawArc(
                    color = Color(0xFF193B7A), // azul más oscuro
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(width * 0.6f, (height * 0.3f * -2)),
                    size = Size(width * 1.2f, height * 1f)
                )
            }

            // Curva inferior izquierda
            drawArc(
                color = Color(0xFF193B7A),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(-width * 0.8f, height * 0.6f),
                size = Size(width * 1.5f, height * 1.5f)
            )
        }

        // Logo centrado
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )
        }
    }
}
