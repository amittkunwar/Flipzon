package com.flipzon.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Color Palette ────────────────────────────────────────────────────────────
object FlipzonColors {
    val MidnightDeep    = Color(0xFF060B18)
    val MidnightMid     = Color(0xFF0D1628)
    val NavyAccent      = Color(0xFF152040)
    val NavyCard        = Color(0xFF0F1C35)
    val NavySurface     = Color(0xFF111E38)

    val GoldPrimary     = Color(0xFFD4A843)
    val GoldLight       = Color(0xFFF0C96A)
    val GoldDim         = Color(0xFF8A6A20)
    val GoldFaint       = Color(0x1AD4A843)

    val SurfaceGlass    = Color(0x14FFFFFF)
    val SurfaceBorder   = Color(0x33D4A843)
    val SurfaceBorderDim= Color(0x1AD4A843)

    val TextPrimary     = Color(0xFFF0EAD6)
    val TextSecondary   = Color(0xFF8A95AB)
    val TextHint        = Color(0xFF4A5568)

    val ErrorRed        = Color(0xFFFF6B6B)
    val SuccessGreen    = Color(0xFF4CAF7D)
}

// ─── Gradient Brushes ─────────────────────────────────────────────────────────
object FlipzonGradients {
    val backgroundVertical = Brush.verticalGradient(
        colors = listOf(
            FlipzonColors.MidnightDeep,
            FlipzonColors.MidnightMid,
            FlipzonColors.NavyAccent
        )
    )

    val goldHorizontal = Brush.horizontalGradient(
        colors = listOf(
            FlipzonColors.GoldDim,
            FlipzonColors.GoldPrimary,
            FlipzonColors.GoldLight
        )
    )

    val goldHorizontalDimmed = Brush.horizontalGradient(
        colors = listOf(
            FlipzonColors.GoldDim.copy(alpha = 0.5f),
            FlipzonColors.GoldDim.copy(alpha = 0.5f)
        )
    )

    val goldText = Brush.horizontalGradient(
        colors = listOf(
            FlipzonColors.GoldDim,
            FlipzonColors.GoldLight,
            FlipzonColors.GoldPrimary,
            FlipzonColors.GoldLight,
            FlipzonColors.GoldDim
        )
    )
}

// ─── Typography ───────────────────────────────────────────────────────────────
// Add these font files to res/font/ and uncomment:
//   playfair_display_bold.ttf  → https://fonts.google.com/specimen/Playfair+Display
//   dm_sans_light.ttf          → https://fonts.google.com/specimen/DM+Sans
//   dm_sans_regular.ttf
//   dm_sans_medium.ttf
//
// val PlayfairDisplay = FontFamily(Font(R.font.playfair_display_bold, FontWeight.Bold))
// val DmSans = FontFamily(
//     Font(R.font.dm_sans_light,   FontWeight.Light),
//     Font(R.font.dm_sans_regular, FontWeight.Normal),
//     Font(R.font.dm_sans_medium,  FontWeight.Medium),
// )

val PlayfairDisplay = FontFamily.Serif   // swap for real font after adding to res/font
val DmSans          = FontFamily.SansSerif

object FlipzonType {
    val displayLarge = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize   = 28.sp
    )
    val displayMedium = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize   = 22.sp
    )
    val labelCaps = TextStyle(
        fontFamily    = DmSans,
        fontWeight    = FontWeight.Medium,
        fontSize      = 11.sp,
        letterSpacing = 3.sp,
        color         = FlipzonColors.TextSecondary
    )
    val bodyRegular = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        color      = FlipzonColors.TextPrimary
    )
    val bodyLight = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight.Light,
        fontSize   = 13.sp,
        color      = FlipzonColors.TextSecondary
    )
    val priceLarge = TextStyle(
        fontFamily = PlayfairDisplay,
        fontWeight = FontWeight.Bold,
        fontSize   = 20.sp,
        color      = FlipzonColors.GoldPrimary
    )
    val priceMedium = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight.Medium,
        fontSize   = 15.sp,
        color      = FlipzonColors.GoldPrimary
    )
    val buttonLabel = TextStyle(
        fontFamily    = DmSans,
        fontWeight    = FontWeight.Medium,
        fontSize      = 12.sp,
        letterSpacing = 2.sp,
        color         = FlipzonColors.MidnightDeep
    )
}