# Flipzon 🛍️

A premium dark-luxury Android shopping app built with **Jetpack Compose**, featuring a deep midnight navy + gold aesthetic throughout.

---

## Screenshots

| Login | Home | Cart |
|-------|------|------|
| Dark gradient login with gold wordmark | Product listing with search | Cart summary with order total |

---

## Tech Stack

| Layer | Library |
|---|---|
| UI | Jetpack Compose (Material 3) |
| Navigation | Navigation Compose |
| DI | Hilt |
| Image Loading | Coil (`coil-compose`) |
| State | ViewModel + StateFlow |
| Local DB | Room (`CartEntity`) |
| Fonts | Playfair Display, DM Sans (Google Fonts) |

---

## Project Structure

```
app/src/main/java/com/flipzon/app/
│
├── ui/
│   ├── auth/
│   │   └── LoginScreen.kt          # Branded login with gradient + glass card
│   ├── home/
│   │   └── HomeScreen.kt           # Product list, search, add-to-cart
│   ├── cart/
│   │   └── CartScreen.kt           # Cart items, quantity stepper, order total
│   ├── main/
│   │   └── MainScreen.kt           # Scaffold with themed TopBar + BottomNav
│   ├── navigation/
│   │   └── AppNavHost.kt           # Root nav + branded splash screen
│   └── theme/
│       └── FlipzonTheme.kt         # Colors, gradients, typography tokens
│
├── viewmodel/
│   ├── AuthViewModel.kt
│   ├── HomeViewModel.kt
│   ├── CartViewModel.kt
│   └── MainViewModel.kt
│
├── model/
│   └── Product.kt
│
└── database/
    └── CartEntity.kt
```

---

## Setup

### 1. Clone the repository
```bash
git clone https://github.com/your-username/flipzon.git
cd flipzon
```

### 2. Add custom fonts *(optional but recommended)*

Download and place in `app/src/main/res/font/`:

| File | Source |
|---|---|
| `playfair_display_bold.ttf` | [Google Fonts – Playfair Display](https://fonts.google.com/specimen/Playfair+Display) |
| `dm_sans_light.ttf` | [Google Fonts – DM Sans](https://fonts.google.com/specimen/DM+Sans) |
| `dm_sans_regular.ttf` | same |
| `dm_sans_medium.ttf` | same |

Then in `FlipzonTheme.kt`, uncomment the `FontFamily` declarations and remove the fallback lines.

### 3. Add dependencies

In `app/build.gradle.kts`:

```kotlin
dependencies {
    // Coil — image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Material Icons Extended — Remove, ShoppingBag, Logout, etc.
    implementation("androidx.compose.material:material-icons-extended")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
```

### 4. Build & Run
```bash
./gradlew assembleDebug
```
Or just hit **Run ▶** in Android Studio.

---

## Design System

All design tokens live in `FlipzonTheme.kt` — one file to rule them all.

### Color Palette

| Token | Hex | Usage |
|---|---|---|
| `MidnightDeep` | `#060B18` | App background base |
| `MidnightMid` | `#0D1628` | Background mid |
| `NavyAccent` | `#152040` | Background bottom |
| `NavyCard` | `#0F1C35` | Card backgrounds |
| `GoldPrimary` | `#D4A843` | Buttons, icons, accents |
| `GoldLight` | `#F0C96A` | Highlights, subtotals |
| `GoldDim` | `#8A6A20` | Gradient start |
| `TextPrimary` | `#F0EAD6` | Main text |
| `TextSecondary` | `#8A95AB` | Labels, hints |
| `ErrorRed` | `#FF6B6B` | Error messages |

### Gradients

| Token | Usage |
|---|---|
| `backgroundVertical` | Full-screen background on every screen |
| `goldHorizontal` | Buttons, borders, rule lines |
| `goldText` | Wordmark "FLIPZON" brush |

### Typography

| Token | Font | Usage |
|---|---|---|
| `displayLarge` | Playfair Display Bold 28sp | Page titles |
| `displayMedium` | Playfair Display Bold 22sp | Card headings |
| `labelCaps` | DM Sans Medium 11sp + 3sp tracking | Section headers, tags |
| `bodyRegular` | DM Sans Regular 14sp | Product names, body |
| `priceLarge` | Playfair Display Bold 20sp | Order total |
| `priceMedium` | DM Sans Medium 15sp | Product prices |
| `buttonLabel` | DM Sans Medium 12sp + 2sp tracking | Button text |

---

## Key Screens

### LoginScreen
- Animated gold radial glow orbs
- Frosted glass card with hairline gold border
- Gradient gold `SIGN IN` button
- `Playfair Display` wordmark with 5-stop gold gradient brush

### HomeScreen
- Gold-accented search bar with glass fill
- `ALL PRODUCTS` section header with vertical gold rule
- `ProductCard` — navy card, gold border stroke via `drawBehind`
- Quantity stepper with `GoldFaint` pill background
- Infinite scroll with `loadMore()` trigger at list end

### CartScreen
- Same card treatment as `ProductCard`
- Per-item subtotal (`qty × price`) in `GoldLight`
- Rounded-top checkout panel (`NavySurface`) with gold border
- Themed `Snackbar` on success/error

### MainScreen
- Custom `TopBar` (no `TopAppBar`) — gold avatar ring, centered wordmark, glass logout button
- Custom `BottomNav` (no `NavigationBar`) — gold hairline top border, selected tab glows gold

### AppNavHost
- Branded splash screen while `DataStore` loads (no blank flash)
- Smooth `fadeIn + slideInHorizontally` route transitions

---

## License

```
MIT License — free to use, modify, and distribute.
```
