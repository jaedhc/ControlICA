package com.example.controlica.presentation.components.common.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import kotlin.reflect.jvm.internal.impl.types.checker.TypeRefinementSupport.Enabled

@Composable
fun <T> CustomInput(
    text: T,
    placeholder: String,
    modifier: Modifier,
    type: InputType = InputType.TEXT,
    iconColor: Color = Color(0xFFBABABA),
    onQueryChange: (T) -> Unit,
    enabled: Boolean
){

    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardType = when(type.value){
        "password" -> KeyboardType.Password
        "email" -> KeyboardType.Email
        "text" -> KeyboardType.Text
        "number" -> KeyboardType.NumberPassword
        else -> {KeyboardType.Text}
    }

    val visualTransformation = when {
        type.value == "password" && !passwordVisible -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    Box(
        modifier = modifier,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (valueIsEmpty(text)) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }

                val texto = if(valueIsEmpty(text))
                    ""
                else
                    text.toString()

                BasicTextField(
                    value = texto,
                    onValueChange = { newText ->
                        val updatedValue = convertToType(newText, text)
                        updatedValue?.let { onQueryChange(it) }
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    enabled = enabled,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = keyboardType
                    ),
                    visualTransformation = visualTransformation,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (type.value == "password") {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = iconColor
                    )
                }
            }
        }
    }
}

// Función para evaluar si el valor está vacío o es equivalente a "vacío"
fun <T> valueIsEmpty(value: T): Boolean {
    return when (value) {
        is String -> value.isEmpty()
        is Double -> value.equals(0.0)
        is Int -> value == 0
        else -> false // Otras verificaciones específicas si es necesario
    }
}

fun <T> convertToType(newText: String, value: T): T? {
    return when (value) {
        is String -> newText as T
        is Double -> newText.toDoubleOrNull() as T? // Retorna null si no es un número válido
        is Int -> newText.toIntOrNull() as T? // Retorna null si no es un número válido
        else -> null
    }
}