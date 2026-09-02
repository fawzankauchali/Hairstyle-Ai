package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.GeometryOverlayType
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBright
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun FacialGeometryOverlay(
    modifier: Modifier = Modifier,
    isScanning: Boolean = false,
    overlayType: GeometryOverlayType = GeometryOverlayType.ALL,
    faceShape: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanLineProgress by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLine"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)

        // 1. Central Vertical Symmetry Axis
        if (overlayType == GeometryOverlayType.ALL || overlayType == GeometryOverlayType.THIRDS) {
            drawLine(
                color = CyanAccent.copy(alpha = 0.55f),
                start = Offset(cx, h * 0.12f),
                end = Offset(cx, h * 0.88f),
                strokeWidth = 2f,
                pathEffect = dashedEffect
            )
        }

        // 2. Facial Thirds (Golden Ratio Lines)
        if (overlayType == GeometryOverlayType.ALL || overlayType == GeometryOverlayType.THIRDS) {
            val hairlineY = h * 0.28f
            val browY = h * 0.42f
            val noseTipY = h * 0.58f
            val chinY = h * 0.76f

            // Hairline Guide
            drawLine(
                color = GoldPrimary.copy(alpha = 0.7f),
                start = Offset(w * 0.22f, hairlineY),
                end = Offset(w * 0.78f, hairlineY),
                strokeWidth = 2.5f
            )

            // Brow line
            drawLine(
                color = CyanAccent.copy(alpha = 0.6f),
                start = Offset(w * 0.18f, browY),
                end = Offset(w * 0.82f, browY),
                strokeWidth = 2f,
                pathEffect = dashedEffect
            )

            // Nose line
            drawLine(
                color = CyanAccent.copy(alpha = 0.6f),
                start = Offset(w * 0.22f, noseTipY),
                end = Offset(w * 0.78f, noseTipY),
                strokeWidth = 2f,
                pathEffect = dashedEffect
            )

            // Chin line
            drawLine(
                color = GoldLight.copy(alpha = 0.7f),
                start = Offset(w * 0.28f, chinY),
                end = Offset(w * 0.72f, chinY),
                strokeWidth = 2.5f
            )
        }

        // 3. Cheekbone & Jawline Contours
        if (overlayType == GeometryOverlayType.ALL || overlayType == GeometryOverlayType.JAWLINE_AXIS) {
            val cheekY = h * 0.50f
            val cheekLeft = Offset(w * 0.18f, cheekY)
            val cheekRight = Offset(w * 0.82f, cheekY)

            // Cheekbone horizontal prominence axis
            drawLine(
                color = CyanBright.copy(alpha = 0.8f),
                start = cheekLeft,
                end = cheekRight,
                strokeWidth = 3f
            )

            // Cheek anchor points
            drawCircle(color = CyanBright, radius = 5f, center = cheekLeft)
            drawCircle(color = CyanBright, radius = 5f, center = cheekRight)

            // Jawline angle vectors
            val jawAngleY = h * 0.66f
            val jawLeft = Offset(w * 0.23f, jawAngleY)
            val jawRight = Offset(w * 0.77f, jawAngleY)
            val chinCenter = Offset(cx, h * 0.76f)

            val jawPath = Path().apply {
                moveTo(cheekLeft.x, cheekLeft.y)
                lineTo(jawLeft.x, jawLeft.y)
                lineTo(chinCenter.x, chinCenter.y)
                lineTo(jawRight.x, jawRight.y)
                lineTo(cheekRight.x, cheekRight.y)
            }
            drawPath(
                path = jawPath,
                color = GoldPrimary.copy(alpha = 0.75f),
                style = Stroke(width = 3.5f)
            )

            drawCircle(color = GoldPrimary, radius = 5f, center = jawLeft)
            drawCircle(color = GoldPrimary, radius = 5f, center = jawRight)
            drawCircle(color = GoldLight, radius = 6f, center = chinCenter)
        }

        // 4. Hairline Contour Arch
        if (overlayType == GeometryOverlayType.ALL || overlayType == GeometryOverlayType.HAIRLINE_GUIDE) {
            val hairlinePath = Path().apply {
                moveTo(w * 0.24f, h * 0.32f)
                quadraticTo(cx, h * 0.24f, w * 0.76f, h * 0.32f)
            }
            drawPath(
                path = hairlinePath,
                color = GoldLight.copy(alpha = 0.85f),
                style = Stroke(width = 3.5f)
            )
        }

        // 5. Head Alignment Oval Frame
        val ovalW = w * 0.68f
        val ovalH = h * 0.66f
        drawOval(
            color = if (isScanning) CyanBright.copy(alpha = pulseAlpha) else Color(0x334ECDC4),
            topLeft = Offset(cx - ovalW / 2f, cy - ovalH / 2f),
            size = Size(ovalW, ovalH),
            style = Stroke(width = if (isScanning) 3.5f else 2f)
        )

        // 6. Active Scanner Laser Beam (when analyzing)
        if (isScanning) {
            val currentScanY = h * scanLineProgress
            val laserGradient = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    CyanBright.copy(alpha = 0.35f),
                    CyanBright,
                    CyanBright.copy(alpha = 0.35f),
                    Color.Transparent
                ),
                startY = currentScanY - 24f,
                endY = currentScanY + 24f
            )

            drawRect(
                brush = laserGradient,
                topLeft = Offset(w * 0.1f, currentScanY - 24f),
                size = Size(w * 0.8f, 48f)
            )

            drawLine(
                color = Color.White,
                start = Offset(w * 0.12f, currentScanY),
                end = Offset(w * 0.88f, currentScanY),
                strokeWidth = 3f
            )
        }
    }
}
