package com.example.controlica.presentation.components.manage_users

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.controlica.R
import com.example.controlica.domain.model.Rol
import com.example.controlica.domain.use_case.auth.AddUserUseCase
import com.example.controlica.presentation.components.common.widgets.CustomDropdown
import com.example.controlica.presentation.components.common.widgets.CustomInput
import com.example.controlica.presentation.components.common.widgets.InputType
import com.example.controlica.presentation.viewmodel.manage_users.AddUserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okio.ByteString.Companion.readByteString

@Composable
fun AddUserScreen(
    modifier: Modifier,
    addUserViewModel: AddUserViewModel,
    navHostController: NavHostController
){
    val userName by addUserViewModel.employeeName.collectAsState(initial = "")
    val userEmail by addUserViewModel.email.collectAsState(initial = "")
    val employeeNum by addUserViewModel.employeeNum.collectAsState(initial = "")
    val password by addUserViewModel.password.collectAsState(initial = "")
    val rol by addUserViewModel.employeeRol.collectAsState(initial = Rol(1, "employee"))

    val enabled by addUserViewModel.addEnable.collectAsState(initial = false)
    val isLoading by addUserViewModel.isLoading.collectAsState()
    val creationResult by addUserViewModel.creationResult.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val roles = listOf(
        Rol(1, "employee"),
        Rol(2, "admin")
    )

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri.value = uri
        val inputStream = imageUri.value?.let { context.contentResolver.openInputStream(it) }
        val byteArray = inputStream!!.readBytes()

        addUserViewModel.loadImage(byteArray)
    }

     Column (
         modifier = modifier
     ) {

         Column(modifier = Modifier
             .fillMaxWidth()
             .padding(top = 18.dp, bottom = 18.dp)
             .clickable { launcher.launch("image/*") },
             horizontalAlignment = Alignment.CenterHorizontally,
         ){
             if(imageUri.value != null){
                 AsyncImage(
                     model = imageUri.value,
                     contentDescription = "Imagen de perfil",
                     modifier = Modifier
                         .height(86.dp)
                         .width(86.dp)
                         .clip(CircleShape),
                     contentScale = ContentScale.Crop
                 )
             }else {
                 Image(
                     modifier = Modifier
                         .height(86.dp)
                         .width(86.dp),
                     painter = painterResource(id = R.drawable.def_profile_pic),
                     contentDescription = "Profile Pic")
             }
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
             onQueryChange = {
                 addUserViewModel.onFormChanged(
                     email = userEmail,
                     password = password,
                     employeeName = it,
                     emploeeNum = employeeNum,
                     employeeRol = rol
                 )
             },
             enabled = !isLoading
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
             onQueryChange = { addUserViewModel.onFormChanged(
                 email = it,
                 password = password,
                 employeeName = userName,
                 emploeeNum = employeeNum,
                 employeeRol = rol
             )},
             enabled = !isLoading
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
             onQueryChange = { addUserViewModel.onFormChanged(
                 email = userEmail,
                 password = password,
                 employeeName = userName,
                 emploeeNum = it,
                 employeeRol = rol
             ) },
             enabled = !isLoading
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
             onQueryChange = { addUserViewModel.onFormChanged(
                 email = userEmail,
                 password = it,
                 employeeName = userName,
                 emploeeNum = employeeNum,
                 employeeRol = rol
             ) },
             enabled = !isLoading
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
             onItemSelected = {addUserViewModel.onFormChanged(
                 email = userEmail,
                 password = password,
                 employeeName = userName,
                 emploeeNum = employeeNum,
                 employeeRol = it
             )},
             modifier = Modifier
                 .background(
                     Color(0xFFFFFFFF),
                     shape = RoundedCornerShape(8.dp)
                 )
                 .height(60.dp),
         )

         Spacer(modifier = Modifier.height(48.dp))
         Button(
             onClick = {
                 coroutineScope.launch {
                     addUserViewModel.createEmployee()
                 }
             },
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

         LaunchedEffect(creationResult) {
            creationResult.onSuccess { unit ->
                if(unit != null){
                    navHostController.navigate("manage_users")
                    addUserViewModel.resetResult()
                }
            }.onFailure { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
         }
     }
}