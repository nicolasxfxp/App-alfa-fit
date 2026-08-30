package com.example.ui.screens.main

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.ExerciseEntity
import com.example.data.model.WorkoutEntity
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
fun WorkoutDetailScreen(
    workout: WorkoutEntity,
    viewModel: AlfaFitViewModel,
    onStartWorkout: (WorkoutEntity) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val exercises by viewModel.selectedWorkoutExercises.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 90.dp)
        ) {
            // HERO IMAGE BANNER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_workout_hero),
                        contentDescription = workout.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        AlfaBlack.copy(alpha = 0.5f),
                                        AlfaBlack.copy(alpha = 0.8f),
                                        AlfaBlack
                                    )
                                )
                            )
                    )

                    // Back Button
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(top = 40.dp, start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AlfaBlack.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = AlfaTextWhite
                        )
                    }

                    // Workout Title in Hero
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AlfaNeonLime)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = workout.category.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = AlfaBlack
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = workout.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = AlfaTextWhite
                        )
                    }
                }
            }

            // INFO SUMMARY ROW
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoPill(icon = Icons.Default.FitnessCenter, text = "${workout.exerciseCount} Exercícios")
                    InfoPill(icon = Icons.Default.Timer, text = "~${workout.estimatedDurationMin} min")
                    InfoPill(icon = Icons.Default.FitnessCenter, text = workout.level)
                }

                if (workout.description.isNotBlank()) {
                    Text(
                        text = workout.description,
                        fontSize = 13.sp,
                        color = AlfaTextGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Lista de Exercícios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaTextWhite,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // EXERCISES LIST
            itemsIndexed(exercises) { index, exercise ->
                ExerciseDetailItem(index = index + 1, exercise = exercise)
            }
        }

        // FLOATING ACTION BAR AT BOTTOM
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, AlfaBlack, AlfaBlack)
                    )
                )
                .padding(16.dp)
        ) {
            AlfaPrimaryButton(
                text = "INICIAR TREINO AGORA",
                icon = Icons.Default.PlayArrow,
                onClick = {
                    viewModel.startWorkoutExecution(workout)
                    onStartWorkout(workout)
                },
                testTag = "start_workout_button"
            )
        }
    }
}

@Composable
fun InfoPill(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(AlfaSurfaceCard)
            .border(1.dp, AlfaBorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AlfaNeonLime,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AlfaTextWhite
        )
    }
}

@Composable
fun ExerciseDetailItem(index: Int, exercise: ExerciseEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index Circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AlfaNeonLimeGlow)
                    .border(1.dp, AlfaNeonLime, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$index",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaNeonLime
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlfaTextWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${exercise.sets} séries • ${exercise.reps} reps • ${exercise.weightKg.toInt()} kg",
                    fontSize = 13.sp,
                    color = AlfaNeonLime,
                    fontWeight = FontWeight.SemiBold
                )
                if (exercise.instructions.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = exercise.instructions,
                        fontSize = 11.sp,
                        color = AlfaTextGray
                    )
                }
            }

            // Rest Time badge
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Descanso",
                    fontSize = 10.sp,
                    color = AlfaTextMuted
                )
                Text(
                    text = "${exercise.restTimeSeconds}s",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlfaTextWhite
                )
            }
        }
    }
}
