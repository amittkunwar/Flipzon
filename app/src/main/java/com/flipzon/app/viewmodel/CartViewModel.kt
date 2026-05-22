package com.flipzon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flipzon.app.database.CartEntity
import com.flipzon.app.datastore.SessionManager
import com.flipzon.app.network.CheckoutProduct
import com.flipzon.app.repository.CartRepository
import com.flipzon.app.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val cartItems: StateFlow<List<CartEntity>> = cartRepository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice: StateFlow<Double> = cartItems.map { list ->
        list.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState

    fun incrementQuantity(productId: Int) {
        viewModelScope.launch {
            val item = cartItems.value.find { it.productId == productId }
            if (item != null) {
                cartRepository.updateQuantity(productId, item.quantity + 1)
            }
        }
    }

    fun decrementQuantity(productId: Int) {
        viewModelScope.launch {
            val item = cartItems.value.find { it.productId == productId }
            if (item != null) {
                cartRepository.updateQuantity(productId, item.quantity - 1)
            }
        }
    }

    fun checkout() {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading
            try {
                val session = sessionManager.sessionFlow.first()
                if (session.userId == -1) {
                    _checkoutState.value = CheckoutState.Error("Invalid user session")
                    return@launch
                }
                
                val currentItems = cartItems.value
                if (currentItems.isEmpty()) {
                    _checkoutState.value = CheckoutState.Error("Cart is empty")
                    return@launch
                }

                val checkoutProducts = currentItems.map { CheckoutProduct(it.productId, it.quantity) }
                productRepository.checkout(session.userId, checkoutProducts)
                
                // On atomic success, clear the local cart
                cartRepository.clearCart()
                _checkoutState.value = CheckoutState.Success
            } catch (e: Exception) {
                // On failure, do not clear the room database
                _checkoutState.value = CheckoutState.Error(e.message ?: "Checkout failed")
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
    }
}

sealed class CheckoutState {
    object Idle : CheckoutState()
    object Loading : CheckoutState()
    object Success : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
