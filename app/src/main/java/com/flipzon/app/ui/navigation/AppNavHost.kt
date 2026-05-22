package com.flipzon.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.flipzon.app.ui.auth.LoginScreen
import com.flipzon.app.ui.main.MainScreen
import com.flipzon.app.ui.theme.*
import com.flipzon.app.viewmodel.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost() {
    val navController: NavHostController = rememberNavController()
    val mainViewModel: MainViewModel     = hiltViewModel()
    val session by mainViewModel.sessionState.collectAsState()

    // ── Splash / loading state ────────────────────────────────────────────────
    if (session == null) {
        SplashScreen()
        return
    }

    val startDest = if (session!!.isValid) "main" else "login"

    NavHost(
        navController    = navController,
        startDestination = startDest,
        enterTransition  = {
            fadeIn(tween(350)) + slideInHorizontally(tween(350)) { it / 6 }
        },
        exitTransition   = {
            fadeOut(tween(250)) + slideOutHorizontally(tween(250)) { -it / 6 }
        },
        popEnterTransition = {
            fadeIn(tween(350)) + slideInHorizontally(tween(350)) { -it / 6 }
        },
        popExitTransition = {
            fadeOut(tween(250)) + slideOutHorizontally(tween(250)) { it / 6 }
        }
    ) {
        composable("login") { LoginScreen(navController) }
        composable("main")  { MainScreen(rootNavController = navController) }
    }
}

// ── Branded splash shown while DataStore loads ────────────────────────────────
@Composable
private fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FlipzonGradients.backgroundVertical),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            enter   = fadeIn(tween(600))
        ) {
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                // Gold rule
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.5.dp)
                        .background(
                            FlipzonGradients.goldHorizontal,
                            androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                        )
                )

                // Wordmark
                androidx.compose.material3.Text(
                    text  = "FLIPZON",
                    style = TextStyle(
                        fontFamily    = PlayfairDisplay,
                        fontSize      = 40.sp,
                        letterSpacing = 8.sp,
                        brush         = FlipzonGradients.goldText
                    )
                )

                // Tagline
                androidx.compose.material3.Text(
                    text  = "PREMIUM SHOPPING",
                    style = FlipzonType.labelCaps
                )

                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.5.dp)
                        .background(
                            FlipzonGradients.goldHorizontal,
                            androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                        )
                )

                androidx.compose.foundation.layout.Spacer(
                    Modifier.height(24.dp)
                )

                // Spinner
                androidx.compose.material3.CircularProgressIndicator(
                    color       = FlipzonColors.GoldPrimary,
                    strokeWidth = 2.dp,
                    modifier    = Modifier.size(24.dp)
                )
            }
        }
    }
}