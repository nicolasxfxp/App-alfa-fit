package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonGreen
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceDark

data class MapPoint(val xRatio: Float, val yRatio: Float)

@Composable
fun DarkGpsMapCanvas(
    routePoints: List<MapPoint>,
    gpsStatusText: String,
    isTracking: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(AlfaDarkNavy)
            .border(1.dp, AlfaBorder, RoundedCornerShape(20.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw tactical grid background lines
            val step = 40.dp.toPx()
            for (x in 0..(width / step).toInt()) {
                drawLine(
                    color = Color(0x15FFFFFF),
                    start = Offset(x * step, 0f),
                    end = Offset(x * step, height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..(height / step).toInt()) {
                drawLine(
                    color = Color(0x15FFFFFF),
                    start = Offset(0f, y * step),
                    end = Offset(width, y * step),
                    strokeWidth = 1f
                )
            }

            // 2. Draw mock city streets / dark contours
            drawLine(
                color = Color(0x22FFFFFF),
                start = Offset(0f, height * 0.35f),
                end = Offset(width, height * 0.45f),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0x1AFFFFFF),
                start = Offset(width * 0.25f, 0f),
                end = Offset(width * 0.35f, height),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0x1AFFFFFF),
                start = Offset(width * 0.75f, 0f),
                end = Offset(width * 0.65f, height),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )

            // 3. Draw GPS Polyline Track
            if (routePoints.isNotEmpty()) {
                val path = Path()
                routePoints.forEachIndexed { index, pt ->
                    val px = pt.xRatio * width
                    val py = pt.yRatio * height
                    if (index == 0) {
                        path.moveTo(px, py)
                    } else {
                        path.lineTo(px, py)
                    }
                }

                // Glowing outer stroke
                drawPath(
                    path = path,
                    color = AlfaNeonLimeGlow,
                    style = Stroke(
                        width = 10.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Crisp inner Neon Lime stroke
                drawPath(
                    path = path,
                    color = AlfaNeonLime,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                // Start point dot
                val startPx = routePoints.first().xRatio * width
                val startPy = routePoints.first().yRatio * height
                drawCircle(
                    color = Color.White,
                    radius = 5.dp.toPx(),
                    center = Offset(startPx, startPy)
                )

                // Current Runner Pin
                val currentPt = routePoints.last()
                val cx = currentPt.xRatio * width
                val cy = currentPt.yRatio * height

                if (isTracking) {
                    drawCircle(
                        color = AlfaNeonLime.copy(alpha = pulseAlpha),
                        radius = pulseRadius * 2.5f,
                        center = Offset(cx, cy)
                    )
                }

                drawCircle(
                    color = AlfaNeonLime,
                    radius = 8.dp.toPx(),
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = AlfaBlack,
                    radius = 3.dp.toPx(),
                    center = Offset(cx, cy)
                )
            }
        }

        // GPS Status Badge overlay
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AlfaBlack.copy(alpha = 0.85f))
                .border(1.dp, AlfaNeonLime.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = "📡 $gpsStatusText",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (gpsStatusText.contains("conectado", ignoreCase = true)) AlfaNeonLime else AlfaNeonGreen
            )
        }
    }
}
