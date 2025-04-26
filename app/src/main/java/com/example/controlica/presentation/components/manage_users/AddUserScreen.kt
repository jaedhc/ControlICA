package com.example.controlica.presentation.components.manage_users

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.controlica.R
import com.example.controlica.domain.model.Rol
import com.example.controlica.presentation.components.common.widgets.CustomDropdown
import com.example.controlica.presentation.components.common.widgets.CustomInput
import com.example.controlica.presentation.components.common.widgets.InputType

@Composable
fun AddUserScreen(
    modifier: Modifier
){
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var employeeNum by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf<Rol?>(null) }

    var enabled by remember { mutableStateOf(true) }

    val roles = listOf(
        Rol(1, "employee"),
        Rol(2, "admin")
    )

     Column (
         modifier = modifier
     ) {

         Column(modifier = Modifier
             .fillMaxWidth()
             .padding(top = 18.dp, bottom = 18.dp),
             horizontalAlignment = Alignment.CenterHorizontally,
         ){
             Image(
                 modifier = Modifier
                     .height(86.dp)
                     .width(86.dp),
                 painter = painterResource(id = R.drawable.def_profile_pic),
                 contentDescription = "Profile Pic")
             Spacer(modifier = Modifier.height(6.dp))
             Text(text = "Agregar foto", color = Color(0xFF919191))
         }

         Spacer(modifier = Modifier.height(10.dp))
         Text(
             modifier = Modifier.padding(8.dp),
             text = "Nombre del usuario",
             fontSize = 16.sp,
             color = Color(0xFF193B7A),
             fontWeight = FontWeight.Bold,
             fontFamily = FontFamily(Font(R.font.inter))
         )
         CustomInput(
             text = userName,
             placeholder = "Ejemplo usuario...",
             modifier = Modifier
                 .background(
                     Color(0xFFFFFFFF),
                     shape = RoundedCornerShape(8.dp)
                 )
                 .padding(horizontal = 16.dp)
                 .height(60.dp),
             onQueryChange = { userName = it }
         )
         //---------------EMAIL----------------------------------------
         Spacer(modifier = Modifier.height(10.dp))
         Text(
             modifier = Modifier.padding(8.dp),
             text = "Email",
             fontSize = 16.sp,
             color = Color(0xFF193B7A),
             fontWeight = FontWeight.Bold,
             fontFamily = FontFamily(Font(R.font.inter))
         )
         CustomInput(
             text = userEmail,
             placeholder = "ejemplo@correo.com",
             modifier = Modifier
                 .background(
                     Color(0xFFFFFFFF),
                     shape = RoundedCornerShape(8.dp)
                 )
                 .padding(horizontal = 16.dp)
                 .height(60.dp),
             type = InputType.EMAIL,
             onQueryChange = { userEmail = it }
         )

         //------------------ NUM DE EMPLEADO ---------------------
         Spacer(modifier = Modifier.height(10.dp))
         Text(
             modifier = Modifier.padding(8.dp),
             text = "Número de empleado",
             fontSize = 16.sp,
             color = Color(0xFF193B7A),
             fontWeight = FontWeight.Bold,
             fontFamily = FontFamily(Font(R.font.inter))
         )
         CustomInput(
             text = employeeNum,
             placeholder = "1867312",
             modifier = Modifier
                 .background(
                     Color(0xFFFFFFFF),
                     shape = RoundedCornerShape(8.dp)
                 )
                 .padding(horizontal = 16.dp)
                 .height(60.dp),
             type = InputType.NUMBER,
             onQueryChange = { employeeNum = it }
         )
         //------------------ CONTRASEÑA ---------------------
         Spacer(modifier = Modifier.height(10.dp))
         Text(
             modifier = Modifier.padding(8.dp),
             text = "Contraseña",
             fontSize = 16.sp,
             color = Color(0xFF193B7A),
             fontWeight = FontWeight.Bold,
             fontFamily = FontFamily(Font(R.font.inter))
         )
         CustomInput(
             text = password,
             placeholder = "********",
             modifier = Modifier
                 .background(
                     Color(0xFFFFFFFF),
                     shape = RoundedCornerShape(8.dp)
                 )
                 .padding(horizontal = 16.dp)
                 .height(60.dp),
             type = InputType.PASSWORD,
             onQueryChange = { password = it }
         )

         //------------------ ROL ---------------------
         Spacer(modifier = Modifier.height(10.dp))
         Text(
             modifier = Modifier.padding(8.dp),
             text = "Tipo de usuario",
             fontSize = 16.sp,
             color = Color(0xFF193B7A),
             fontWeight = FontWeight.Bold,
             fontFamily = FontFamily(Font(R.font.inter))
         )
         CustomDropdown(
             items = roles,
             selectedItem = rol,
             onItemSelected = {rol = it},
             modifier = Modifier
                 .background(
                     Color(0xFFFFFFFF),
                     shape = RoundedCornerShape(8.dp)
                 )
                 .height(60.dp),
         )

         Spacer(modifier = Modifier.height(48.dp))
         Button(
             onClick = { /*TODO*/ },
             shape = RoundedCornerShape(8.dp),
             modifier = Modifier
                 .fillMaxWidth()
                 .height(48.dp)
                 .align(Alignment.End),
             colors = ButtonDefaults.buttonColors(
                 backgroundColor = Color(0xFF4771BF),
                 disabledBackgroundColor = Color(0xFF6A80AC),
                 contentColor = Color.White,
                 disabledContentColor = Color.White
             ),
             enabled = enabled
         ) {
             Text(
                 text = "AGREGAR USUARIO",
                 fontFamily = FontFamily(Font(R.font.inter)),
                 fontWeight = FontWeight.Bold)
         }
         Spacer(modifier = Modifier.height(24.dp))
     }
}