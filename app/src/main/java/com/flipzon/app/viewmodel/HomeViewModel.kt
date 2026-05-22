package com.flipzon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flipzon.app.database.CartEntity
import com.flipzon.app.model.Product
import com.flipzon.app.repository.CartRepository
import com.flipzon.app.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var hasMore = true
    private val limit = 20

    // Expose cart items to know which items are in the cart
    val cartItems: StateFlow<List<CartEntity>> = cartRepository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadMore()
        observeSearch()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    // Reset and fetch
                    _products.value = emptyList()
                    hasMore = true
                    loadMore()
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadMore() {
        if (_isLoading.value || !hasMore) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val currentList = _products.value
                val skip = currentList.size
                val query = _searchQuery.value

                val newProducts = if (query.isNotBlank()) {
                    productRepository.searchProducts(query, limit, skip)
                } else {
                    productRepository.getProducts(limit, skip)
                }

                if (newProducts.isEmpty() || newProducts.size < limit) {
                    hasMore = false
                }
                _products.value = currentList + newProducts
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load products"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retry() {
        loadMore()
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addOrUpdate(product)
        }
    }

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
}
