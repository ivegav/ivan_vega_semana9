import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.iacc.ivan_vega_semana9.dto.ProductoDto

class ProductDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertProduct(product: ProductoDto) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProductEntry.COLUMN_NAME_ID, product.id)
            put(ProductEntry.COLUMN_NAME_TITLE, product.title)
            put(ProductEntry.COLUMN_NAME_PRICE, product.price)
            put(ProductEntry.COLUMN_NAME_DESCRIPTION, product.description)
            put(ProductEntry.COLUMN_NAME_CATEGORY, product.category)
            put(ProductEntry.COLUMN_NAME_IMAGE, product.image)
            put(ProductEntry.COLUMN_NAME_RATE, product.rating.rate)
            put(ProductEntry.COLUMN_NAME_COUNT, product.rating.count)
        }
        db.insert(ProductEntry.TABLE_NAME, null, values)
    }

    fun deleteProduct(productId: Int) {
        val db = writableDatabase
        db.delete(ProductEntry.TABLE_NAME, "${ProductEntry.COLUMN_NAME_ID} = ?", arrayOf(productId.toString()))
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ProductReader.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ProductEntry.TABLE_NAME} (" +
                    "${ProductEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                    "${ProductEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${ProductEntry.COLUMN_NAME_PRICE} REAL," +
                    "${ProductEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
                    "${ProductEntry.COLUMN_NAME_CATEGORY} TEXT," +
                    "${ProductEntry.COLUMN_NAME_IMAGE} TEXT," +
                    "${ProductEntry.COLUMN_NAME_RATE} REAL," +
                    "${ProductEntry.COLUMN_NAME_COUNT} INTEGER)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ProductEntry.TABLE_NAME}"
    }

    object ProductEntry {
        const val TABLE_NAME = "product"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_PRICE = "price"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_CATEGORY = "category"
        const val COLUMN_NAME_IMAGE = "image"
        const val COLUMN_NAME_RATE = "rate"
        const val COLUMN_NAME_COUNT = "count"
    }

    fun getAllProducts(): List<ProductoDto> {
        val products: MutableList<ProductoDto> = ArrayList()
        val db = readableDatabase
        val cursor = db.query(
            ProductEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val productId = getInt(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_ID))
                val productName = getString(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_TITLE))
                val productPrice = getDouble(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_PRICE))
                val productDescription = getString(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_DESCRIPTION))
                val productCategory = getString(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_CATEGORY))
                val productImage = getString(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_IMAGE))
                val rate = getDouble(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_RATE))
                val count = getInt(getColumnIndexOrThrow(ProductEntry.COLUMN_NAME_COUNT))

                val rating = ProductoDto.Rating(rate, count)
                val product = ProductoDto(productId, productName, productPrice, productDescription, productCategory, productImage, rating)
                products.add(product)
            }
        }
        cursor.close()
        return products
    }
}