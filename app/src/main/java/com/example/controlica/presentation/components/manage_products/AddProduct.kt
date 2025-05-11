package com.example.controlica.presentation.components.manage_products

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.controlica.presentation.components.common.widgets.CustomDateInput
import com.example.controlica.presentation.components.common.widgets.CustomDropdown
import com.example.controlica.presentation.components.common.widgets.CustomInput
import com.example.controlica.presentation.components.common.widgets.InputType
import com.example.controlica.presentation.viewmodel.products.AddProductViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddProduct(
    modifier: Modifier,
    addProductViewModel: AddProductViewModel,
    navHostController: NavHostController
){
    val code by addProductViewModel.code.collectAsState(initial = "")
    val name by addProductViewModel.name.collectAsState(initial = "")
    val stock by addProductViewModel.stock.collectAsState(initial = 0)
    val expiration by addProductViewModel.expiration.collectAsState(
        initial = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    )
    val price by addProductViewModel.price.collectAsState(initial = 0.0)
    val category by addProductViewModel.category.collectAsState(initial = null)
    val type by addProductViewModel.type.collectAsState(initial = null)
    val productPhoto by addProductViewModel.productPhoto.collectAsState(initial = null)

    val enabled by addProductViewModel.addEnable.collectAsState(initial = false)
    val isLoading by addProductViewModel.isLoading.collectAsState()
    val creationResult by addProductViewModel.creationResult.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri.value = uri
        val inputStream = imageUri.value?.let { context.contentResolver.openInputStream(it) }
        val byteArray = inputStream!!.readBytes()

        addProductViewModel.loadImage(byteArray)
    }

    LaunchedEffect(creationResult) {
        creationResult.onSuccess { unit ->
            if(unit != null){
                navHostController.navigate("stock")
                addProductViewModel.resetResult()
            }
        }.onFailure { error ->
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val options = mapOf(
        1 to "Food",
        2 to "Lobby",
        3 to "Para llevar",
        4 to "Barra",
        5 to "Tapas y vasos"
    )

    val tipos = mapOf(
        1 to "Alimentos",
        2 to "Tazas",
        3 to "Tés",
        4 to "Café 1/2 Libra",
        5 to "Tumblers",
        6 to "Dulces",
        7 to "Bebidas RTD",
        8 to "Tapas",
        9 to "Vasos",
        10 to "Cafe 5 libras",
        11 to "Bolsas y filtros",
        12 to "Condimentos alimentos y bebidas",
        13 to "Jarabes y bebidas"
    )

    Column(
        modifier = modifier
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 18.dp)
            .clickable { launcher.launch("image/*") },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
            text = "Nombre del producto",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        CustomInput(
            text = name,
            placeholder = "Galletas...",
            modifier = Modifier
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp)
                .height(60.dp),
            onQueryChange = {
                addProductViewModel.onFormChanged(
                    code = code,
                    name = it,
                    stock = stock,
                    expiration = expiration,
                    price = price,
                    category = category,
                    type = type
                )
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Codigo",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        CustomInput(
            text = code,
            placeholder = "E5...",
            modifier = Modifier
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp)
                .height(60.dp),
            onQueryChange = {
                addProductViewModel.onFormChanged(
                    code = it,
                    name = name,
                    stock = stock,
                    expiration = expiration,
                    price = price,
                    category = category,
                    type = type
                )
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Stock",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        CustomInput(
            text = stock,
            placeholder = "100...",
            modifier = Modifier
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp)
                .height(60.dp),
            onQueryChange = {
                addProductViewModel.onFormChanged(
                    code = code,
                    name = name,
                    stock = it,
                    expiration = expiration,
                    price = price,
                    category = category,
                    type = type
                )
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Precio",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        CustomInput(
            text = price,
            placeholder = "$ MXN",
            type = InputType.NUMBER,
            modifier = Modifier
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp)
                .height(60.dp),
            onQueryChange = {
                addProductViewModel.onFormChanged(
                    code = code,
                    name = name,
                    stock = stock,
                    expiration = expiration,
                    price = it,
                    category = category,
                    type = type
                )
            },
            enabled = !isLoading
        )
        // ---------- DROPDOWN CATEGORIA ----------
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Categoría",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        CustomDropdown(
            p_items = options.toList(),
            selectedItem = category,
            onItemSelected = {
                addProductViewModel.onFormChanged(
                    code = code,
                    name = name,
                    stock = stock,
                    expiration = expiration,
                    price = price,
                    category = it,
                    type = type
                )
            },
            placeholder = "Categoría",
            itemToText = { it.second },
            modifier = Modifier.background(Color(0xFFF9F9F9))
        )
        // ---------- DROPDOWN TIPO ----------
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Tipo de producto",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        CustomDropdown(
            p_items = tipos.toList(),
            selectedItem = type,
            onItemSelected = {
                addProductViewModel.onFormChanged(
                    code = code,
                    name = name,
                    stock = stock,
                    expiration = expiration,
                    price = price,
                    category = category,
                    type = it
                )
            },
            placeholder = "Tipo",
            itemToText = { it.second },
            modifier = Modifier.background(Color(0xFFF9F9F9))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Expiracion",
            fontSize = 16.sp,
            color = Color(0xFF193B7A),
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.inter))
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomDateInput(
            selectedDate = expiration,
            onDateSelected = {
                addProductViewModel.onFormChanged(
                    code = code,
                    name = name,
                    stock = stock,
                    expiration = it,
                    price = price,
                    category = category,
                    type = type
                )
            },
            placeholder = "Selecciona una fecha"
        )

        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    addProductViewModel.createProduct()
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
            enabled = (enabled && !isLoading)
        ) {
            Text(
                text = "AGREGAR PRODUCTO",
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

}