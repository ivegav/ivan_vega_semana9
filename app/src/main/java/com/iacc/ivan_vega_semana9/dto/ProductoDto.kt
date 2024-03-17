package com.iacc.ivan_vega_semana9.dto

class ProductoDto(
    var id: Int,
    var title: String,
    var price: Double,
    var description: String,
    var category: String,
    var image: String,
    var rating: Rating
) {
    class Rating(var rate: Double, var count: Int)

    override fun toString(): String {
        return "ProductoDto(id=$id, title='$title', price=$price, description='$description', category='$category', image='$image', rating=$rating)"
    }
}