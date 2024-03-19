package com.iacc.ivan_vega_semana9

import ProductDBHelper
import ProductoAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonArrayRequest
import com.iacc.ivan_vega_semana9.dto.ProductoDto
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var dbHelper: ProductDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = ProductDBHelper(this)

        recyclerView = findViewById(R.id.recyclerView)

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapter()
        recyclerView.adapter = adapter

        val btnShowChart: Button = findViewById(R.id.btnShowChart)

        btnShowChart.setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }

        Log.v("[Productos]", "Cargando productos...")
        if (!checkIfProductsExistInDB()) {
            Log.v("[Productos]", "No hay productos en la base de datos, cargando desde API...")
            fetchProducts()
        } else {
            // Si hay productos en la base de datos, cargarlos desde la base de datos
            Log.v("[Productos]", "Cargando productos desde la base de datos...")
            val productsFromDB = dbHelper.getAllProducts()
            adapter.setProductos(productsFromDB)
            Toast.makeText(this, "Productos cargados desde la base de datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfProductsExistInDB(): Boolean {
        Log.v("[Productos]", "Verificando si existen productos en la base de datos...")
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${ProductDBHelper.ProductEntry.TABLE_NAME}", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }
    private fun fetchProducts() {
        val url = "https://fakestoreapi.com/products"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val productos: MutableList<ProductoDto> = ArrayList()
                    for (i in 0 until response.length()) {
                        val product = response.getJSONObject(i)
                        val productId = product.getInt("id")
                        val productName = product.getString("title")
                        val productPrice = product.getDouble("price")
                        val productDescription = product.getString("description")
                        val productCategory = product.getString("category")
                        val productImage = product.getString("image")
                        val rating = product.getJSONObject("rating")
                        val rate = rating.getDouble("rate")
                        val count = rating.getInt("count")

                        val newProduct = ProductoDto(
                            productId,
                            productName,
                            productPrice,
                            productDescription,
                            productCategory,
                            productImage,
                            ProductoDto.Rating(rate, count)
                        )
                        productos.add(newProduct)

                        dbHelper.insertProduct(newProduct)
                    }
                    adapter.setProductos(productos)
                    Log.v("[Productos]", "Productos cargados desde API: $productos")
                    Toast.makeText(this, "Productos cargados desde la API", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                println("Error en la solicitud: $error")
            })

        Volley.newRequestQueue(this).add(request)
    }

}