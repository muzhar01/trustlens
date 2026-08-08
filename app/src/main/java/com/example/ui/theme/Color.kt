package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// 1. Mobilink Bank TrustLens Core Mobile Tokens
val MobilinkNavy = Color(0xFF0F2537)           // Primary Dark: App Bar, Dark Headers, Primary Buttons
val MobilinkNavyDark = Color(0xFF081724)       // Deep Navy
val MobilinkTeal = Color(0xFF00A896)           // Brand Accent: Active Taps, Stepper Highlights, Radio Active
val MobilinkTealLight = Color(0xFFE0F7F5)      // Light Teal Pill / Container
val MobilinkTealContainer = Color(0xFFCCF2EE)  // Teal Container

// Status & Risk Palette
val TrustEmerald = Color(0xFF059669)           // Low Risk / Auto-Approved / Verified Status
val TrustEmeraldLight = Color(0xFFECFDF5)      // Low Risk BG Tint
val TrustEmeraldBorder = Color(0xFFA7F3D0)     // Low Risk Border

val AlertAmber = Color(0xFFD97706)             // Medium Risk / Standard Manual Review Queue
val AlertAmberLight = Color(0xFFFFFBEB)        // Medium Risk BG Tint
val AlertAmberBorder = Color(0xFFFDE68A)       // Medium Risk Border

val CriticalCrimson = Color(0xFFDC2626)        // High Risk / EDD Queue Routing / Anomaly Flags
val CriticalCrimsonLight = Color(0xFFFEF2F2)   // High Risk BG Tint (#FEF2F2 per spec)
val CriticalCrimsonBorder = Color(0xFFFCA5A5)  // High Risk Border Tint

// Neutral & Card Surfaces
val NeutralSurface = Color(0xFFF8FAFC)         // Mobile Screen Background (#F8FAFC per spec)
val CardSurface = Color(0xFFFFFFFF)            // Form Cards, Accordions (#FFFFFF per spec)
val BorderSubtle = Color(0xFFE2E8F0)           // Border 1px
val BorderMedium = Color(0xFFCBD5E1)           // Active Border
val TextPrimary = Color(0xFF0F172A)            // Text Primary
val TextSecondary = Color(0xFF475569)          // Text Muted / Secondary
val TextTertiary = Color(0xFF94A3B8)           // Text Light / Placeholder

// Legacy Aliases mapped to Mobilink Theme for complete backward compatibility
val TrustLensPrimary = MobilinkNavy
val TrustLensPrimaryContainer = MobilinkTealLight
val TrustLensOnPrimary = Color(0xFFFFFFFF)
val TrustLensOnPrimaryContainer = MobilinkNavy
val TrustLensPrimaryFixedDim = MobilinkTeal
val TrustLensPrimaryFixed = MobilinkTealLight
val TrustLensSurfaceVariant = NeutralSurface

val TrustLensSecondary = MobilinkTeal
val TrustLensSecondaryContainer = MobilinkTealContainer
val TrustLensOnSecondary = Color(0xFFFFFFFF)
val TrustLensOnSecondaryContainer = MobilinkNavy
val TrustLensSecondaryFixed = MobilinkTealContainer
val TrustLensSecondaryFixedDim = MobilinkTeal

val TrustLensTertiary = CriticalCrimson
val TrustLensTertiaryContainer = CriticalCrimsonLight
val TrustLensOnTertiaryContainer = CriticalCrimson

val TrustLensBackground = NeutralSurface
val TrustLensSurface = CardSurface
val TrustLensSurfaceBright = CardSurface
val TrustLensSurfaceDim = Color(0xFFEDEFEF)
val TrustLensSurfaceContainer = Color(0xFFF1F5F9)
val TrustLensSurfaceContainerLow = NeutralSurface
val TrustLensSurfaceContainerHigh = Color(0xFFE2E8F0)
val TrustLensSurfaceContainerHighest = Color(0xFFCBD5E1)
val TrustLensSurfaceContainerLowest = CardSurface

val TrustLensOnBackground = TextPrimary
val TrustLensOnSurface = TextPrimary
val TrustLensOnSurfaceVariant = TextSecondary
val TrustLensOutline = BorderMedium
val TrustLensOutlineVariant = BorderSubtle
val TrustLensSurfaceTint = MobilinkTeal

val TrustLensNavBackground = CardSurface
val TrustLensNavBorder = BorderSubtle
val TrustLensNavActivePill = MobilinkTealContainer
val TrustLensNavActiveContent = MobilinkNavy

val TrustLensError = CriticalCrimson
val TrustLensErrorContainer = CriticalCrimsonLight
val TrustLensOnErrorContainer = CriticalCrimson
val TrustLensOnError = Color(0xFFFFFFFF)

val TrustLensAiBoxBg = CriticalCrimsonLight
val TrustLensAiBoxBorder = CriticalCrimsonBorder
val TrustLensAiRed = CriticalCrimson
val TrustLensAiBadgeBg = CardSurface


