package com.iacc.ivan_vega_semana9

import ProductDBHelper
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.iacc.ivan_vega_semana9.dto.ProductoDto

class GraficoActivity : AppCompatActivity() {
    private lateinit var dbHelper: ProductDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafico)

        dbHelper = ProductDBHelper(this)
        Log.v("[Productos]", "Mostrando gráfico de los cinco productos con mejor valoración...")

        displayTopRatedProductsChart()
    }

    private fun displayTopRatedProductsChart() {
        val topRatedProducts = getTopRatedProducts()

        val barChart = findViewById<BarChart>(R.id.barChart)

        val entries = ArrayList<BarEntry>()
        val productNames = ArrayList<String>()

        for ((index, product) in topRatedProducts.withIndex()) {
            entries.add(BarEntry(index.toFloat(), product.rating.rate.toFloat()))
            productNames.add(product.id.toString())
        }

        val barDataSet = BarDataSet(entries, "Productos")
        barDataSet.color = Color.rgb(51, 181, 229) // Color azul más atractivo
        barDataSet.valueTextColor = Color.BLACK // Color del texto dentro de las barras

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f // Ancho de las barras
        barChart.data = barData

        val description = Description()
        description.text = "Top 5 Productos con Mejor Valoración"
        barChart.description = description

        // Personalizar el eje X con los nombres de los productos
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(productNames)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true) // Línea de eje X
        xAxis.axisLineWidth = 2f // Ancho de la línea de eje X

        // Personalizar el eje Y
        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0f // Valor mínimo del eje Y
        leftAxis.axisMaximum = 5f // Valor máximo del eje Y
        leftAxis.setDrawGridLines(false) // Ocultar líneas de cuadrícula en el eje Y

        // Animaciones
        barChart.animateY(1000)

        barChart.invalidate()
    }

    // Función para obtener los cinco productos con mejor valoración
    private fun getTopRatedProducts(): List<ProductoDto> {
        Log.v("[Productos]", "Obteniendo los cinco productos con mejor valoración...")
        val products = dbHelper.getAllProducts()
        return products.sortedByDescending { it.rating.rate }.take(5)
    }
}