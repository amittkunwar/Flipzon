package com.flipzon.app.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.flipzon.app.viewmodel.AuthViewModel

// ─── Color Palette ────────────────────────────────────────────────────────────
private val MidnightDeep   = Color(0xFF060B18)
private val MidnightMid    = Color(0xFF0D1628)
private val NavyAccent     = Color(0xFF152040)
private val GoldPrimary    = Color(0xFFD4A843)
private val GoldLight      = Color(0xFFF0C96A)
private val GoldDim        = Color(0xFF8A6A20)
private val SurfaceGlass   = Color(0x1AFFFFFF)
private val SurfaceBorder  = Color(0x33D4A843)
private val TextPrimary    = Color(0xFFF0EAD6)
private val TextSecondary  = Color(0xFF8A95AB)
private val ErrorRed       = Color(0xFFFF6B6B)

// ─── Typography ───────────────────────────────────────────────────────────────
// Add these font files to res/font/:
//   playfair_display_bold.ttf     → https://fonts.google.com/specimen/Playfair+Display
//   dm_sans_regular.ttf           → https://fonts.google.com/specimen/DM+Sans
//   dm_sans_medium.ttf
//   dm_sans_light.ttf
//
// Then uncomment the FontFamily declarations below and remove the Fallback ones.

// val PlayfairDisplay = FontFamily(Font(R.font.playfair_display_bold, FontWeight.Bold))
// val DmSans = FontFamily(
//     Font(R.font.dm_sans_light,   FontWeight.Light),
//     Font(R.font.dm_sans_regular, FontWeight.Normal),
//     Font(R.font.dm_sans_medium,  FontWeight.Medium),
// )

// Fallback (system serif / sans) — swap for the above once fonts are added
private val PlayfairDisplay = FontFamily.Serif
private val DmSans          = FontFamily.SansSerif

// ─── Wired-up screen ──────────────────────────────────────────────────────────
@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState = authViewModel.loginState

    LaunchedEffect(loginState) {
        if (loginState is AuthViewModel.LoginState.Success) {
            navController.navigate("main") { popUpTo("login") { inclusive = true } }
        }
    }

    LoginScreenContent(
        email           = email,
        onEmailChange   = { email = it },
        password        = password,
        onPasswordChange = { password = it },
        isLoading       = loginState is AuthViewModel.LoginState.Loading,
        errorMessage    = (loginState as? AuthViewModel.LoginState.Error)?.message,
        onLoginClick    = { authViewModel.login(email, password) }
    )
}

// ─── Stateless UI ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    // Subtle pulsing glow animation on the accent orb
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            tween(2800, easing = EaseInOutSine),
            RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MidnightDeep, MidnightMid, NavyAccent)
                )
            )
    ) {
        // ── Decorative gold orb ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(340.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-80).dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GoldPrimary.copy(alpha = glowAlpha),
                                GoldPrimary.copy(alpha = 0f)
                            )
                        )
                    )
                }
        )

        // ── Bottom accent orb ────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 80.dp)
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1A3A6E).copy(alpha = 0.7f),
                                Color.Transparent
                            )
                        )
                    )
                }
        )

        // ── Card ─────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── App title block ───────────────────────────────────────────
            Spacer(Modifier.height(16.dp))

            // Thin gold rule above wordmark
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(1.5.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, GoldPrimary, Color.Transparent)
                        )
                    )
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "FLIPZON",
                style = TextStyle(
                    fontFamily = PlayfairDisplay,
                    fontWeight  = FontWeight.Bold,
                    fontSize    = 42.sp,
                    letterSpacing = 8.sp,
                    brush = Brush.horizontalGradient(
                        listOf(GoldDim, GoldLight, GoldPrimary, GoldLight, GoldDim)
                    )
                )
            )

            Text(
                text = "PREMIUM SHOPPING",
                style = TextStyle(
                    fontFamily    = DmSans,
                    fontWeight    = FontWeight.Light,
                    fontSize      = 11.sp,
                    letterSpacing = 5.sp,
                    color         = TextSecondary
                )
            )

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(1.5.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, GoldPrimary, Color.Transparent)
                        )
                    )
            )

            Spacer(Modifier.height(40.dp))

            // ── Glass card ────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceGlass)
                    .drawBehind {
                        // Gold border stroke via drawRoundRect
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GoldPrimary.copy(alpha = 0.6f),
                                    Color.Transparent,
                                    GoldPrimary.copy(alpha = 0.3f)
                                )
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx()),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx())
                        )
                    }
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text  = "Welcome back",
                        style = TextStyle(
                            fontFamily = PlayfairDisplay,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 26.sp,
                            color      = TextPrimary
                        )
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text  = "Sign in to continue your experience",
                        style = TextStyle(
                            fontFamily = DmSans,
                            fontWeight = FontWeight.Light,
                            fontSize   = 13.sp,
                            color      = TextSecondary,
                            textAlign  = TextAlign.Center
                        )
                    )

                    Spacer(Modifier.height(28.dp))

                    // Email field
                    GoldOutlinedTextField(
                        value         = email,
                        onValueChange = onEmailChange,
                        label         = "Email address",
                        keyboardType  = KeyboardType.Email
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password field
                    GoldOutlinedTextField(
                        value            = password,
                        onValueChange    = onPasswordChange,
                        label            = "Password",
                        keyboardType     = KeyboardType.Password,
                        isPassword       = true,
                        passwordVisible  = passwordVisible,
                        onTogglePassword = { passwordVisible = !passwordVisible }
                    )

                    // Forgot password
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = { /* navigate to forgot password */ },
                            modifier = Modifier.align(Alignment.CenterEnd),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 0.dp)
                        ) {
                            Text(
                                text  = "Forgot password?",
                                style = TextStyle(
                                    fontFamily = DmSans,
                                    fontSize   = 12.sp,
                                    color      = GoldPrimary
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Error
                    errorMessage?.let {
                        Text(
                            text  = it,
                            style = TextStyle(
                                fontFamily = DmSans,
                                fontSize   = 13.sp,
                                color      = ErrorRed
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }

                    // Login button
                    Button(
                        onClick  = onLoginClick,
                        enabled  = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (!isLoading)
                                        Brush.horizontalGradient(listOf(GoldDim, GoldPrimary, GoldLight))
                                    else
                                        Brush.horizontalGradient(listOf(GoldDim.copy(0.5f), GoldDim.copy(0.5f))),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier  = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color     = MidnightDeep
                                )
                            } else {
                                Text(
                                    text  = "SIGN IN",
                                    style = TextStyle(
                                        fontFamily    = DmSans,
                                        fontWeight    = FontWeight.Medium,
                                        fontSize      = 14.sp,
                                        letterSpacing = 3.sp,
                                        color         = MidnightDeep
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Divider row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = SurfaceBorder)
                        Text(
                            text  = "  or  ",
                            style = TextStyle(fontFamily = DmSans, fontSize = 12.sp, color = TextSecondary)
                        )
                        Divider(modifier = Modifier.weight(1f), color = SurfaceBorder)
                    }

                    Spacer(Modifier.height(20.dp))

                    // Sign-up nudge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text  = "New to Flipzon? ",
                            style = TextStyle(fontFamily = DmSans, fontSize = 13.sp, color = TextSecondary)
                        )
                        TextButton(
                            onClick = { /* navigate to register */ },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text  = "Create account",
                                style = TextStyle(
                                    fontFamily = DmSans,
                                    fontWeight = FontWeight.Medium,
                                    fontSize   = 13.sp,
                                    color      = GoldPrimary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─── Reusable gold-themed text field ─────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoldOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    val visualTransformation =
        if (isPassword && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None

    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text  = label,
                style = TextStyle(fontFamily = DmSans, fontSize = 13.sp)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = if (isPassword && onTogglePassword != null) {
            {
                IconButton(onClick = onTogglePassword) {
                    Text(
                        text  = if (passwordVisible) "👁" else "👁‍🗨",
                        fontSize = 16.sp
                    )
                }
            }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(14.dp),
        colors   = OutlinedTextFieldDefaults.colors(
            focusedBorderColor    = GoldPrimary,
            unfocusedBorderColor  = SurfaceBorder,
            focusedLabelColor     = GoldPrimary,
            unfocusedLabelColor   = TextSecondary,
            cursorColor           = GoldPrimary,
            focusedTextColor      = TextPrimary,
            unfocusedTextColor    = TextPrimary.copy(alpha = 0.85f),
            focusedContainerColor = Color(0x14FFFFFF),
            unfocusedContainerColor = Color(0x0AFFFFFF)
        ),
        textStyle = TextStyle(fontFamily = DmSans, fontSize = 15.sp)
    )
}

// ─── Preview ─────────────────────────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFF060B18)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            email            = "hello@flipzon.com",
            onEmailChange    = {},
            password         = "••••••••••",
            onPasswordChange = {},
            isLoading        = false,
            errorMessage     = null,
            onLoginClick     = {}
        )
    }
}