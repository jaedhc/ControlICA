package com.example.controlica.presentation.components.manage_products

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.controlica.R
import com.example.controlica.core.di.AppSessionEntryPoint
import com.example.controlica.data.model.products.ProductDTO
import com.example.controlica.data.model.products.ProductFilterRequest
import com.example.controlica.presentation.components.common.shimmerEffect
import com.example.controlica.presentation.components.common.widgets.CustomDateInput
import com.example.controlica.presentation.components.common.widgets.CustomDialog
import com.example.controlica.presentation.components.common.widgets.CustomDropdown
import com.example.controlica.presentation.components.common.widgets.SearchBar
import com.example.controlica.presentation.viewmodel.products.ManageProdcutsViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import okhttp3.internal.wait


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockScren(
    modifier: Modifier,
    navHostController: NavHostController,
    manageProdcutsViewModel: ManageProdcutsViewModel
){
    Box(modifier = modifier){
        Loader(navHostController = navHostController, manageProdcutsViewModel = manageProdcutsViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Loader(
    navHostController: NavHostController,
    manageProdcutsViewModel: ManageProdcutsViewModel
){
    val isLoading: Boolean by manageProdcutsViewModel.isLoading.collectAsState()
    val productsList by manageProdcutsViewModel.productsList.collectAsState()
    var filteredProducts by remember { mutableStateOf(listOf<ProductDTO>()) }

    var filter by remember {
        mutableStateOf(ProductFilterRequest(
            1.0,
            999.0,
            LocalDate(2026,12,31),
            null,
            null))
    }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current


    // Obtiene la instancia singleton de AppSession
    val appSession = remember {
        EntryPointAccessors.fromApplication(
            context,
            AppSessionEntryPoint::class.java
        ).appSession()
    }

    val isAdmin by appSession.isAdmin.collectAsState()

    when {
        isLoading -> LoadingScreen()

        productsList.isSuccess -> {
            val products = productsList.getOrNull().orEmpty()

            Menu(
                allProducts = products,
                navHostController = navHostController,
                onSearchChange = { filteredProducts = it },
                onFilteredChange = {
                    coroutineScope.launch {
                        manageProdcutsViewModel.getFilteredProducts(it)
                    }
                }
            )

            if(products.isNotEmpty()){
                ProductList(filteredProducts, manageProdcutsViewModel, isAdmin)
                
                if (isAdmin){
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){
                        FloatingActionButton(
                            onClick = { navHostController.navigate("add_product") },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp, bottom = 16.dp),
                            shape = CircleShape,
                            containerColor = Color(0xFF4771BF) // Color de fondo del botón
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar",
                                tint = Color.White
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.empty),
                            contentDescription = "Empty list",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "No se encontraron prductos con esta descripción",
                            color = Color(0xFF919BAD),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        productsList.isFailure -> {
            Text(
                text = productsList.toString(),
                color = Color.Black,
                fontSize = 12.sp, fontWeight = FontWeight.SemiBold
                )
        }
    }

    LaunchedEffect (Unit) {
        manageProdcutsViewModel.getFilteredProducts(filter)
    }
}

@Composable
fun ProductList(
    products: List<ProductDTO>,
    manageProdcutsViewModel: ManageProdcutsViewModel,
    isAdmin: Boolean
){
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productId by remember { mutableStateOf("") }

    var selectedProduct by remember { mutableStateOf<ProductDTO?>(null) }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top
    ) {
        items(products){ product ->
            ProductCard(
                product = product,
                onEdit = { selectedProduct = product },
                onDelete = {
                    productId = product.id.toString()
                    showDeleteDialog = true
                },
                isAdmin = isAdmin)
        }
    }

    CustomDialog(
        onDeleteClick = {
            showDeleteDialog = false },
        onCancelClick = { showDeleteDialog = false },
        onDismissRequest = { showDeleteDialog = false },
        showDialog = showDeleteDialog,
    )

    selectedProduct?.let { product ->
        ProductEditPopup(
            product = product,
            onEdit = { productId, newStock ->
                manageProdcutsViewModel.updateStock(productId, newStock)
            },
            onDismiss = { selectedProduct = null },
            isAdmin = isAdmin
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductCard(
    product: ProductDTO,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    isAdmin: Boolean
){
    val dismissState = rememberDismissState(
        confirmStateChange = {
            when(it){
                DismissValue.DismissedToEnd -> {
                    onEdit()
                    false
                }
                DismissValue.DismissedToStart -> {
                    if(isAdmin) onDelete()
                    false
                }
                else -> false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            val direction = dismissState.dismissDirection
            val color = when(direction) {
                DismissDirection.StartToEnd -> Color(0xFFBBDEFB)
                DismissDirection.EndToStart -> Color.Transparent
                null -> Color.Transparent
            }
            val icon = when (direction) {

                DismissDirection.StartToEnd -> {
                    if(isAdmin){
                        Icons.Default.Edit
                    }else {
                        Icons.Default.RemoveRedEye
                    }
                }
                DismissDirection.EndToStart ->
                    if(isAdmin){
                        Icons.Default.Delete
                    }else {
                        null
                    }
                null -> null
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = if (direction == DismissDirection.StartToEnd)
                    Alignment.CenterStart else Alignment.CenterEnd
            ) {
                icon?.let {
                    Icon(it, contentDescription = null, tint = Color.Black)
                }
            }
        },
        dismissContent = {
            Card(
                modifier =
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                //elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp))
                {
                    Box(modifier = Modifier
                        .weight(.2f)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                    ){
                        if (!product.photo.isNullOrEmpty()){
                            AsyncImage(
                                model = product.photo, // URL de la imagen
                                contentDescription = "Imagen de producto",
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                                    .align(Alignment.Center),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.placeholder_product)
                            )
                        }else{
                            Image(
                                painter = painterResource(id = R.drawable.def_profile_pic),
                                contentDescription = "default prfile pic",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .height(60.dp)
                                    .width(60.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier
                        .padding(16.dp)
                        .weight(.3f)
                    ) {
                        Text(
                            text = product.code,
                            color = Color(0xFF193B7A),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = product.name,
                            color = Color(0xFF5F6B83),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Box(modifier = Modifier
                        .weight(.25f)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                    ){
                        StockProgress(stock = product.stock, maxStock = product.totalStock)
                    }

                    Box(modifier = Modifier
                        .weight(.25f)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ){
                            Text(
                                text = "Exp",
                                color = Color(0xFF5F6B83),
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${product.daysToExpire} days",
                                color = Color(0xFF193B7A),
                                fontSize = 16.sp
                            )
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun ProductEditPopup(
    product: ProductDTO,
    onEdit: (productId: Int, newStock: Int) -> Unit,
    onDismiss: () -> Unit,
    isAdmin: Boolean
) {
    var currentStock by remember { mutableStateOf(product.stock) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .background(Color.White)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.background(Color.White)) {
                    AsyncImage(
                        model = product.photo,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = product.code,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF919BAD))
                        Text(text = product.name, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow(label = "Categoría:", value = product.categoryName)
                InfoRow(label = "Tipo:", value = product.typeName)
                InfoRow(label = "Precio:", value = "$${product.price}")
                InfoRow(label = "Stock", value = "${product.stock}")

                Spacer(modifier = Modifier.height(16.dp))

                if(isAdmin){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            if (currentStock > 0) currentStock--
                        }) {
                            Icon(Icons.Default.Remove, contentDescription = null)
                        }
                        Text("${currentStock}", fontSize = 20.sp)
                        IconButton(onClick = {
                            if (currentStock < product.totalStock) currentStock++
                        }) {
                            Icon(Icons.Default.Add, contentDescription = null)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C8CD5) // Azul personalizado
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            onEdit(product.id, currentStock)
                        }
                    ) {
                        Text("APLICAR CAMBIOS", color = Color.White)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Menu(
    allProducts: List<ProductDTO>,
    navHostController: NavHostController,
    onSearchChange: (List<ProductDTO>) -> Unit,
    onFilteredChange: (ProductFilterRequest) -> Unit
){
    var query by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    val queried = remember(query, allProducts) {
        allProducts.filter { it.name.contains(query, ignoreCase = true) }
    }
    var filtered by remember {
        mutableStateOf(ProductFilterRequest(
            1.0,
            999.0,
            LocalDate(2026,12,31),
            null,
            null))
    }

    LaunchedEffect(queried) {
        onSearchChange(queried)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(10.dp)
    ) {
        SearchBar(
            modifier = Modifier
                .weight(.80f)
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp)
                .height(75.dp),
            onSearch = { query = it }
        )
        Spacer(modifier = Modifier.weight(.05f))
        Button(
            modifier = Modifier
                .weight(.15f)
                .height(70.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
            ),
            onClick = { showFilterDialog = true }
        ) {
            Image(
                painter = rememberVectorPainter(image = Icons.Default.FilterList),
                contentDescription = "Filtros",
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer(
                        scaleX = 2f,
                        scaleY = 2f
                    ))
        }
    }

    FilterDialog(
        onAplicarFiltro = {
            filtered = it
            onFilteredChange(it) },
        onCancelClick = { showFilterDialog = false },
        onDismissRequest  = { showFilterDialog = false },
        showDialog = showFilterDialog
    )

}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold, color = Color(0xFF193B7A))
        Text(text = value, color = Color(0xFF193B7A))
    }
}


@Composable
fun StockProgress(
    stock: Int,
    maxStock: Int
) {
    // Calcular porcentaje
    val progress = stock.toFloat() / maxStock.toFloat()

    // Elegir color según porcentaje
    val progressColor = when {
        progress >= 0.8f -> Color(0xFF0D3C91) // Azul
        progress >= 0.3f -> Color(0xFFFFC107) // Amarillo
        else -> Color(0xFFFF3D00) // Rojo
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        Text(
            text = "Stock",
            color = Color(0xFF5F6B83),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Texto con stock actual / máximo
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stock.toString().padStart(2, '0'), // Formato 01, 02, 10, etc
                color = Color(0xFF0D3C91),
                fontSize = 16.sp
            )
            Text(
                text = "/$maxStock",
                color = Color(0xFFB0B0B0),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = progress,
            color = progressColor,
            trackColor = Color(0xFFE0E0E0),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(6.dp)
                .clip(RoundedCornerShape(50)) // Redondeado
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FilterDialog(
    onAplicarFiltro: (ProductFilterRequest) -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    showDialog: Boolean
){

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

    var selectedOptions by remember { mutableStateOf(listOf<Int>()) }
    var type by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var priceRange by remember { mutableStateOf(1.0..999.0) }
    var expirationDate by remember { mutableStateOf<LocalDate?>(null) }

    if(showDialog){
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .defaultMinSize(minWidth = 500.dp)
                    .widthIn(max = 600.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Expiración",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomDateInput(
                        selectedDate = expirationDate,
                        onDateSelected = { expirationDate = it },
                        placeholder = "Selecciona una fecha"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PriceRangeSelector(
                        range = priceRange,
                        onRangeChange = { priceRange = it }
                    )
                    Text(text = "Categoría",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(8.dp))
                    TagsSelector(
                        options = options,
                        selectedOptions = selectedOptions,
                        onSelectionChanged = { selectedOptions = it }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Tipos",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.align(Alignment.Start))
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomDropdown(
                        p_items = tipos.toList(),
                        selectedItem = type,
                        onItemSelected = { type = it },
                        placeholder = "Tipo",
                        itemToText = { it.second },
                        modifier = Modifier.background(Color(0xFFF9F9F9))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onAplicarFiltro(
                            ProductFilterRequest(
                                priceRange.start ?: 1.0,
                                priceRange.endInclusive ?: 1000.0,
                                expirationDate ?: LocalDate(2026,12,31),
                                selectedOptions,
                                type?.first
                            )
                        ) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C8CD5)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Aplicar Filtros",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSelector(
    minPrice: Double = 1.0,
    maxPrice: Double = 599.0,
    range: ClosedFloatingPointRange<Double>,
    onRangeChange: (ClosedFloatingPointRange<Double>) -> Unit
) {

    val thumbSize = 24.dp
    val trackHeight = 8.dp
    val thumbRadiusPx = with(LocalDensity.current) { thumbSize.toPx() } / 2

    Column {
        // Header
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Precio", fontWeight = FontWeight.Bold, color = Color(0xFF000000))
            Text(
                text = "Rango $${range.start.toInt()} - $${range.endInclusive.toInt()}",
                color = Color(0xFF193B7A)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Range Slider
        RangeSlider(
            value = range.start.toFloat()..range.endInclusive.toFloat(),
            onValueChange = {
                onRangeChange(it.start.toDouble()..it.endInclusive.toDouble())
            },
            valueRange = minPrice.toFloat()..maxPrice.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF193B7A),
                disabledThumbColor = Color(0xFF193B7A),
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                disabledActiveTrackColor = Color.Transparent,
                disabledInactiveTrackColor = Color.Transparent
            ),
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFF193B7A), shape = CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.White, shape = CircleShape)
                            .align(Alignment.Center)
                    )
                }
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFF193B7A), shape = CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.White, shape = CircleShape)
                            .align(Alignment.Center)
                    )
                }
            },
            track = { sliderState ->

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                ) {
                    val canvasWidth = size.width
                    val trackTop = size.height / 2 - trackHeight.toPx() / 2

                    // Área real del track gris (dejando margen para thumbs)
                    val trackStartX = thumbRadiusPx
                    val trackEndX = canvasWidth - thumbRadiusPx
                    val trackWidth = trackEndX - trackStartX

                    // Track gris de borde a borde
                    drawRoundRect(
                        color = Color.LightGray,
                        topLeft = Offset(trackStartX, trackTop),
                        size = Size(trackWidth, trackHeight.toPx()),
                        cornerRadius = CornerRadius(trackHeight.toPx() / 2, trackHeight.toPx() / 2)
                    )

                    // Track azul entre thumbs → con coerceIn para evitar desbordes
                    val progressStartX = (trackStartX + (trackWidth * sliderState.activeRangeStart))
                        .coerceIn(trackStartX, trackEndX)

                    val progressEndX = (trackStartX + (trackWidth * sliderState.activeRangeEnd))
                        .coerceIn(trackStartX, trackEndX)

                    drawRoundRect(
                        color = Color(0xFF193B7A),
                        topLeft = Offset(progressStartX, trackTop),
                        size = Size(progressEndX - progressStartX, trackHeight.toPx()),
                        cornerRadius = CornerRadius(trackHeight.toPx() / 2, trackHeight.toPx() / 2)
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsSelector(
    options: Map<Int, String>,
    selectedOptions: List<Int>,
    onSelectionChanged: (List<Int>) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { (id, label) ->
            val isSelected = id in selectedOptions

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) Color(0xFF4B73CC) else Color.Transparent
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFF4B73CC),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        val newSelection = if (isSelected) {
                            selectedOptions - id
                        } else {
                            selectedOptions + id
                        }
                        onSelectionChanged(newSelection)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.White else Color(0xFF4B73CC)
                )
            }
        }
    }

}


//@Composable
//fun DialogDetail

@Composable
fun LoadingScreen(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Box(
            modifier = Modifier
                .weight(.75f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.weight(.05f))

        Box(
            modifier = Modifier
                .weight(.2f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
    }

    ShimmerList()
}

@Composable
fun ShimmerList() {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
    ) {
        items(6) {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x00FFFFFF))
                            .shimmerEffect()
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) { }
                    }
                }
            }
        }
    }
}
