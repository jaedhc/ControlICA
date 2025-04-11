package com.example.controlica.presentation.components.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
// import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.controlica.R
import com.example.controlica.presentation.view.HomeActivity
import com.example.controlica.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen (
    modifier: Modifier,
    navHostController: NavHostController,
    authViewModel: AuthViewModel
){
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF224483))
    ){
        AnimatedVisibility(visible = visible, enter = fadeIn(animationSpec = tween(1000))) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFF224483)),
                contentAlignment = Alignment.Center
            ){
                Login(modifier = Modifier.padding(16.dp), navHostController = navHostController, authViewModel)
            }
        }
    }
}

@Composable
fun Login(
    modifier: Modifier,
    navHostController: NavHostController,
    authViewModel: AuthViewModel
){
    val email: String by authViewModel.email.observeAsState(initial = "")
    val password: String by authViewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by authViewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by authViewModel.isLoading.observeAsState(initial = false)
    val loginResult by authViewModel.loginResult.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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

    Column(modifier = modifier) {
        HeaderImage(
            Modifier
                .align(Alignment.CenterHorizontally)
                .width(160.dp)
                .height(160.dp)
                .fillMaxSize()
        )
        Spacer(modifier = Modifier.padding(2.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Bienvenido!",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.padding(22.dp))

        Text(text = "Número de empleado",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter)))
        Spacer(modifier = Modifier.padding(3.dp))
        EmailField(email) { authViewModel.onLoginChanged(it, password) }
        Spacer(modifier = Modifier.padding(8.dp))

        Text(text = "Contraseña",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter)))
        Spacer(modifier = Modifier.padding(3.dp))
        PasswordField(password) { authViewModel.onLoginChanged(email, it) }
        Spacer(modifier = Modifier.padding(8.dp))

        ForgotPassword(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.padding(16.dp))

        LoginButton(loginEnable) {
            coroutineScope.launch {
                authViewModel.authenticateUser()
            }
        }
    }

    LaunchedEffect(loginResult) {
        loginResult.onSuccess { user ->
            if(user != null){
                Toast.makeText(context, "Bienvenido!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
                if(context is Activity) context.finish()
            }
        }.onFailure { error ->
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            disabledBackgroundColor = Color(0xFFC9C9C9),
            contentColor = Color(0xFF224483),
            disabledContentColor = Color.White
        ), enabled = loginEnable
    ) {
        Text(text = "Iniciar sesión", fontFamily = FontFamily(Font(R.font.inter)), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Olvidaste la contraseña?",
        modifier = modifier.clickable { },
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontFamily = FontFamily(Font(R.font.inter))
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = password, onValueChange = { onTextFieldChanged(it) },
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(text = "Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible}) {
                val icon: ImageVector = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                Icon(imageVector = icon, contentDescription = "Toogle Password", tint = Color.Gray)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF636262),
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email, onValueChange = { onTextFieldChanged(it) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF636262),
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Header",
        modifier = modifier
    )
}