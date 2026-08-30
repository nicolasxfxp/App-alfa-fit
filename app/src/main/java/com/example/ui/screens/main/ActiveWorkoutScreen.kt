package com.example.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SkipNext
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaSecondaryButton
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
fun ActiveWorkoutScreen(
    viewModel: AlfaFitViewModel,
    onFinishAndExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedWorkout by viewModel.selectedWorkout.collectAsState()
    val exercises by viewModel.selectedWorkoutExercises.collectAsState()
    val currentIndex by viewModel.currentExerciseIndex.collectAsState()
    val currentSet by viewModel.currentSetIndex.collectAsState()
    val elapsedSeconds by viewModel.workoutTimerSeconds.collectAsState()
    val isResting by viewModel.isResting.collectAsState()
    val restCountdown by viewModel.restCountdownSeconds.collectAsState()
    val isFinished by viewModel.workoutFinished.collectAsState()

    val currentExercise = exercises.getOrNull(currentIndex)
    var showQuitConfirmDialog by remember { mutableStateOf(false) }

    val formattedElapsed = remember(elapsedSeconds) {
        val min = elapsedSeconds / 60
        val sec = elapsedSeconds % 60
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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP HEADER
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = selectedWorkout?.name ?: "Treino em Andamento",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = AlfaTextWhite
                        )
                        Text(
                            text = "Exercício ${currentIndex + 1} de ${exercises.size}",
                            fontSize = 13.sp,
                            color = AlfaNeonLime,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Quit button
                    IconButton(
                        onClick = { showQuitConfirmDialog = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AlfaSurfaceCard)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Encerrar",
                            tint = AlfaTextGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = {
                        if (exercises.isNotEmpty()) (currentIndex + 1).toFloat() / exercises.size.toFloat() else 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AlfaNeonLime,
                    trackColor = AlfaSurfaceCard
                )
            }

            // CENTER EXERCISE DISPLAY
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Live Elapsed Time Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AlfaSurfaceDark)
                        .border(1.dp, AlfaBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = AlfaNeonLime,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formattedElapsed,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = AlfaTextWhite,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isResting) {
                    // REST COUNTDOWN DISPLAY
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AlfaSurfaceElevated),
                        border = androidx.compose.foundation.BorderStroke(2.dp, AlfaNeonLime)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "DESCANSO",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                color = AlfaNeonLime
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "00:${String.format(java.util.Locale.US, "%02d", restCountdown)}",
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Black,
                                color = AlfaTextWhite,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AlfaSecondaryButton(
                                text = "Pular Descanso",
                                icon = Icons.Default.SkipNext,
                                onClick = { viewModel.skipRest() },
                                height = 44.dp,
                                testTag = "skip_rest_button"
                            )
                        }
                    }
                } else if (currentExercise != null) {
                    // ACTIVE EXERCISE CARD
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = currentExercise.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = AlfaTextWhite
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = currentExercise.instructions.ifBlank { "Execução controlada com amplitude máxima." },
                                fontSize = 13.sp,
                                color = AlfaTextGray
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // SETS & REPS BIG DISPLAY
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "SÉRIE",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlfaTextMuted
                                    )
                                    Text(
                                        text = "$currentSet / ${currentExercise.sets}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AlfaNeonLime
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(1.dp)
                                        .background(AlfaBorder)
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "REPETIÇÕES",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlfaTextMuted
                                    )
                                    Text(
                                        text = "${currentExercise.reps}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AlfaTextWhite
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(1.dp)
                                        .background(AlfaBorder)
                                )

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "CARGA",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AlfaTextMuted
                                    )
                                    Text(
                                        text = "${currentExercise.weightKg.toInt()} kg",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AlfaTextWhite
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // BOTTOM CONTROLS
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AlfaPrimaryButton(
                    text = if (currentExercise != null && currentSet >= currentExercise.sets && currentIndex >= exercises.size - 1) "FINALIZAR TREINO" else "CONCLUIR SÉRIE",
                    icon = Icons.Default.Check,
                    onClick = { viewModel.completeSet() },
                    testTag = "complete_set_button"
                )

                if (currentIndex < exercises.size - 1) {
                    AlfaSecondaryButton(
                        text = "Próximo Exercício",
                        icon = Icons.Default.SkipNext,
                        onClick = { viewModel.nextExercise() },
                        testTag = "next_exercise_button"
                    )
                }
            }
        }
    }

    // WORKOUT FINISHED CELEBRATION MODAL
    if (isFinished) {
        AlertDialog(
            onDismissRequest = onFinishAndExit,
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
                        text = "Treino Concluído! 🔥",
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
                        text = "Excelente dedicação! Mais um degrau na sua evolução.",
                        fontSize = 14.sp,
                        color = AlfaTextGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Tempo", fontSize = 11.sp, color = AlfaTextMuted)
                            Text(text = formattedElapsed, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AlfaTextWhite)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Exercícios", fontSize = 11.sp, color = AlfaTextMuted)
                            Text(text = "${exercises.size}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AlfaNeonLime)
                        }
                    }
                }
            },
            confirmButton = {
                AlfaPrimaryButton(
                    text = "CONCLUIR E SALVAR",
                    onClick = onFinishAndExit,
                    testTag = "save_and_exit_workout_button"
                )
            }
        )
    }

    // CONFIRM QUIT DIALOG
    if (showQuitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showQuitConfirmDialog = false },
            containerColor = AlfaSurfaceDark,
            titleContentColor = AlfaTextWhite,
            textContentColor = AlfaTextGray,
            title = {
                Text(text = "Encerrar Treino?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Deseja salvar o progresso realizado até aqui?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.finishWorkout()
                        showQuitConfirmDialog = false
                        onFinishAndExit()
                    }
                ) {
                    Text("Salvar e Sair", color = AlfaNeonLime, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitConfirmDialog = false }) {
                    Text("Continuar Treinando", color = AlfaTextGray)
                }
            }
        )
    }
}
