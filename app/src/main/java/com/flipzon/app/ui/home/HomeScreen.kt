package com.flipzon.app.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.flipzon.app.model.Product
import com.flipzon.app.ui.theme.*
import com.flipzon.app.viewmodel.HomeViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.flipzon.app.database.CartEntity

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val products      by viewModel.products.collectAsState()
    val searchQuery   by viewModel.searchQuery.collectAsState()
    val isLoading     by viewModel.isLoading.collectAsState()
    val error         by viewModel.error.collectAsState()
    val cartItems     by viewModel.cartItems.collectAsState()

    HomeScreenContent(
        products = products,
        searchQuery = searchQuery,
        isLoading = isLoading,
        error = error,
        cartItems = cartItems,
        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
        onRetry = { viewModel.retry() },
        onLoadMore = { viewModel.loadMore() },
        onAddToCart = { viewModel.addToCart(it) },
        onIncrement = { viewModel.incrementQuantity(it) },
        onDecrement = { viewModel.decrementQuantity(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    products: List<Product>,
    searchQuery: String,
    isLoading: Boolean,
    error: String?,
    cartItems: List<CartEntity>,
    onSearchQueryChange: (String) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit
) {
    val listState     = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FlipzonGradients.backgroundVertical)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Search bar ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier      = Modifier.fillMaxWidth(),
                    placeholder   = {
                        Text(
                            "Search products…",
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize   = 14.sp,
                                color      = FlipzonColors.TextHint
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint   = FlipzonColors.GoldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    shape      = RoundedCornerShape(16.dp),
                    colors     = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor     = FlipzonColors.GoldPrimary,
                        unfocusedBorderColor   = FlipzonColors.SurfaceBorder,
                        focusedContainerColor  = FlipzonColors.SurfaceGlass,
                        unfocusedContainerColor= FlipzonColors.SurfaceGlass,
                        cursorColor            = FlipzonColors.GoldPrimary,
                        focusedTextColor       = FlipzonColors.TextPrimary,
                        unfocusedTextColor     = FlipzonColors.TextPrimary
                    ),
                    textStyle = TextStyle(fontFamily = DmSans, fontSize = 14.sp)
                )
            }

            // ── Section header ──────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(16.dp)
                        .background(
                            FlipzonGradients.goldHorizontal,
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text  = "ALL PRODUCTS",
                    style = FlipzonType.labelCaps
                )
            }

            Spacer(Modifier.height(8.dp))

            // ── Content ─────────────────────────────────────────────────────
            when {
                error != null && products.isEmpty() -> {
                    ErrorState(error = error, onRetry = onRetry)
                }
                products.isEmpty() && !isLoading -> {
                    EmptyState(message = "No products found")
                }
                else -> {
                    LazyColumn(
                        state           = listState,
                        modifier        = Modifier.fillMaxSize(),
                        contentPadding  = PaddingValues(
                            start  = 16.dp, end = 16.dp,
                            top    = 4.dp,  bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products.size) { index ->
                            val product = products[index]
                            if (index >= products.size - 1 && !isLoading) {
                                LaunchedEffect(Unit) { onLoadMore() }
                            }
                            val cartItem = cartItems.find { it.productId == product.id }

                            AnimatedVisibility(
                                visible      = true,
                                enter        = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
                            ) {
                                ProductCard(
                                    product      = product,
                                    cartQuantity = cartItem?.quantity ?: 0,
                                    onAddToCart  = { onAddToCart(product) },
                                    onIncrement  = { onIncrement(product.id) },
                                    onDecrement  = { onDecrement(product.id) }
                                )
                            }
                        }

                        if (isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color       = FlipzonColors.GoldPrimary,
                                        strokeWidth = 2.dp,
                                        modifier    = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    cartQuantity: Int,
    onAddToCart: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(FlipzonColors.NavyCard)
            .drawBehind {
                drawRoundRect(
                    brush        = FlipzonGradients.goldHorizontal,
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style        = Stroke(width = 0.8.dp.toPx()),
                    alpha        = 0.35f
                )
            }
    ) {
        Row(
            modifier           = Modifier.padding(14.dp),
            verticalAlignment  = Alignment.CenterVertically
        ) {
            // Product image
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(FlipzonColors.SurfaceGlass)
                    .border(
                        width = 0.6.dp,
                        color = FlipzonColors.SurfaceBorderDim,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model              = product.thumbnail,
                    contentDescription = product.title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = product.title,
                    style    = FlipzonType.bodyRegular.copy(fontSize = 15.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text  = "$${product.price}",
                    style = FlipzonType.priceMedium
                )

                Spacer(Modifier.height(10.dp))

                if (cartQuantity > 0) {
                    // Quantity stepper
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(FlipzonColors.GoldFaint)
                            .border(
                                width = 0.6.dp,
                                color = FlipzonColors.SurfaceBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        IconButton(
                            onClick  = onDecrement,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                Icons.Filled.Remove,
                                contentDescription = "Decrease",
                                tint     = FlipzonColors.GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text  = cartQuantity.toString(),
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize   = 14.sp,
                                color      = FlipzonColors.TextPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick  = onIncrement,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Increase",
                                tint     = FlipzonColors.GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    // Add to cart button
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(FlipzonGradients.goldHorizontal)
                    ) {
                        Button(
                            onClick        = onAddToCart,
                            modifier       = Modifier.fillMaxSize(),
                            shape          = RoundedCornerShape(10.dp),
                            colors         = ButtonDefaults.buttonColors(
                                containerColor = androidx.compose.ui.graphics.Color.Transparent
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text  = "ADD TO CART",
                                style = FlipzonType.buttonLabel.copy(fontSize = 11.sp)
                            )
                        }
                    }
                        }
                    }
                }
            }
        }



@Composable
private fun ErrorState(error: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text  = "Something went wrong",
                style = FlipzonType.displayMedium.copy(color = FlipzonColors.TextPrimary)
            )
            Text(
                text  = error,
                style = FlipzonType.bodyLight
            )
            GoldButton(text = "RETRY", onClick = onRetry)
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, style = FlipzonType.bodyLight)
    }
}

@Composable
fun GoldButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick        = onClick,
        enabled        = !isLoading,
        modifier       = modifier.height(50.dp),
        shape          = RoundedCornerShape(14.dp),
        colors         = ButtonDefaults.buttonColors(
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (!isLoading) FlipzonGradients.goldHorizontal
                    else FlipzonGradients.goldHorizontalDimmed,
                    RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color       = FlipzonColors.MidnightDeep
                )
            } else {
                Text(text = text, style = FlipzonType.buttonLabel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyProducts = listOf(
        Product(id = 1, title = "Smartphone", price = 599.0, thumbnail = ""),
        Product(id = 2, title = "Laptop",     price = 1299.0, thumbnail = "")
    )

    MaterialTheme {
        HomeScreenContent(
            products            = dummyProducts,
            searchQuery         = "",
            isLoading           = false,
            error               = null,
            cartItems           = emptyList(),
            onSearchQueryChange = {},
            onRetry             = {},
            onLoadMore          = {},
            onAddToCart         = {},
            onIncrement         = {},
            onDecrement         = {}
        )
    }
}