package com.flipzon.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flipzon.app.model.Product

@Entity(tableName = "cartentity")
data class CartEntity(
    @PrimaryKey val productId: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val quantity: Int = 1
) {
    companion object {
        fun fromProduct(product: Product, quantity: Int = 1) = CartEntity(
            productId = product.id,
            title = product.title,
            price = product.price,
            thumbnail = product.thumbnail,
            quantity = quantity
        )
    }
}
