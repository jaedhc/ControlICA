package com.example.controlica.presentation.components

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.controlica.data.model.graphs.CatStatusDTO
import com.example.controlica.presentation.viewmodel.DashboardViewModel
import com.example.controlica.ui.theme.PrimaryBlue
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import io.github.dautovicharis.charts.StackedBarChart
import io.github.dautovicharis.charts.model.toMultiChartDataSet
import io.github.dautovicharis.charts.style.ChartViewDefaults
import io.github.dautovicharis.charts.style.StackedBarChartDefaults
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun DashboardScreen(
    modifier: Modifier,
    dashboardViewModel: DashboardViewModel
){
    val context = LocalContext.current
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    var selectedMonth by remember { mutableStateOf(now.monthNumber) }
    var selectedYear by remember { mutableStateOf(now.year) }

    Box(modifier = modifier){
        Column(modifier = Modifier.padding(0.dp)){
            //AddCustomStackedBarChart()
            MonthSelector { mes, anio ->
                selectedMonth = mes
                selectedYear = anio
            }
            CategoryStatusChartLoader(dashboardViewModel, selectedMonth, selectedYear)

        }
    }
}

@Composable
fun MonthSelector(
    onMonthChange: (month: Int, year: Int) -> Unit
) {
    var currentDate by remember {
        mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }

    val MonthNamesSpanish = listOf(
        "Ene", "Feb", "Mar", "Abr", "May", "Jun",
        "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
    )

    val formatter = remember { MonthNamesSpanish }

    val previous = currentDate.minus(1, DateTimeUnit.MONTH)
    val next = currentDate.plus(1, DateTimeUnit.MONTH)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                currentDate = previous
                onMonthChange(currentDate.monthNumber, currentDate.year)
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Mes anterior",
                tint = Color(0xFF10367C),
                modifier = Modifier.border(1.dp, Color(0xFF10367C), RoundedCornerShape(6.dp))
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${formatter[previous.monthNumber - 1]} ${previous.year}",
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "${formatter[currentDate.monthNumber - 1]} ${currentDate.year}",
                color = Color(0xFF10367C),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            Text(
                text = "${formatter[next.monthNumber - 1]} ${next.year}",
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        IconButton(
            onClick = {
                currentDate = next
                onMonthChange(currentDate.monthNumber, currentDate.year)
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Mes siguiente",
                tint = Color(0xFF10367C),
                modifier = Modifier.border(1.dp, Color(0xFF10367C), RoundedCornerShape(6.dp))
            )
        }
    }
}


@Composable
private fun AddCustomStackedBarChart(catStatusList: List<CatStatusDTO>) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val maxWidthDp = this.maxWidth

        val colors = listOf(Color(0xFFE0668F), Color(0xFF6F5E9A), PrimaryBlue)
        val style =  StackedBarChartDefaults.style(
            barColors = colors,
            chartViewStyle =
            ChartViewDefaults.style(
                backgroundColor = Color(0xFFFFFFFF),
                shadow = 5.dp,
                width = maxWidthDp
            )
        )
        val transformedItems = catStatusList.map { dto ->
            dto.categoryName to listOf(
                dto.totalMerma.toFloat(),
                dto.totalStock.toFloat(),
                dto.totalSold.toFloat()
            )
        }

        val dataSet = transformedItems.toMultiChartDataSet(
            title = "Stock por mes",
            prefix = "",
            categories = listOf("Merma", "Stock", "Vendido")
        )

        StackedBarChart(dataSet = dataSet, style = style)

    }
}


@Composable
fun CategoryStatusChartLoader(
    dashboardViewModel: DashboardViewModel,
    month: Int,
    year: Int
){

    val isLoading: Boolean by dashboardViewModel.isLoading.collectAsState()
    val catChar by dashboardViewModel.catCharResult.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(month, year) {
        dashboardViewModel.getCatStatus(month, year)
    }

    when {
        isLoading -> true

        catChar.isSuccess -> {
            val list = catChar.getOrNull().orEmpty()

            if(list.isNotEmpty()){
                AddCustomStackedBarChart(catStatusList = list)
            }
        }

        catChar.isFailure -> {
            Text(text = "${catChar.exceptionOrNull()}")
        }
    }
}

@Composable
fun CategoryBarChart(catStatusList: List<CatStatusDTO>) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                // Configuración básica del gráfico
                description.isEnabled = false
                legend.isEnabled = true
                xAxis.setDrawGridLines(false)

                // Configurar los ejes X y Y
                xAxis.setDrawGridLines(false) // Deshabilitar las líneas de la cuadrícula en el eje X
                xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM // Posicionar las etiquetas en la parte inferior
                axisLeft.setDrawGridLines(false) // Habilitar las líneas de la cuadrícula en el eje Y
                axisRight.isEnabled = false // Deshabilitar el eje Y derecho

                // Crear las entradas de las barras apiladas
                val entries = mutableListOf<BarEntry>()

                // Para cada categoría, agregamos sus datos al gráfico
                catStatusList.forEachIndexed { index, catStatus ->
                    entries.add(BarEntry(index.toFloat(), floatArrayOf(
                        catStatus.totalMerma.toFloat(), // Merma
                        catStatus.totalSold.toFloat(),  // Vendido
                        catStatus.totalStock.toFloat()  // Stock
                    )))
                }

                // Crear el dataset para las barras apiladas
                val dataSet = BarDataSet(entries, "")

                // Colores personalizados
                dataSet.colors = listOf(
                    Color(0xFF193B7A).toArgb(), // Rojo (para merma)
                    Color(0xFFBABABA).toArgb(), // Azul (para vendido)
                    Color(0xFF14284D).toArgb()  // Verde (para stock)
                )

                dataSet.stackLabels = arrayOf("Merma", "Vendido", "Stock")

                // Crear los datos y asignarlos al gráfico
                val data = BarData(dataSet)
                this.data = data
                invalidate() // Actualizar el gráfico

                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return catStatusList.getOrNull(value.toInt())?.categoryName ?: ""
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Ajusta el tamaño del gráfico
    )
}

@Composable
fun ProductDepletionPieChart() {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                // Configuración básica del gráfico
                description.isEnabled = false
                legend.isEnabled = true
                setUsePercentValues(true)
                holeRadius = 40f // Tamaño del agujero en el centro
                transparentCircleRadius = 61f // Radio del círculo transparente

                // Crear las entradas del gráfico
                val entries = mutableListOf<PieEntry>()

                // Obtener los productos
                val products = getProductsFromDatabase()

                // Contar productos caducados y no caducados
                var expiredCount = 0f
                var nonExpiredCount = 0f
                for (product in products) {
                    if (product.daysToExpire <= 0) {
                        expiredCount++
                    } else {
                        nonExpiredCount++
                    }
                }

                // Añadir las entradas al gráfico
                entries.add(PieEntry(expiredCount, "Caducados"))
                entries.add(PieEntry(nonExpiredCount, "No Caducados"))

                // Crear el dataset para el gráfico
                val dataSet = PieDataSet(entries, "Merma de Productos")

                // Colores personalizados
                val customColors = listOf(
                    Color(0xFF4771BF).toArgb(), // Rojo (para caducados)
                    Color(0xFFA7A7A7).toArgb()  // Verde (para no caducados)
                )

                // Establecer colores personalizados
                dataSet.colors = customColors

                dataSet.sliceSpace = 3f // Espacio entre las porciones

                // Crear los datos y asignarlos al gráfico
                val data = PieData(dataSet)
                this.data = data
                invalidate() // Actualizar el gráfico
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Ajusta el tamaño del gráfico
    )
}

// Función para obtener los productos desde la base de datos o servicio
fun getProductsFromDatabase(): List<Product> {
    return listOf(
        Product(name = "Producto A", daysToExpire = -1, expiration = "2025-06-01", stock = 10),
        Product(name = "Producto B", daysToExpire = 5, expiration = "2025-05-01", stock = 5)
    )
}

data class Product(
    val name: String,
    val daysToExpire: Int,  // Días para la caducidad
    val expiration: String, // Fecha de caducidad
    val stock: Int
)
