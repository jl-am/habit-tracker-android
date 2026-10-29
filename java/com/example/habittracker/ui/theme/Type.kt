package com.example.habittracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.habittracker.R

// Lora: headings
val LoraFont = FontFamily(
    Font(R.font.lora_variable, FontWeight.Normal)
)

// Nunito Sans: body text, buttons, and labels
val NunitoSansFont = FontFamily(
    Font(R.font.nunito_sans_variable, FontWeight.Normal)
)

// App-wide Material 3 typography
val Typography = Typography(

    // Large screen headings
    displayLarge = TextStyle(
        fontFamily = LoraFont,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),

    // Screen titles
    headlineLarge = TextStyle(
        fontFamily = LoraFont,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 38.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = LoraFont,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 34.sp
    ),

    // Smaller headings
    titleLarge = TextStyle(
        fontFamily = LoraFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),

    titleMedium = TextStyle(
        fontFamily = NunitoSansFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    // Main body text
    bodyLarge = TextStyle(
        fontFamily = NunitoSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = NunitoSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Buttons and prominent labels
    labelLarge = TextStyle(
        fontFamily = NunitoSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    labelMedium = TextStyle(
        fontFamily = NunitoSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),

    // Small labels
    labelSmall = TextStyle(
        fontFamily = NunitoSansFont,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)
