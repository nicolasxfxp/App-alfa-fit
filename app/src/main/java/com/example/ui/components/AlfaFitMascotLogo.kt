package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite

@Composable
fun AlfaFitLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    textSize: TextUnit = 32.sp,
    showSlogan: Boolean = true,
    showMascotImage: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showMascotImage) {
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(AlfaNeonLimeGlow, AlfaDarkNavy, AlfaBlack)
                        )
                    )
                    .border(2.dp, AlfaNeonLime, RoundedCornerShape(24.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_alfa_pitbull_logo),
                    contentDescription = "Mascote ALFA FIT",
                    modifier = Modifier
                        .size(size - 8.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // "ALFA" in White + "FIT" in Neon Lime
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ALFA ",
                fontSize = textSize,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = AlfaTextWhite
            )
            Text(
                text = "FIT",
                fontSize = textSize,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = AlfaNeonLime
            )
        }

        if (showSlogan) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Seu treino. Seu ritmo. Sua evolução.",
                style = MaterialTheme.typography.bodyMedium,
                color = AlfaTextGray,
                fontSize = 13.sp
            )
        }
    }
}
