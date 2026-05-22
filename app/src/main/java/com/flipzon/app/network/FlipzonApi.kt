package com.flipzon.app.network

import com.flipzon.app.model.LoginRequest
import com.flipzon.app.model.LoginResponse
import com.flipzon.app.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FlipzonApi {
    // Authentication
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // Get all products with pagination (limit, skip)
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductsResponse

    // Search products (optional)
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("skip") skip: Int = 0
    ): ProductsResponse

    // Add cart (checkout) – DummyJSON expects an array of product IDs and quantities
    @POST("carts/add")
    suspend fun checkout(@Body body: CheckoutRequest): CheckoutResponse
}

// Response wrappers
data class ProductsResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class CheckoutRequest(
    val userId: Int,
    val products: List<CheckoutProduct>
)

data class CheckoutProduct(
    val id: Int,
    val quantity: Int
)

data class CheckoutResponse(
    val id: Int,
    val total: Double,
    val discountedTotal: Double,
    val userId: Int,
    val products: List<CheckoutProduct>
)
