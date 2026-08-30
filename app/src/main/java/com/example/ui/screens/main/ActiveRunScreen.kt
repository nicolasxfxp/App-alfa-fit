package com.example.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaSecondaryButton
import com.example.ui.components.DarkGpsMapCanvas
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaBorderSubtle
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonGreen
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaSurfaceElevated
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite
import com.example.ui.viewmodel.AlfaFitViewModel

@Composable
fun ActiveRunScreen(
    viewModel: AlfaFitViewModel,
    onFinishRun: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mode by viewModel.runMode.collectAsState()
    val targetKm by viewModel.targetDistanceKm.collectAsState()
    val isPaused by viewModel.runIsPaused.collectAsState()
    val distanceKm by viewModel.runDistanceKm.collectAsState()
    val durationSeconds by viewModel.runDurationSeconds.collectAsState()
    val avgPace by viewModel.runAvgPace.collectAsState()
    val calories by viewModel.runCaloriesKcal.collectAsState()
    val gpsStatus by viewModel.gpsStatus.collectAsState()
    val routePoints by viewModel.routePoints.collectAsState()
    val showCelebration by viewModel.runCompletedCelebration.collectAsState()

    var showFinishConfirmDialog by remember { mutableStateOf(false) }

    val formattedDuration = remember(durationSeconds) {
        val min = durationSeconds / 60
        val sec = durationSeconds % 60
        String.format(java.util.Locale.US, "%02d:%02d", min, sec)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP GPS MAP
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                DarkGpsMapCanvas(
                    routePoints = routePoints,
                    gpsStatusText = gpsStatus,
                    isTracking = !isPaused
                )

                if (mode == "META") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Meta: ${targetKm.toInt()} km",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlfaNeonLime
                        )
                        Text(
                            text = "${((distanceKm / targetKm) * 100).toInt().coerceIn(0, 100)}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlfaTextWhite
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (distanceKm / targetKm).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AlfaNeonLime,
                        trackColor = AlfaSurfaceCard
                    )
                }
            }

            // MAIN RUN METRICS DISPLAY
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Huge Distance in KM
                Text(
                    text = String.format(java.util.Locale.US, "%.2f", distanceKm),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaNeonLime,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "QUILÔMETROS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    color = AlfaTextMuted
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row (Duration, Pace, Calories)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RunStatItem(
                        value = formattedDuration,
                        label = "TEMPO",
                        icon = Icons.Default.Timer
                    )

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(1.dp)
                            .background(AlfaBorder)
                    )

                    RunStatItem(
                        value = "$avgPace /km",
                        label = "PACE MÉDIO",
                        icon = Icons.Default.Speed
                    )

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(1.dp)
                            .background(AlfaBorder)
                    )

                    RunStatItem(
                        value = "$calories kcal",
                        label = "CALORIAS",
                        icon = Icons.Default.LocalFireDepartment
                    )
                }
            }

            // BOTTOM ACTION CONTROLS (Pause / Resume / Stop)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Finish Button
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(AlfaSurfaceCard)
                        .border(1.dp, AlfaBorder, CircleShape)
                        .clickable { showFinishConfirmDialog = true }
                        .testTag("stop_run_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Encerrar",
                        tint = Color(0xFFFF3366),
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Pause / Resume Big Neon Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AlfaNeonLime)
                        .clickable {
                            if (isPaused) viewModel.resumeRun() else viewModel.pauseRun()
                        }
                        .testTag("pause_resume_run_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPaused) "Continuar" else "Pausar",
                        tint = AlfaBlack,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }

    // GOAL CELEBRATION MODAL
    if (showCelebration) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCelebration() },
            containerColor = AlfaSurfaceDark,
            titleContentColor = AlfaTextWhite,
            textContentColor = AlfaTextGray,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AlfaNeonLime,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Meta Concluída! 🎉",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = AlfaTextWhite
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Parabéns, você atingiu sua meta de ${targetKm.toInt()} km!",
                        fontSize = 14.sp,
                        color = AlfaTextGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Pace", fontSize = 11.sp, color = AlfaTextMuted)
                            Text(text = avgPace, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AlfaNeonLime)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Tempo", fontSize = 11.sp, color = AlfaTextMuted)
                            Text(text = formattedDuration, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AlfaTextWhite)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Calorias", fontSize = 11.sp, color = AlfaTextMuted)
                            Text(text = "$calories kcal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AlfaTextWhite)
                        }
                    }
                }
            },
            confirmButton = {
                AlfaPrimaryButton(
                    text = "VER RESUMO E FINALIZAR",
                    onClick = {
                        viewModel.finishRun()
                        viewModel.dismissCelebration()
                        onFinishRun()
                    },
                    testTag = "finish_celebration_button"
                )
            }
        )
    }

    // FINISH CONFIRM DIALOG
    if (showFinishConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showFinishConfirmDialog = false },
            containerColor = AlfaSurfaceDark,
            titleContentColor = AlfaTextWhite,
            textContentColor = AlfaTextGray,
            title = {
                Text(text = "Encerrar Corrida?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Deseja finalizar e salvar os dados desta sessão?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.finishRun()
                        showFinishConfirmDialog = false
                        onFinishRun()
                    }
                ) {
                    Text("Salvar e Sair", color = AlfaNeonLime, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishConfirmDialog = false }) {
                    Text("Continuar Correndo", color = AlfaTextGray)
                }
            }
        )
    }
}

@Composable
fun RunStatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AlfaNeonLime,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            color = AlfaTextWhite
        )
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = AlfaTextMuted
        )
    }
}
