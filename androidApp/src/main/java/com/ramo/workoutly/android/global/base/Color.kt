package com.ramo.workoutly.android.global.base

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

val Color.darker: Color
    get() = Color(ColorUtils.blendARGB(this@darker.toArgb(), Color(red = 109, green = 157, blue = 241).toArgb(), 0.15F))

fun Color.darker(f: Float = 0.15F): Color = Color(ColorUtils.blendARGB(this@darker.toArgb(), Color(red = 109, green = 157, blue = 241).toArgb(), f))

@androidx.compose.runtime.Composable
fun Theme.outlinedTextFieldStyle(): androidx.compose.material3.TextFieldColors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = textColor,
    errorTextColor = textColor,
    errorSupportingTextColor = error,
    unfocusedBorderColor = textGrayColor,
    focusedPlaceholderColor = textColor,
    unfocusedPlaceholderColor = textHintColor,
    focusedLabelColor = textColor,
    unfocusedLabelColor =  textHintColor,
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
    errorBorderColor = error,
)

fun Color.marge(topColor: Color, ratio: Float = 0.5f): Color {
    if (ratio == 0f) return this
    if (ratio == 1f) return topColor
    val intColor = ColorUtils.blendARGB(toArgb(), topColor.toArgb(), ratio)
    return Color(intColor)
}

fun Color.lighten(factor: Float): Color {
    return Color(
        red = red + (1 - red) * factor,
        green = green + (1 - green) * factor,
        blue = blue + (1 - blue) * factor,
        alpha = alpha
    )
}

fun Color.darken(factor: Float): Color {
    return Color(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor),
        alpha = alpha
    )
}

fun Color.getGradientFromBaseColor(second: Color): Brush {
    return Brush.linearGradient(
        colors = listOf(this@getGradientFromBaseColor, second)
    )
}