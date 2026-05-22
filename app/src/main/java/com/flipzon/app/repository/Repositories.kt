package com.flipzon.app.repository

import com.flipzon.app.database.CartDao
import com.flipzon.app.database.CartEntity
import com.flipzon.app.model.LoginRequest
import com.flipzon.app.model.LoginResponse
import com.flipzon.app.model.Product
import com.flipzon.app.network.FlipzonApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: FlipzonApi
) {
    suspend fun login(username: String, password: String): LoginResponse =
        api.login(LoginRequest(username, password))
}

class ProductRepository @Inject constructor(
    private val api: FlipzonApi
) {
    suspend fun getProducts(limit: Int, skip: Int): List<Product> =
        api.getProducts(limit, skip).products
    suspend fun searchProducts(query: String, limit: Int, skip: Int): List<Product> =
        api.searchProducts(query, limit, skip).products
    suspend fun checkout(userId: Int, products: List<com.flipzon.app.network.CheckoutProduct>) =
        api.checkout(com.flipzon.app.network.CheckoutRequest(userId, products))
}

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getCartItems(): Flow<List<CartEntity>> = cartDao.getAll()
    suspend fun addOrUpdate(product: Product) {
        val existing = cartDao.getAll().first().firstOrNull { it.productId == product.id }
        if (existing != null) {
            cartDao.update(existing.copy(quantity = existing.quantity + 1))
        } else {
            cartDao.insert(CartEntity.fromProduct(product))
        }
    }
    suspend fun updateQuantity(productId: Int, newQty: Int) {
        val item = cartDao.getAll().first().firstOrNull { it.productId == productId } ?: return
        if (newQty <= 0) cartDao.delete(item) else cartDao.update(item.copy(quantity = newQty))
    }
    suspend fun clearCart() = cartDao.clearAll()
}
