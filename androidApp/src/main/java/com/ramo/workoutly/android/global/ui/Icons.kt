@file:Suppress("unused")

package com.ramo.workoutly.android.global.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberExitToApp(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "exit_to_app",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16.375f, 26.958f)
                quadToRelative(-0.375f, -0.416f, -0.375f, -1f)
                quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                lineToRelative(3.667f, -3.708f)
                horizontalLineToRelative(-13.5f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadTo(5.208f, 20f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.395f, 0.959f, -0.395f)
                horizontalLineToRelative(13.5f)
                lineToRelative(-3.709f, -3.709f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.937f)
                quadToRelative(0f, -0.563f, 0.417f, -0.979f)
                quadToRelative(0.375f, -0.417f, 0.958f, -0.417f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                lineToRelative(6.041f, 6.083f)
                quadToRelative(0.209f, 0.209f, 0.313f, 0.438f)
                quadToRelative(0.104f, 0.229f, 0.104f, 0.479f)
                quadToRelative(0f, 0.292f, -0.104f, 0.5f)
                quadToRelative(-0.104f, 0.208f, -0.313f, 0.417f)
                lineTo(18.292f, 27f)
                quadToRelative(-0.417f, 0.417f, -0.959f, 0.396f)
                quadToRelative(-0.541f, -0.021f, -0.958f, -0.438f)
                close()
                moveTo(7.833f, 34.75f)
                quadToRelative(-1.041f, 0f, -1.833f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.875f)
                verticalLineToRelative(-6.791f)
                quadToRelative(0f, -0.584f, 0.375f, -0.959f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                quadToRelative(0.541f, 0f, 0.916f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.959f)
                verticalLineToRelative(6.791f)
                horizontalLineToRelative(24.292f)
                verticalLineTo(7.833f)
                horizontalLineTo(7.833f)
                verticalLineToRelative(6.875f)
                quadToRelative(0f, 0.584f, -0.375f, 0.959f)
                reflectiveQuadToRelative(-0.916f, 0.375f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineTo(7.833f)
                quadToRelative(0f, -1.083f, 0.792f, -1.854f)
                quadToRelative(0.792f, -0.771f, 1.833f, -0.771f)
                horizontalLineToRelative(24.292f)
                quadToRelative(1.083f, 0f, 1.875f, 0.771f)
                reflectiveQuadToRelative(0.792f, 1.854f)
                verticalLineToRelative(24.25f)
                quadToRelative(0f, 1.084f, -0.792f, 1.875f)
                quadToRelative(-0.792f, 0.792f, -1.875f, 0.792f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberDeleteText(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Delete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10f, 5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.344f, 0.519f)
                lineToRelative(-6.328f, 5.74f)
                arcToRelative(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 1.481f)
                lineToRelative(6.328f, 5.741f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 10f, 19f)
                horizontalLineToRelative(10f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, -2f)
                verticalLineTo(7f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, -2f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 9f)
                lineToRelative(6f, 6f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 9f)
                lineToRelative(-6f, 6f)
            }
        }.build()
    }
}

@Composable
fun rememberSearch(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "search",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(31.917f, 33.792f)
                lineToRelative(-9.75f, -9.75f)
                quadToRelative(-1.209f, 1.041f, -2.855f, 1.625f)
                quadToRelative(-1.645f, 0.583f, -3.479f, 0.583f)
                quadToRelative(-4.458f, 0f, -7.521f, -3.083f)
                quadToRelative(-3.062f, -3.084f, -3.062f, -7.459f)
                reflectiveQuadToRelative(3.062f, -7.437f)
                quadToRelative(3.063f, -3.063f, 7.48f, -3.063f)
                quadToRelative(4.375f, 0f, 7.437f, 3.063f)
                quadToRelative(3.063f, 3.062f, 3.063f, 7.479f)
                quadToRelative(0f, 1.75f, -0.584f, 3.396f)
                quadToRelative(-0.583f, 1.646f, -1.666f, 3.021f)
                lineToRelative(9.833f, 9.75f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.916f)
                quadToRelative(0f, 0.542f, -0.417f, 0.959f)
                quadToRelative(-0.416f, 0.375f, -0.979f, 0.375f)
                quadToRelative(-0.562f, 0f, -0.937f, -0.375f)
                close()
                moveTo(15.792f, 23.625f)
                quadToRelative(3.291f, 0f, 5.583f, -2.313f)
                quadToRelative(2.292f, -2.312f, 2.292f, -5.604f)
                quadToRelative(0f, -3.291f, -2.292f, -5.583f)
                quadToRelative(-2.292f, -2.292f, -5.583f, -2.292f)
                quadToRelative(-3.292f, 0f, -5.604f, 2.313f)
                quadToRelative(-2.313f, 2.312f, -2.313f, 5.562f)
                quadToRelative(0f, 3.292f, 2.313f, 5.604f)
                quadToRelative(2.312f, 2.313f, 5.604f, 2.313f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberComment(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "comment",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11.5f, 23.208f)
                horizontalLineToRelative(17.042f)
                quadToRelative(0.5f, 0f, 0.896f, -0.396f)
                quadToRelative(0.395f, -0.395f, 0.395f, -0.937f)
                reflectiveQuadToRelative(-0.395f, -0.937f)
                quadToRelative(-0.396f, -0.396f, -0.896f, -0.396f)
                horizontalLineTo(11.5f)
                quadToRelative(-0.583f, 0f, -0.958f, 0.396f)
                quadToRelative(-0.375f, 0.395f, -0.375f, 0.937f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0.375f, 0.396f, 0.958f, 0.396f)
                close()
                moveToRelative(0f, -5.25f)
                horizontalLineToRelative(17.042f)
                quadToRelative(0.5f, 0f, 0.896f, -0.375f)
                quadToRelative(0.395f, -0.375f, 0.395f, -0.916f)
                quadToRelative(0f, -0.584f, -0.395f, -0.959f)
                quadToRelative(-0.396f, -0.375f, -0.896f, -0.375f)
                horizontalLineTo(11.5f)
                quadToRelative(-0.583f, 0f, -0.958f, 0.396f)
                reflectiveQuadToRelative(-0.375f, 0.938f)
                quadToRelative(0f, 0.541f, 0.375f, 0.916f)
                reflectiveQuadToRelative(0.958f, 0.375f)
                close()
                moveToRelative(0f, -5.208f)
                horizontalLineToRelative(17.042f)
                quadToRelative(0.5f, 0f, 0.896f, -0.396f)
                quadToRelative(0.395f, -0.396f, 0.395f, -0.937f)
                quadToRelative(0f, -0.542f, -0.395f, -0.917f)
                quadToRelative(-0.396f, -0.375f, -0.896f, -0.375f)
                horizontalLineTo(11.5f)
                quadToRelative(-0.583f, 0f, -0.958f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                quadToRelative(0f, 0.583f, 0.375f, 0.958f)
                reflectiveQuadToRelative(0.958f, 0.375f)
                close()
                moveToRelative(22.667f, 21.292f)
                lineToRelative(-4.292f, -4.292f)
                horizontalLineTo(6.25f)
                quadToRelative(-1.042f, 0f, -1.833f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.833f)
                verticalLineTo(6.208f)
                quadToRelative(0f, -1.041f, 0.792f, -1.833f)
                quadToRelative(0.791f, -0.792f, 1.833f, -0.792f)
                horizontalLineToRelative(27.5f)
                quadToRelative(1.083f, 0f, 1.854f, 0.792f)
                quadToRelative(0.771f, 0.792f, 0.771f, 1.833f)
                verticalLineToRelative(26.917f)
                quadToRelative(0f, 0.833f, -0.792f, 1.187f)
                quadToRelative(-0.791f, 0.355f, -1.416f, -0.27f)
                close()
                moveTo(6.25f, 6.208f)
                verticalLineToRelative(20.917f)
                horizontalLineTo(31f)
                lineToRelative(2.75f, 2.75f)
                verticalLineTo(6.208f)
                horizontalLineTo(6.25f)
                close()
                moveToRelative(0f, 0f)
                verticalLineToRelative(23.667f)
                verticalLineTo(6.208f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberChat(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Chat",
            defaultWidth = 256.dp,
            defaultHeight = 256.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.1f, 11.2f)
                curveToRelative(-1.5f, 0.8f, -2.4f, 1.6f, -3.1f, 3.1f)
                lineToRelative(-1.1f, 2f)
                verticalLineTo(84f)
                curveToRelative(0f, 64.9f, 0f, 67.6f, 0.9f, 69.4f)
                curveToRelative(0.5f, 1.1f, 1.6f, 2.4f, 2.4f, 3f)
                curveToRelative(1.6f, 1.1f, 1.7f, 1.1f, 13.9f, 1.2f)
                lineToRelative(12.3f, 0.1f)
                verticalLineToRelative(18.3f)
                verticalLineToRelative(18.3f)
                lineTo(57.8f, 176f)
                lineToRelative(18.3f, -18.3f)
                horizontalLineTo(139f)
                horizontalLineToRelative(62.8f)
                lineToRelative(-0.3f, -70.4f)
                curveToRelative(-0.1f, -38.7f, -0.4f, -71f, -0.5f, -71.8f)
                curveToRelative(-0.2f, -1.6f, -2.5f, -4f, -4.5f, -4.7f)
                curveToRelative(-0.9f, -0.4f, -28.8f, -0.5f, -90.9f, -0.5f)
                horizontalLineTo(16.2f)
                lineTo(14.1f, 11.2f)
                close()
                moveTo(164.8f, 61.7f)
                lineToRelative(0.1f, 7.5f)
                horizontalLineToRelative(-59f)
                horizontalLineToRelative(-59f)
                verticalLineToRelative(-7.6f)
                verticalLineTo(54f)
                lineToRelative(58.9f, 0.1f)
                lineToRelative(58.9f, 0.1f)
                lineTo(164.8f, 61.7f)
                close()
                moveTo(164.4f, 106.1f)
                verticalLineToRelative(7.4f)
                horizontalLineToRelative(-58.8f)
                horizontalLineTo(46.9f)
                verticalLineToRelative(-7.4f)
                verticalLineToRelative(-7.4f)
                horizontalLineToRelative(58.8f)
                horizontalLineToRelative(58.8f)
                lineTo(164.4f, 106.1f)
                lineTo(164.4f, 106.1f)
                close()
            }
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(216.5f, 135.6f)
                verticalLineToRelative(36.9f)
                horizontalLineToRelative(-62.8f)
                horizontalLineTo(90.8f)
                lineToRelative(0.2f, 19.2f)
                curveToRelative(0.2f, 21.2f, 0.2f, 21.3f, 3f, 23.3f)
                curveToRelative(1.4f, 1f, 1.7f, 1f, 23.1f, 1.3f)
                curveToRelative(11.9f, 0.2f, 33.8f, 0.3f, 48.6f, 0.4f)
                lineToRelative(27f, 0f)
                lineToRelative(14.5f, 14.5f)
                curveToRelative(8f, 8f, 14.7f, 14.5f, 14.8f, 14.5f)
                curveToRelative(0.2f, 0f, 0.4f, -6.5f, 0.4f, -14.5f)
                verticalLineToRelative(-14.5f)
                horizontalLineToRelative(8.7f)
                curveToRelative(8.1f, 0f, 8.8f, -0.1f, 10.7f, -1.1f)
                curveToRelative(1.5f, -0.7f, 2.4f, -1.6f, 3.1f, -3.1f)
                lineToRelative(1f, -2f)
                verticalLineToRelative(-52.8f)
                verticalLineToRelative(-52.8f)
                lineToRelative(-1f, -2f)
                curveToRelative(-2f, -3.9f, -2f, -3.9f, -16.1f, -4.1f)
                lineToRelative(-12.4f, -0.1f)
                lineTo(216.5f, 135.6f)
                lineTo(216.5f, 135.6f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberTaxi(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "CarTaxiFront",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10f, 2f)
                horizontalLineToRelative(4f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(21f, 8f)
                lineToRelative(-2f, 2f)
                lineToRelative(-1.5f, -3.7f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15.646f, 5f)
                horizontalLineTo(8.4f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.903f, 1.257f)
                lineTo(5f, 10f)
                lineTo(3f, 8f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(7f, 14f)
                horizontalLineToRelative(0.01f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(17f, 14f)
                horizontalLineToRelative(0.01f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5f, 10f)
                horizontalLineTo(19f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 21f, 12f)
                verticalLineTo(16f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 18f)
                horizontalLineTo(5f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 16f)
                verticalLineTo(12f)
                arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 10f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5f, 18f)
                verticalLineToRelative(2f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 18f)
                verticalLineToRelative(2f)
            }
        }.build()
    }
}

@Composable
fun rememberProfile(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Profile",
            defaultWidth = 256.dp,
            defaultHeight = 256.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(246f, 231.2f)
                lineTo(246f, 231.2f)
                curveToRelative(0f, 6.1f, -5f, 11.1f, -11.1f, 11.1f)
                horizontalLineTo(21.1f)
                curveToRelative(-6.1f, 0f, -11.1f, -5f, -11.1f, -11.1f)
                verticalLineToRelative(0f)
                lineToRelative(0f, 0f)
                curveToRelative(0f, 0f, 0f, -40.3f, 29.5f, -55f)
                curveToRelative(18.6f, -9.3f, 11.5f, -1.7f, 34.3f, -11.2f)
                curveToRelative(22.9f, -9.5f, 28.3f, -12.8f, 28.3f, -12.8f)
                lineToRelative(0.2f, -21.8f)
                curveToRelative(0f, 0f, -8.6f, -6.6f, -11.2f, -27.1f)
                curveTo(85.7f, 104.8f, 84f, 97f, 83.6f, 92f)
                curveToRelative(-0.3f, -4.8f, -3.1f, -19.9f, 3.4f, -18.5f)
                curveToRelative(-1.3f, -10.1f, -2.3f, -19.1f, -1.8f, -23.9f)
                curveToRelative(1.6f, -16.9f, 17.9f, -34.5f, 42.9f, -35.8f)
                curveToRelative(29.4f, 1.3f, 41.1f, 18.9f, 42.7f, 35.8f)
                curveToRelative(0.5f, 4.8f, -0.6f, 13.9f, -1.9f, 23.9f)
                curveToRelative(6.6f, -1.3f, 3.7f, 13.7f, 3.4f, 18.5f)
                curveToRelative(-0.3f, 5f, -2.1f, 12.8f, -7.4f, 11.3f)
                curveToRelative(-2.7f, 20.5f, -11.2f, 27f, -11.2f, 27f)
                lineToRelative(0.2f, 21.7f)
                curveToRelative(0f, 0f, 5.4f, 3.1f, 28.3f, 12.6f)
                curveToRelative(22.9f, 9.5f, 15.7f, 2.4f, 34.3f, 11.7f)
                curveTo(246f, 190.9f, 246f, 231.2f, 246f, 231.2f)
                lineTo(246f, 231.2f)
                lineTo(246f, 231.2f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberDoneAll(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "done_all",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12.083f, 29.25f)
                quadToRelative(-0.25f, 0f, -0.458f, -0.083f)
                quadToRelative(-0.208f, -0.084f, -0.417f, -0.292f)
                lineToRelative(-7.333f, -7.333f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.959f)
                quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                quadToRelative(0.417f, -0.417f, 0.937f, -0.396f)
                quadToRelative(0.521f, 0.021f, 0.938f, 0.396f)
                lineToRelative(6.375f, 6.417f)
                lineTo(14f, 27.917f)
                lineToRelative(-0.958f, 0.916f)
                quadToRelative(-0.209f, 0.25f, -0.438f, 0.334f)
                quadToRelative(-0.229f, 0.083f, -0.521f, 0.083f)
                close()
                moveToRelative(7.5f, 0f)
                quadToRelative(-0.25f, 0f, -0.479f, -0.083f)
                quadToRelative(-0.229f, -0.084f, -0.437f, -0.292f)
                lineToRelative(-7.334f, -7.333f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.959f)
                quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                quadToRelative(0.417f, -0.375f, 0.938f, -0.375f)
                quadToRelative(0.521f, 0f, 0.896f, 0.375f)
                lineToRelative(6.416f, 6.417f)
                lineToRelative(14.709f, -14.75f)
                quadToRelative(0.375f, -0.375f, 0.937f, -0.375f)
                quadToRelative(0.563f, 0f, 0.938f, 0.375f)
                quadToRelative(0.416f, 0.416f, 0.416f, 0.958f)
                reflectiveQuadToRelative(-0.416f, 0.917f)
                lineTo(20.5f, 28.875f)
                quadToRelative(-0.208f, 0.208f, -0.438f, 0.292f)
                quadToRelative(-0.229f, 0.083f, -0.479f, 0.083f)
                close()
                moveToRelative(0f, -6.917f)
                lineToRelative(-1.875f, -1.875f)
                lineToRelative(9.125f, -9.166f)
                quadToRelative(0.375f, -0.375f, 0.938f, -0.375f)
                quadToRelative(0.562f, 0f, 0.937f, 0.416f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.938f)
                quadToRelative(0f, 0.562f, -0.375f, 0.937f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberFire(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Fire",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 16f)
                curveToRelative(3.314f, 0f, 6f, -2f, 6f, -5.5f)
                curveToRelative(0f, -1.5f, -0.5f, -4f, -2.5f, -6f)
                curveToRelative(0.25f, 1.5f, -1.25f, 2f, -1.25f, 2f)
                curveTo(11f, 4f, 9f, 0.5f, 6f, 0f)
                curveToRelative(0.357f, 2f, 0.5f, 4f, -2f, 6f)
                curveToRelative(-1.25f, 1f, -2f, 2.729f, -2f, 4.5f)
                curveTo(2f, 14f, 4.686f, 16f, 8f, 16f)
                moveToRelative(0f, -1f)
                curveToRelative(-1.657f, 0f, -3f, -1f, -3f, -2.75f)
                curveToRelative(0f, -0.75f, 0.25f, -2f, 1.25f, -3f)
                curveTo(6.125f, 10f, 7f, 10.5f, 7f, 10.5f)
                curveToRelative(-0.375f, -1.25f, 0.5f, -3.25f, 2f, -3.5f)
                curveToRelative(-0.179f, 1f, -0.25f, 2f, 1f, 3f)
                curveToRelative(0.625f, 0.5f, 1f, 1.364f, 1f, 2.25f)
                curveTo(11f, 14f, 9.657f, 15f, 8f, 15f)
            }
        }.build()
    }
}

@Composable
fun rememberSleepMoon(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Sleep",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960F,
            viewportHeight = 960F
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(483f, 976f)
                quadToRelative(-84f, 0f, -157.5f, -32f)
                reflectiveQuadToRelative(-128f, -86.5f)
                reflectiveQuadToRelative(-86.5f, -128f)
                reflectiveQuadTo(79f, 572f)
                quadToRelative(0f, -146f, 93f, -257.5f)
                reflectiveQuadTo(409f, 176f)
                quadToRelative(-18f, 99f, 11f, 193.5f)
                reflectiveQuadTo(520f, 535f)
                reflectiveQuadToRelative(165.5f, 100f)
                reflectiveQuadTo(879f, 646f)
                quadToRelative(-26f, 144f, -138f, 237f)
                reflectiveQuadToRelative(-258f, 93f)
                moveToRelative(0f, -80f)
                quadToRelative(88f, 0f, 163f, -44f)
                reflectiveQuadToRelative(118f, -121f)
                quadToRelative(-86f, -8f, -163f, -43.5f)
                reflectiveQuadTo(463f, 591f)
                reflectiveQuadToRelative(-97f, -138f)
                reflectiveQuadToRelative(-43f, -163f)
                quadToRelative(-77f, 43f, -120.5f, 118.5f)
                reflectiveQuadTo(159f, 572f)
                quadToRelative(0f, 135f, 94.5f, 229.5f)
                reflectiveQuadTo(483f, 896f)
                moveToRelative(237f, -400f)
                lineToRelative(-50f, -110f)
                lineToRelative(-110f, -50f)
                lineToRelative(110f, -50f)
                lineToRelative(50f, -110f)
                lineToRelative(50f, 110f)
                lineToRelative(110f, 50f)
                lineToRelative(-110f, 50f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberHeart(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Ecg_heart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 840f)
                quadToRelative(-18f, 0f, -34.5f, -6.5f)
                reflectiveQuadTo(416f, 814f)
                lineTo(148f, 545f)
                quadToRelative(-35f, -35f, -51.5f, -80f)
                reflectiveQuadTo(80f, 371f)
                quadToRelative(0f, -103f, 67f, -177f)
                reflectiveQuadToRelative(167f, -74f)
                quadToRelative(48f, 0f, 90.5f, 19f)
                reflectiveQuadToRelative(75.5f, 53f)
                quadToRelative(32f, -34f, 74.5f, -53f)
                reflectiveQuadToRelative(90.5f, -19f)
                quadToRelative(100f, 0f, 167.5f, 74f)
                reflectiveQuadTo(880f, 370f)
                quadToRelative(0f, 49f, -17f, 94f)
                reflectiveQuadToRelative(-51f, 80f)
                lineTo(543f, 814f)
                quadToRelative(-13f, 13f, -29f, 19.5f)
                reflectiveQuadToRelative(-34f, 6.5f)
                moveToRelative(40f, -520f)
                quadToRelative(10f, 0f, 19f, 5f)
                reflectiveQuadToRelative(14f, 13f)
                lineToRelative(68f, 102f)
                horizontalLineToRelative(166f)
                quadToRelative(7f, -17f, 10.5f, -34.5f)
                reflectiveQuadTo(801f, 370f)
                quadToRelative(-2f, -69f, -46f, -118.5f)
                reflectiveQuadTo(645f, 202f)
                quadToRelative(-31f, 0f, -59.5f, 12f)
                reflectiveQuadTo(536f, 249f)
                lineToRelative(-27f, 29f)
                quadToRelative(-5f, 6f, -13f, 9.5f)
                reflectiveQuadToRelative(-16f, 3.5f)
                reflectiveQuadToRelative(-16f, -3.5f)
                reflectiveQuadToRelative(-14f, -9.5f)
                lineToRelative(-27f, -29f)
                quadToRelative(-21f, -23f, -49f, -36f)
                reflectiveQuadToRelative(-60f, -13f)
                quadToRelative(-66f, 0f, -110f, 50.5f)
                reflectiveQuadTo(160f, 370f)
                quadToRelative(0f, 18f, 3f, 35.5f)
                reflectiveQuadToRelative(10f, 34.5f)
                horizontalLineToRelative(187f)
                quadToRelative(10f, 0f, 19f, 5f)
                reflectiveQuadToRelative(14f, 13f)
                lineToRelative(35f, 52f)
                lineToRelative(54f, -162f)
                quadToRelative(4f, -12f, 14.5f, -20f)
                reflectiveQuadToRelative(23.5f, -8f)
                moveToRelative(12f, 130f)
                lineToRelative(-54f, 162f)
                quadToRelative(-4f, 12f, -15f, 20f)
                reflectiveQuadToRelative(-24f, 8f)
                quadToRelative(-10f, 0f, -19f, -5f)
                reflectiveQuadToRelative(-14f, -13f)
                lineToRelative(-68f, -102f)
                horizontalLineTo(236f)
                lineToRelative(237f, 237f)
                quadToRelative(2f, 2f, 3.5f, 2.5f)
                reflectiveQuadToRelative(3.5f, 0.5f)
                reflectiveQuadToRelative(3.5f, -0.5f)
                reflectiveQuadToRelative(3.5f, -2.5f)
                lineToRelative(236f, -237f)
                horizontalLineTo(600f)
                quadToRelative(-10f, 0f, -19f, -5f)
                reflectiveQuadToRelative(-15f, -13f)
                close()
            }
        }.build()
    }
}


@Composable
fun rememberSteps(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Steps",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(216f, 380f)
                quadToRelative(39f, 0f, 74f, 14f)
                reflectiveQuadToRelative(64f, 41f)
                lineToRelative(382f, 365f)
                horizontalLineToRelative(24f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(800f, 760f)
                quadToRelative(0f, -8f, -1.5f, -17f)
                reflectiveQuadTo(788f, 725f)
                lineTo(605f, 542f)
                lineToRelative(-71f, -214f)
                lineToRelative(-74f, 18f)
                quadToRelative(-38f, 10f, -69f, -14f)
                reflectiveQuadToRelative(-31f, -63f)
                verticalLineToRelative(-84f)
                lineToRelative(-28f, -14f)
                lineToRelative(-154f, 206f)
                quadToRelative(-1f, 1f, -1f, 1.5f)
                reflectiveQuadToRelative(-1f, 1.5f)
                close()
                moveToRelative(0f, 80f)
                horizontalLineToRelative(-46f)
                quadToRelative(3f, 7f, 7.5f, 13f)
                reflectiveQuadToRelative(10.5f, 11f)
                lineToRelative(324f, 295f)
                quadToRelative(11f, 11f, 25f, 16f)
                reflectiveQuadToRelative(29f, 5f)
                horizontalLineToRelative(54f)
                lineTo(299f, 493f)
                quadToRelative(-17f, -17f, -38.5f, -25f)
                reflectiveQuadToRelative(-44.5f, -8f)
                moveTo(566f, 880f)
                quadToRelative(-30f, 0f, -57f, -11f)
                reflectiveQuadToRelative(-50f, -31f)
                lineTo(134f, 543f)
                quadToRelative(-46f, -42f, -51.5f, -103f)
                reflectiveQuadTo(114f, 329f)
                lineToRelative(154f, -206f)
                quadToRelative(17f, -23f, 45.5f, -30.5f)
                reflectiveQuadTo(368f, 99f)
                lineToRelative(28f, 14f)
                quadToRelative(21f, 11f, 32.5f, 30f)
                reflectiveQuadToRelative(11.5f, 42f)
                verticalLineToRelative(84f)
                lineToRelative(74f, -19f)
                quadToRelative(30f, -8f, 58f, 7.5f)
                reflectiveQuadToRelative(38f, 44.5f)
                lineToRelative(65f, 196f)
                lineToRelative(170f, 170f)
                quadToRelative(20f, 20f, 27.5f, 43f)
                reflectiveQuadToRelative(7.5f, 49f)
                quadToRelative(0f, 50f, -35f, 85f)
                reflectiveQuadToRelative(-85f, 35f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberMetabolic(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Body_system",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(316f, 720f)
                lineToRelative(76f, -364f)
                lineToRelative(-72f, 28f)
                verticalLineToRelative(96f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-148f)
                lineToRelative(202f, -85f)
                quadToRelative(14f, -6f, 29.5f, -7.5f)
                reflectiveQuadTo(501f, 243f)
                reflectiveQuadToRelative(26.5f, 14f)
                reflectiveQuadToRelative(20.5f, 23f)
                lineToRelative(40f, 64f)
                quadToRelative(28f, 45f, 73.5f, 70.5f)
                reflectiveQuadTo(760f, 440f)
                verticalLineToRelative(80f)
                quadToRelative(-70f, 0f, -125.5f, -28f)
                reflectiveQuadTo(540f, 420f)
                lineToRelative(-24f, 60f)
                lineToRelative(84f, 80f)
                verticalLineToRelative(160f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-122f)
                lineToRelative(-78f, -72f)
                lineToRelative(-42f, 194f)
                close()
                moveToRelative(224f, -500f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(460f, 140f)
                reflectiveQuadToRelative(23.5f, -56.5f)
                reflectiveQuadTo(540f, 60f)
                reflectiveQuadToRelative(56.5f, 23.5f)
                reflectiveQuadTo(620f, 140f)
                reflectiveQuadToRelative(-23.5f, 56.5f)
                reflectiveQuadTo(540f, 220f)
                moveTo(480f, 880f)
                quadToRelative(-83f, 0f, -156f, -31.5f)
                reflectiveQuadTo(197f, 763f)
                reflectiveQuadToRelative(-85.5f, -127f)
                reflectiveQuadTo(80f, 480f)
                quadToRelative(0f, -119f, 61.5f, -214f)
                reflectiveQuadTo(302f, 122f)
                lineToRelative(36f, 71f)
                quadToRelative(-79f, 39f, -128.5f, 115.5f)
                reflectiveQuadTo(160f, 480f)
                quadToRelative(0f, 134f, 93f, 227f)
                reflectiveQuadToRelative(227f, 93f)
                reflectiveQuadToRelative(227f, -93f)
                reflectiveQuadToRelative(93f, -227f)
                horizontalLineToRelative(80f)
                quadToRelative(0f, 83f, -31.5f, 156f)
                reflectiveQuadTo(763f, 763f)
                reflectiveQuadToRelative(-127f, 85.5f)
                reflectiveQuadTo(480f, 880f)
            }
        }.build()
    }
}

@Composable
fun rememberDistance(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Distance",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 880f)
                quadToRelative(-106f, 0f, -173f, -33.5f)
                reflectiveQuadTo(240f, 760f)
                quadToRelative(0f, -35f, 29f, -61f)
                reflectiveQuadToRelative(80f, -41f)
                lineToRelative(23f, 76f)
                quadToRelative(-17f, 5f, -31f, 12.5f)
                reflectiveQuadTo(322f, 760f)
                quadToRelative(13f, 16f, 60f, 28f)
                reflectiveQuadToRelative(98f, 12f)
                reflectiveQuadToRelative(98.5f, -12f)
                reflectiveQuadToRelative(60.5f, -28f)
                quadToRelative(-5f, -6f, -19f, -13.5f)
                reflectiveQuadTo(589f, 734f)
                lineToRelative(23f, -76f)
                quadToRelative(51f, 15f, 79.5f, 41f)
                reflectiveQuadToRelative(28.5f, 61f)
                quadToRelative(0f, 53f, -67f, 86.5f)
                reflectiveQuadTo(480f, 880f)
                moveToRelative(0f, -267f)
                quadToRelative(18f, -33f, 38f, -60.5f)
                reflectiveQuadToRelative(39f, -52.5f)
                quadToRelative(37f, -48f, 59f, -86.5f)
                reflectiveQuadToRelative(22f, -95.5f)
                quadToRelative(0f, -66f, -46f, -112f)
                reflectiveQuadToRelative(-112f, -46f)
                reflectiveQuadToRelative(-112f, 46f)
                reflectiveQuadToRelative(-46f, 112f)
                quadToRelative(0f, 57f, 22f, 95.5f)
                reflectiveQuadToRelative(59f, 86.5f)
                quadToRelative(19f, 25f, 39f, 52.5f)
                reflectiveQuadToRelative(38f, 60.5f)
                moveToRelative(0f, 147f)
                quadToRelative(-11f, 0f, -20f, -6.5f)
                reflectiveQuadTo(447f, 736f)
                quadToRelative(-23f, -71f, -58f, -119f)
                reflectiveQuadToRelative(-68f, -92f)
                quadToRelative(-32f, -44f, -55.5f, -91f)
                reflectiveQuadTo(242f, 318f)
                quadToRelative(0f, -100f, 69f, -169f)
                reflectiveQuadToRelative(169f, -69f)
                reflectiveQuadToRelative(169f, 69f)
                reflectiveQuadToRelative(69f, 169f)
                quadToRelative(0f, 69f, -23f, 116f)
                reflectiveQuadToRelative(-56f, 91f)
                quadToRelative(-32f, 44f, -67.5f, 92f)
                reflectiveQuadTo(513f, 736f)
                quadToRelative(-4f, 11f, -13f, 17.5f)
                reflectiveQuadToRelative(-20f, 6.5f)
                moveToRelative(0f, -357f)
                quadToRelative(35f, 0f, 60f, -25f)
                reflectiveQuadToRelative(25f, -60f)
                reflectiveQuadToRelative(-25f, -60f)
                reflectiveQuadToRelative(-60f, -25f)
                reflectiveQuadToRelative(-60f, 25f)
                reflectiveQuadToRelative(-25f, 60f)
                reflectiveQuadToRelative(25f, 60f)
                reflectiveQuadToRelative(60f, 25f)
                moveToRelative(0f, -85f)
            }
        }.build()
    }
}


@Composable
fun rememberDumbbell(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Dumbbell",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.4f, 14.4f)
                lineTo(9.6f, 9.6f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18.657f, 21.485f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.829f, -2.828f)
                lineToRelative(-1.767f, 1.768f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.829f, -2.829f)
                lineToRelative(6.364f, -6.364f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.829f, 2.829f)
                lineToRelative(-1.768f, 1.767f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.828f, 2.829f)
                close()
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(21.5f, 21.5f)
                lineToRelative(-1.4f, -1.4f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.9f, 3.9f)
                lineTo(2.5f, 2.5f)
            }
            path(
                fill = null,
                fillAlpha = 1.0f,
                stroke = SolidColor(color),
                strokeAlpha = 1.0f,
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6.404f, 12.768f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.829f, -2.829f)
                lineToRelative(1.768f, -1.767f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.828f, -2.829f)
                lineToRelative(2.828f, -2.828f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.829f, 2.828f)
                lineToRelative(1.767f, -1.768f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.829f, 2.829f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberTimer(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Timer",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(360f, 120f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(80f, 440f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(-240f)
                horizontalLineToRelative(-80f)
                close()
                moveToRelative(40f, 320f)
                quadToRelative(-74f, 0f, -139.5f, -28.5f)
                reflectiveQuadTo(226f, 774f)
                reflectiveQuadToRelative(-77.5f, -114.5f)
                reflectiveQuadTo(120f, 520f)
                reflectiveQuadToRelative(28.5f, -139.5f)
                reflectiveQuadTo(226f, 266f)
                reflectiveQuadToRelative(114.5f, -77.5f)
                reflectiveQuadTo(480f, 160f)
                quadToRelative(62f, 0f, 119f, 20f)
                reflectiveQuadToRelative(107f, 58f)
                lineToRelative(56f, -56f)
                lineToRelative(56f, 56f)
                lineToRelative(-56f, 56f)
                quadToRelative(38f, 50f, 58f, 107f)
                reflectiveQuadToRelative(20f, 119f)
                quadToRelative(0f, 74f, -28.5f, 139.5f)
                reflectiveQuadTo(734f, 774f)
                reflectiveQuadToRelative(-114.5f, 77.5f)
                reflectiveQuadTo(480f, 880f)
                moveToRelative(0f, -80f)
                quadToRelative(116f, 0f, 198f, -82f)
                reflectiveQuadToRelative(82f, -198f)
                reflectiveQuadToRelative(-82f, -198f)
                reflectiveQuadToRelative(-198f, -82f)
                reflectiveQuadToRelative(-198f, 82f)
                reflectiveQuadToRelative(-82f, 198f)
                reflectiveQuadToRelative(82f, 198f)
                reflectiveQuadToRelative(198f, 82f)
                moveToRelative(0f, -280f)
            }
        }.build()
    }
}

@Composable
fun rememberFile(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Attach_file",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(720f, 630f)
                quadToRelative(0f, 104f, -73f, 177f)
                reflectiveQuadTo(470f, 880f)
                reflectiveQuadToRelative(-177f, -73f)
                reflectiveQuadToRelative(-73f, -177f)
                verticalLineToRelative(-370f)
                quadToRelative(0f, -75f, 52.5f, -127.5f)
                reflectiveQuadTo(400f, 80f)
                reflectiveQuadToRelative(127.5f, 52.5f)
                reflectiveQuadTo(580f, 260f)
                verticalLineToRelative(350f)
                quadToRelative(0f, 46f, -32f, 78f)
                reflectiveQuadToRelative(-78f, 32f)
                reflectiveQuadToRelative(-78f, -32f)
                reflectiveQuadToRelative(-32f, -78f)
                verticalLineToRelative(-370f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(370f)
                quadToRelative(0f, 13f, 8.5f, 21.5f)
                reflectiveQuadTo(470f, 640f)
                reflectiveQuadToRelative(21.5f, -8.5f)
                reflectiveQuadTo(500f, 610f)
                verticalLineToRelative(-350f)
                quadToRelative(-1f, -42f, -29.5f, -71f)
                reflectiveQuadTo(400f, 160f)
                reflectiveQuadToRelative(-71f, 29f)
                reflectiveQuadToRelative(-29f, 71f)
                verticalLineToRelative(370f)
                quadToRelative(-1f, 71f, 49f, 120.5f)
                reflectiveQuadTo(470f, 800f)
                quadToRelative(70f, 0f, 119f, -49.5f)
                reflectiveQuadTo(640f, 630f)
                verticalLineToRelative(-390f)
                horizontalLineToRelative(80f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberTriangleUp(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "TriangleUp",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14f, 10.44f)
                lineToRelative(-0.413f, 0.56f)
                horizontalLineTo(2.393f)
                lineTo(2f, 10.46f)
                lineTo(7.627f, 5f)
                horizontalLineToRelative(0.827f)
                lineTo(14f, 10.44f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberTriangleDown(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "TriangleDown",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(2f, 5.56f)
                lineTo(2.413f, 5f)
                horizontalLineToRelative(11.194f)
                lineToRelative(0.393f, 0.54f)
                lineTo(8.373f, 11f)
                horizontalLineToRelative(-0.827f)
                lineTo(2f, 5.56f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberCloudUpload(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Cloud_upload",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(260f, 800f)
                quadToRelative(-91f, 0f, -155.5f, -63f)
                reflectiveQuadTo(40f, 583f)
                quadToRelative(0f, -78f, 47f, -139f)
                reflectiveQuadToRelative(123f, -78f)
                quadToRelative(25f, -92f, 100f, -149f)
                reflectiveQuadToRelative(170f, -57f)
                quadToRelative(117f, 0f, 198.5f, 81.5f)
                reflectiveQuadTo(760f, 440f)
                quadToRelative(69f, 8f, 114.5f, 59.5f)
                reflectiveQuadTo(920f, 620f)
                quadToRelative(0f, 75f, -52.5f, 127.5f)
                reflectiveQuadTo(740f, 800f)
                horizontalLineTo(520f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(440f, 720f)
                verticalLineToRelative(-206f)
                lineToRelative(-64f, 62f)
                lineToRelative(-56f, -56f)
                lineToRelative(160f, -160f)
                lineToRelative(160f, 160f)
                lineToRelative(-56f, 56f)
                lineToRelative(-64f, -62f)
                verticalLineToRelative(206f)
                horizontalLineToRelative(220f)
                quadToRelative(42f, 0f, 71f, -29f)
                reflectiveQuadToRelative(29f, -71f)
                reflectiveQuadToRelative(-29f, -71f)
                reflectiveQuadToRelative(-71f, -29f)
                horizontalLineToRelative(-60f)
                verticalLineToRelative(-80f)
                quadToRelative(0f, -83f, -58.5f, -141.5f)
                reflectiveQuadTo(480f, 240f)
                reflectiveQuadToRelative(-141.5f, 58.5f)
                reflectiveQuadTo(280f, 440f)
                horizontalLineToRelative(-20f)
                quadToRelative(-58f, 0f, -99f, 41f)
                reflectiveQuadToRelative(-41f, 99f)
                reflectiveQuadToRelative(41f, 99f)
                reflectiveQuadToRelative(99f, 41f)
                horizontalLineToRelative(100f)
                verticalLineToRelative(80f)
                close()
                moveToRelative(220f, -280f)
            }
        }.build()
    }
}

@Composable
fun rememberNext(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Navigate_next",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(504f, 480f)
                lineTo(320f, 296f)
                lineToRelative(56f, -56f)
                lineToRelative(240f, 240f)
                lineToRelative(-240f, 240f)
                lineToRelative(-56f, -56f)
                close()
            }
        }.build()
    }
}


@Composable
fun rememberPrevious(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Navigate_before",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(560f, 720f)
                lineTo(320f, 480f)
                lineToRelative(240f, -240f)
                lineToRelative(56f, 56f)
                lineToRelative(-184f, 184f)
                lineToRelative(184f, 184f)
                close()
            }
        }.build()
    }
}


@Composable
fun rememberReplace(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "Replay",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 880f)
                quadToRelative(-75f, 0f, -140.5f, -28.5f)
                reflectiveQuadToRelative(-114f, -77f)
                reflectiveQuadToRelative(-77f, -114f)
                reflectiveQuadTo(120f, 520f)
                horizontalLineToRelative(80f)
                quadToRelative(0f, 117f, 81.5f, 198.5f)
                reflectiveQuadTo(480f, 800f)
                reflectiveQuadToRelative(198.5f, -81.5f)
                reflectiveQuadTo(760f, 520f)
                reflectiveQuadToRelative(-81.5f, -198.5f)
                reflectiveQuadTo(480f, 240f)
                horizontalLineToRelative(-6f)
                lineToRelative(62f, 62f)
                lineToRelative(-56f, 58f)
                lineToRelative(-160f, -160f)
                lineToRelative(160f, -160f)
                lineToRelative(56f, 58f)
                lineToRelative(-62f, 62f)
                horizontalLineToRelative(6f)
                quadToRelative(75f, 0f, 140.5f, 28.5f)
                reflectiveQuadToRelative(114f, 77f)
                reflectiveQuadToRelative(77f, 114f)
                reflectiveQuadTo(840f, 520f)
                reflectiveQuadToRelative(-28.5f, 140.5f)
                reflectiveQuadToRelative(-77f, 114f)
                reflectiveQuadToRelative(-114f, 77f)
                reflectiveQuadTo(480f, 880f)
            }
        }.build()
    }
}
