package com.flipzon.app.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.flipzon.app.database.CartEntity
import com.flipzon.app.ui.home.GoldButton
import com.flipzon.app.ui.theme.*
import com.flipzon.app.viewmodel.CartViewModel
import com.flipzon.app.viewmodel.CheckoutState
import java.util.Locale

import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {
    val cartItems      by viewModel.cartItems.collectAsState()
    val totalPrice     by viewModel.totalPrice.collectAsState()
    val checkoutState  by viewModel.checkoutState.collectAsState()

    CartScreenContent(
        cartItems = cartItems,
        totalPrice = totalPrice,
        checkoutState = checkoutState,
        onCheckout = { viewModel.checkout() },
        onIncrement = { viewModel.incrementQuantity(it) },
        onDecrement = { viewModel.decrementQuantity(it) },
        onResetCheckoutState = { viewModel.resetCheckoutState() }
    )
}

@Composable
fun CartScreenContent(
    cartItems: List<CartEntity>,
    totalPrice: Double,
    checkoutState: CheckoutState?,
    onCheckout: () -> Unit,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    onResetCheckoutState: () -> Unit
) {
    val snackbarHost   = remember { SnackbarHostState() }

    LaunchedEffect(checkoutState) {
        when (checkoutState) {
            is CheckoutState.Error -> {
                snackbarHost.showSnackbar(checkoutState.message)
                onResetCheckoutState()
            }
            CheckoutState.Success  -> {
                snackbarHost.showSnackbar("Order placed successfully!")
                onResetCheckoutState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost     = {
            SnackbarHost(snackbarHost) { data ->
                Snackbar(
                    snackbarData     = data,
                    containerColor   = FlipzonColors.NavySurface,
                    contentColor     = FlipzonColors.TextPrimary,
                    actionColor      = FlipzonColors.GoldPrimary,
                    shape            = RoundedCornerShape(14.dp)
                )
            }
        },
        containerColor   = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FlipzonGradients.backgroundVertical)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // ── Section header ──────────────────────────────────────
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp).height(16.dp)
                            .background(
                                FlipzonGradients.goldHorizontal,
                                RoundedCornerShape(2.dp)
                            )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("YOUR CART", style = FlipzonType.labelCaps)
                    Spacer(Modifier.weight(1f))
                    if (cartItems.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FlipzonColors.GoldFaint)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text  = "${cartItems.size} items",
                                style = FlipzonType.labelCaps.copy(
                                    color = FlipzonColors.GoldPrimary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                // ── List / empty ────────────────────────────────────────
                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Filled.ShoppingBag,
                                contentDescription = null,
                                tint     = FlipzonColors.TextHint,
                                modifier = Modifier.size(52.dp)
                            )
                            Text("Your cart is empty", style = FlipzonType.bodyLight)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier            = Modifier.weight(1f),
                        contentPadding      = PaddingValues(
                            start = 16.dp, end = 16.dp,
                            top   = 4.dp,  bottom = 12.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cartItems.size) { index ->
                            CartItemCard(
                                item        = cartItems[index],
                                onIncrement = { onIncrement(cartItems[index].productId) },
                                onDecrement = { onDecrement(cartItems[index].productId) }
                            )
                        }
                    }
                }

                // ── Order summary & checkout ────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(FlipzonColors.NavySurface)
                        .drawBehind {
                            drawRoundRect(
                                brush        = FlipzonGradients.goldHorizontal,
                                cornerRadius = CornerRadius(24.dp.toPx()),
                                style        = Stroke(width = 0.8.dp.toPx()),
                                alpha        = 0.4f
                            )
                        }
                        .padding(horizontal = 22.dp, vertical = 20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        // Thin gold divider at top
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(2.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(
                                    FlipzonGradients.goldHorizontal,
                                    RoundedCornerShape(1.dp)
                                )
                        )

                        // Summary row
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically,
                            modifier              = Modifier.fillMaxWidth()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("ORDER TOTAL", style = FlipzonType.labelCaps)
                                Text(
                                    text  = String.format(Locale.US, "$%.2f", totalPrice),
                                    style = FlipzonType.priceLarge
                                )
                            }

                            // Item count pill
                            if (cartItems.isNotEmpty()) {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text("ITEMS", style = FlipzonType.labelCaps)
                                    Text(
                                        text  = "${cartItems.sumOf { it.quantity }}",
                                        style = FlipzonType.priceMedium
                                    )
                                }
                            }
                        }

                        // Checkout button
                        GoldButton(
                            text      = "PLACE ORDER",
                            onClick   = onCheckout,
                            isLoading = checkoutState == CheckoutState.Loading,
                            modifier  = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartEntity,
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
                    alpha        = 0.3f
                )
            }
    ) {
        Row(
            modifier          = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FlipzonColors.SurfaceGlass)
                    .border(
                        width = 0.6.dp,
                        color = FlipzonColors.SurfaceBorderDim,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model              = item.thumbnail,
                    contentDescription = item.title,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = item.title,
                    style    = FlipzonType.bodyRegular,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text  = "$${item.price}",
                    style = FlipzonType.priceMedium
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Stepper
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
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.Remove,
                                contentDescription = "Decrease",
                                tint     = FlipzonColors.GoldPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Text(
                            text  = item.quantity.toString(),
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize   = 14.sp,
                                color      = FlipzonColors.TextPrimary
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        IconButton(
                            onClick  = onIncrement,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Increase",
                                tint     = FlipzonColors.GoldPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }

                    // Item subtotal
                    Text(
                        text  = String.format(Locale.US, "$%.2f", item.price * item.quantity),
                        style = FlipzonType.priceMedium.copy(color = FlipzonColors.GoldLight)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val sampleItems = listOf(
        CartEntity(productId = 1, title = "Wireless Headphones", price = 99.99, thumbnail = "", quantity = 1),
        CartEntity(productId = 2, title = "Smart Watch", price = 149.99, thumbnail = "", quantity = 2)
    )
    MaterialTheme {
        CartScreenContent(
            cartItems = sampleItems,
            totalPrice = 399.97,
            checkoutState = null,
            onCheckout = {},
            onIncrement = {},
            onDecrement = {},
            onResetCheckoutState = {}
        )
    }
}