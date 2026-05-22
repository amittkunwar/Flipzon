package com.flipzon.app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.flipzon.app.ui.cart.CartScreen
import com.flipzon.app.ui.home.HomeScreen
import com.flipzon.app.ui.theme.*
import com.flipzon.app.viewmodel.AuthViewModel
import com.flipzon.app.viewmodel.MainViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.flipzon.app.datastore.UserSession

@Composable
fun MainScreen(rootNavController: NavHostController) {
    val bottomNavController = rememberNavController()
    val mainViewModel: MainViewModel   = hiltViewModel()
    val authViewModel: AuthViewModel   = hiltViewModel()
    val session by mainViewModel.sessionState.collectAsState()

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute      = navBackStackEntry?.destination?.route

    MainScreenContent(
        session = session,
        currentRoute = currentRoute,
        onLogout = {
            authViewModel.logout()
            rootNavController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        },
        onNavigateBottom = { route ->
            bottomNavController.navigate(route) {
                popUpTo(bottomNavController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState    = true
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlipzonGradients.backgroundVertical)
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController    = bottomNavController,
                    startDestination = "home_tab"
                ) {
                    composable("home_tab") { HomeScreen() }
                    composable("cart_tab") { CartScreen() }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenContent(
    session: UserSession?,
    currentRoute: String?,
    onLogout: () -> Unit,
    onNavigateBottom: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // ── Themed top bar ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(FlipzonColors.MidnightDeep, FlipzonColors.MidnightMid)
                        )
                    )
                    .drawBehind {
                        // Gold hairline at bottom
                        drawLine(
                            brush       = FlipzonGradients.goldHorizontal,
                            start       = androidx.compose.ui.geometry.Offset(0f, size.height),
                            end         = androidx.compose.ui.geometry.Offset(size.width, size.height),
                            strokeWidth = 0.8.dp.toPx(),
                            alpha       = 0.5f
                        )
                    }
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment       = Alignment.CenterVertically,
                    horizontalArrangement   = Arrangement.SpaceBetween,
                    modifier                = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(FlipzonColors.NavyCard)
                                .drawBehind {
                                    drawCircle(
                                        brush = FlipzonGradients.goldHorizontal,
                                        style = Stroke(width = 1.2.dp.toPx()),
                                        alpha = 0.7f
                                    )
                                }
                        ) {
                            AsyncImage(
                                model              = session?.image,
                                contentDescription = "Profile",
                                contentScale       = ContentScale.Crop,
                                modifier           = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Column {
                            Text(
                                text  = "Welcome back",
                                style = FlipzonType.labelCaps.copy(fontSize = 9.sp)
                            )
                            Text(
                                text     = "${session?.firstName ?: ""} ${session?.lastName ?: ""}".trim()
                                    .ifEmpty { "Guest" },
                                style    = FlipzonType.bodyRegular.copy(fontSize = 15.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Wordmark (compact)
                    Text(
                        text  = "FLIPZON",
                        style = TextStyle(
                            fontFamily    = PlayfairDisplay,
                            fontSize      = 18.sp,
                            letterSpacing = 4.sp,
                            brush         = FlipzonGradients.goldText
                        )
                    )

                    // Logout
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(FlipzonColors.SurfaceGlass)
                    ) {
                        Icon(
                            Icons.Filled.Logout,
                            contentDescription = "Logout",
                            tint     = FlipzonColors.GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        },

        bottomBar = {
            // ── Themed bottom nav ────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FlipzonColors.MidnightDeep)
                    .drawBehind {
                        drawLine(
                            brush       = FlipzonGradients.goldHorizontal,
                            start       = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end         = androidx.compose.ui.geometry.Offset(size.width, 0f),
                            strokeWidth = 0.8.dp.toPx(),
                            alpha       = 0.45f
                        )
                    }
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    NavItem(
                        label     = "HOME",
                        icon      = Icons.Filled.Home,
                        selected  = currentRoute == "home_tab",
                        onClick   = { onNavigateBottom("home_tab") }
                    )
                    NavItem(
                        label     = "CART",
                        icon      = Icons.Filled.ShoppingCart,
                        selected  = currentRoute == "cart_tab",
                        onClick   = { onNavigateBottom("cart_tab") }
                    )
                }
            }
        },
        content = content
    )
}

@Composable
private fun NavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint       = if (selected) FlipzonColors.GoldPrimary else FlipzonColors.TextHint
    val bgColor    = if (selected) FlipzonColors.GoldFaint   else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .run {
                if (selected) drawBehind {
                    drawRoundRect(
                        brush        = FlipzonGradients.goldHorizontal,
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        style        = Stroke(width = 0.8.dp.toPx()),
                        alpha        = 0.5f
                    )
                } else this
            }
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = tint,
                modifier           = Modifier.size(22.dp)
            )
        }
        Text(
            text  = label,
            style = FlipzonType.labelCaps.copy(
                fontSize = 9.sp,
                color    = tint
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreenContent(
            session = UserSession(userId = 1, email = "john@example.com", firstName = "John", lastName = "Doe", image = ""),
            currentRoute = "home_tab",
            onLogout = {},
            onNavigateBottom = {},
            content = {
                Box(modifier = Modifier.fillMaxSize().background(FlipzonColors.NavySurface))
            }
        )
    }
}