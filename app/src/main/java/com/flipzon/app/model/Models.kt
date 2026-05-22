package com.flipzon.app.model

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val id: Int, val email: String, val firstName: String, val lastName: String, val image: String)

data class Product(val id: Int, val title: String, val price: Double, val thumbnail: String)

data class CartItem(val productId: Int, val quantity: Int)
