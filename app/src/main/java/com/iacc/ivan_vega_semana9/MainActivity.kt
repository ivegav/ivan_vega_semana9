package com.iacc.ivan_vega_semana9

import ProductoAdapter
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referenciar el RecyclerView en el layout
        recyclerView = findViewById(R.id.recyclerView)

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductoAdapter()
        recyclerView.adapter = adapter

        // Llamar a la función para cargar los productos desde la API al iniciar la aplicación
        fetchProducts()
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
                    }
                    adapter.setProductos(productos)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                println("Error en la solicitud: $error")
            })

        Volley.newRequestQueue(this).add(request)
    }
/**
    private fun getProductsFromLocalStorage(): List<ProductoDto> {
        // Esta función simula obtener los productos almacenados de manera persistente
        // Aquí deberías implementar la lógica para obtener los productos de la base de datos local
        // o cualquier otro medio de almacenamiento persistente
        return listOf(
            ProductoDto("Producto 1", 10.99, 4.5, "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg"),
            ProductoDto("Producto 2", 20.99, 3.8, "https://example.com/product2.jpg"),
            ProductoDto("Producto 3", 15.99, 4.2, "https://example.com/product3.jpg")
            // Agregar más productos según sea necesario
        )
    }
    */
}