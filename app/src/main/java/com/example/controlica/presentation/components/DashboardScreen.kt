package com.example.controlica.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.datetime.toLocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(modifier: Modifier){
    Box(modifier = modifier){
        Box (modifier = Modifier.padding(40.dp)){
            ProductDepletionPieChart()
        }
    }
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
