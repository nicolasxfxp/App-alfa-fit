package com.example.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaBorderSubtle
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite
import com.example.ui.viewmodel.AlfaFitViewModel

@Composable
fun RunningScreen(
    viewModel: AlfaFitViewModel,
    onStartRun: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf("META") } // "META" or "LIVRE"
    var selectedKmTarget by remember { mutableStateOf(5.0f) }

    val presetGoals = listOf(1.0f, 2.0f, 3.0f, 5.0f, 10.0f, 15.0f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // HERO HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_running_hero),
                contentDescription = "Corrida",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AlfaBlack.copy(alpha = 0.4f),
                                AlfaBlack.copy(alpha = 0.85f),
                                AlfaBlack
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Corrida & Pace GPS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaTextWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Escolha seu modo e supere seu recorde de ritmo",
                    fontSize = 13.sp,
                    color = AlfaTextGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // MODE SELECTION
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Modo de Corrida",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AlfaTextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mode 1: Por Meta
                ModeCard(
                    title = "Modo 1",
                    subtitle = "Por Meta",
                    description = "Defina uma distância e receba aviso ao atingir",
                    icon = Icons.Default.Flag,
                    isSelected = selectedMode == "META",
                    onClick = { selectedMode = "META" },
                    modifier = Modifier.weight(1f),
                    testTag = "mode_goal_card"
                )

                // Mode 2: Modo Livre
                ModeCard(
                    title = "Modo 2",
                    subtitle = "Modo Livre",
                    description = "Corra livremente com monitoramento em tempo real",
                    icon = Icons.Default.Timeline,
                    isSelected = selectedMode == "LIVRE",
                    onClick = { selectedMode = "LIVRE" },
                    modifier = Modifier.weight(1f),
                    testTag = "mode_free_card"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TARGET DISTANCE OPTIONS IF META MODE
            if (selectedMode == "META") {
                Text(
                    text = "Defina sua Meta de Distância",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlfaTextWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetGoals.take(3).forEach { km ->
                        TargetKmChip(
                            km = km,
                            isSelected = selectedKmTarget == km,
                            onClick = { selectedKmTarget = km },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetGoals.drop(3).forEach { km ->
                        TargetKmChip(
                            km = km,
                            isSelected = selectedKmTarget == km,
                            onClick = { selectedKmTarget = km },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // BIG START BUTTON
            AlfaPrimaryButton(
                text = "INICIAR CORRIDA",
                icon = Icons.Default.DirectionsRun,
                onClick = {
                    viewModel.configureRun(selectedMode, selectedKmTarget)
                    viewModel.startRun()
                    onStartRun()
                },
                testTag = "start_run_button"
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun ModeCard(
    title: String,
    subtitle: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AlfaSurfaceCard else AlfaSurfaceDark
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 2.dp else 1.dp,
            if (isSelected) AlfaNeonLime else AlfaBorderSubtle
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) AlfaNeonLime else AlfaSurfaceDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) AlfaBlack else AlfaTextGray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = subtitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = if (isSelected) AlfaNeonLime else AlfaTextWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 11.sp,
                color = AlfaTextGray,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun TargetKmChip(
    km: Float,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) AlfaNeonLime else AlfaSurfaceCard)
            .border(
                1.dp,
                if (isSelected) AlfaNeonLime else AlfaBorderSubtle,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${km.toInt()} km",
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            color = if (isSelected) AlfaBlack else AlfaTextWhite
        )
    }
}
