package com.example.controlica.presentation.components.manage_users

import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.controlica.R
import com.example.controlica.domain.model.Employee
import com.example.controlica.presentation.components.common.widgets.CustomDialog
import com.example.controlica.presentation.components.common.widgets.SearchBar
import com.example.controlica.presentation.viewmodel.manage_users.ManageUsersViewModel
import kotlinx.coroutines.launch

@Composable
fun ManageUsersScreen(
    modifier: Modifier,
    navHostController: NavHostController,
    manageUsersViewModel: ManageUsersViewModel
){
    Box(modifier = modifier){
        Loader(manageUsersViewModel, navHostController)
    }
}


@Composable
fun Loader(
    manageUsersViewModel: ManageUsersViewModel,
    navHostController: NavHostController
){
    val isLoading: Boolean by manageUsersViewModel.isLoading.collectAsState()
    val employees by manageUsersViewModel.listEmployees.collectAsState()
    var filteredEmployees by remember { mutableStateOf(listOf<Employee>()) }
    val context = LocalContext.current

    //Mocked(navHostController)

    when {
        isLoading -> {
            LoadingScreen()
        }

        employees.isSuccess -> {
            val list = employees.getOrNull().orEmpty()
            if (list.isNotEmpty()){
                LoadedScreen(
                    allEmployees = list,
                    navHostController = navHostController,
                    onSearchChange = { filteredEmployees = it }
                )
                EmployeeList(
                    employees = filteredEmployees,
                    manageUsersViewModel
                )
            } else {
                Toast.makeText(context, "No hay usuarios", Toast.LENGTH_LONG).show()
                Text("No hay usuarios")
            }
        }

        employees.isFailure -> {
            val errorMessage = employees.exceptionOrNull()?.localizedMessage ?: "Error desconocido"
            Text("Ocurrió un error: $errorMessage")
        }
    }

    LaunchedEffect(Unit) {
        manageUsersViewModel.getAllEmployees()
    }
}

@Composable
fun LoadedScreen(
    allEmployees: List<Employee>,
    navHostController: NavHostController,
    onSearchChange: (List<Employee>) -> Unit
){
    var query by remember { mutableStateOf("") }

    val filtered = remember(query, allEmployees){
        allEmployees.filter { it.name.contains(query, ignoreCase = true) }
    }

    LaunchedEffect (filtered) {
        onSearchChange(filtered)
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
                containerColor = Color(0xFF4771BF),
                contentColor = Color.White,
            ),
            onClick = { navHostController.navigate("add_user") }
        ) {
            Text(text = "+", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
        }
        //EmployeeList(employees = filtered)
    }
}

@Composable
fun Mocked(
    navHostController: NavHostController
){
    var filteredEmployees by remember { mutableStateOf(listOf<Employee>()) }
    val employe = listOf<Employee>(
        Employee(
            id = "1234",
            name = "Jairo Hernández",
            employeeNumber = 18472639,
            role = "employee",
            photoUrl = null,
            password = "",
            email = ""
        ),
        Employee(
            id = "1234",
            name = "Mauricio Jair",
            employeeNumber = 90218475,
            role = "admin",
            photoUrl = null,
            password = "",
            email = ""
        ),
        Employee(
            id = "1234",
            name = "Carlos Suarez",
            employeeNumber = 53782019,
            role = "admin",
            photoUrl = null,
            password = "",
            email = ""
        ),
        Employee(
            id = "1234",
            name = "Jorge Santillan",
            employeeNumber = 46193820,
            role = "employee",
            photoUrl = null,
            password = "",
            email = ""
        )
    )

    LoadedScreen(
        allEmployees = employe,
        navHostController = navHostController,
        onSearchChange = {filteredEmployees = it}
    )

    //EmployeeList(employees = filteredEmployees)
}


@Composable
fun EmployeeList(
    employees: List<Employee>,
    manageUsersViewModel: ManageUsersViewModel
){

    var showDeleteDialog by remember { mutableStateOf(false) }
    var employeeId by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 80.dp),
        verticalArrangement = Arrangement.Top
    ) {
        items(employees){ employee ->
            EmployeeCard(
                employee = employee,
                onEdit = { },
                onDelete = {
                    employeeId = employee.id.toString()
                    showDeleteDialog = true
                })
        }
    }

    CustomDialog(
        onDeleteClick = {
            manageUsersViewModel.deleteUser(employeeId)
            showDeleteDialog = false },
        onCancelClick = { showDeleteDialog = false },
        onDismissRequest = { showDeleteDialog = false },
        showDialog = showDeleteDialog,
        )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmployeeCard(
    employee: Employee,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
){
    val dismissState = rememberDismissState(
        confirmStateChange = {
            when(it){
                DismissValue.DismissedToEnd -> {
                    onEdit()
                    false
                }
                DismissValue.DismissedToStart -> {
                    onDelete()
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
                DismissDirection.StartToEnd -> Icons.Default.Edit
                DismissDirection.EndToStart -> Icons.Default.Delete
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
            val tagColor = when(employee.role){
                "admin" -> Color(0xB872A3FD)
                "employee" -> Color(0xB894EF8C)
                else -> Color.Transparent
            }

            val textColor = when(employee.role){
                "admin" -> Color(0xFF0E357D)
                "employee" -> Color(0xFF077123)
                else -> Color.Transparent
            }

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
                    .padding(10.dp)){
                    Box(modifier = Modifier
                        .weight(.2f)
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically)
                    ){
                        if (!employee.photoUrl.isNullOrEmpty()){
                            AsyncImage(
                                model = employee.photoUrl, // URL de la imagen
                                contentDescription = "Imagen de perfil",
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.Center),
                                contentScale = ContentScale.Crop
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
                        .weight(.6f)
                    ) {
                        Text(
                            text = employee.name,
                            color = Color(0xFF193B7A),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = employee.employeeNumber.toString(), color = Color(0xFF5F6B83))
                    }
                    Column(modifier = Modifier
                        .weight(.2f)
                        .align(Alignment.CenterVertically)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(tagColor),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = employee.role, fontSize = 12.sp, color = textColor)
                        }
                    }
                }

            }
        }
    )
}


// ------------------ SHIMMER EFFECT ----------------------------------

fun Modifier.shimmerEffect(): Modifier = composed{
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

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
fun ShimmerList(){
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
    ) {
        items(6){
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