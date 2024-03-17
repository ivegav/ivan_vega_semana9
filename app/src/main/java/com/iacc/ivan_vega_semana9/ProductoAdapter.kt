
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iacc.ivan_vega_semana9.R
import com.iacc.ivan_vega_semana9.dto.ProductoDto
import com.squareup.picasso.Picasso

class ProductoAdapter : RecyclerView.Adapter<ProductoAdapter.ProductViewHolder>() {

    private var productList: List<ProductoDto> = ArrayList()

    fun setProductos(products: List<ProductoDto>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.producto_adapter, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.productoNombre)
        private val productPrice: TextView = itemView.findViewById(R.id.productoPrecio)
        private val productRating: TextView = itemView.findViewById(R.id.productoCalificacion)
        private val productImage: ImageView = itemView.findViewById(R.id.productoImagen)

        fun bind(product: ProductoDto) {
            productName.text = product.title
            productPrice.text = "$${product.price}"
            productRating.text = "Rating: ${product.rating.rate}"

            // Cargar la imagen del producto utilizando Picasso u otra biblioteca de manejo de imágenes
            Picasso.get().load(product.image).into(productImage)
        }
    }
}