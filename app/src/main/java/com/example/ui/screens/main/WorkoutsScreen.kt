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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.WorkoutEntity
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
fun WorkoutsScreen(
    viewModel: AlfaFitViewModel,
    onSelectWorkout: (WorkoutEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val allWorkouts by viewModel.allWorkouts.collectAsState()
    var selectedCategory by remember { mutableStateOf("Todos") }

    val categories = listOf(
        "Todos", "Pernas", "Peito", "Costas", "Ombros", "Bíceps", "Tríceps", "Glúteos", "Abdômen", "Full Body", "Cardio"
    )

    val filteredWorkouts = remember(allWorkouts, selectedCategory) {
        if (selectedCategory == "Todos") allWorkouts else allWorkouts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
            .padding(top = 16.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Biblioteca de Treinos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Escolha um treino estruturado e eleve sua intensidade",
                fontSize = 13.sp,
                color = AlfaTextGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Categories Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) AlfaNeonLime else AlfaSurfaceCard)
                        .border(
                            1.dp,
                            if (isSelected) AlfaNeonLime else AlfaBorderSubtle,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("filter_chip_$category")
                ) {
                    Text(
                        text = category,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                        color = if (isSelected) AlfaBlack else AlfaTextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Workout Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredWorkouts) { workout ->
                WorkoutCardItem(
                    workout = workout,
                    onClick = { onSelectWorkout(workout) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun WorkoutCardItem(
    workout: WorkoutEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("workout_card_${workout.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AlfaNeonLimeGlow)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = workout.category.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = AlfaNeonLime,
                        letterSpacing = 0.5.sp
                    )
                }

                // Level Tag
                Text(
                    text = workout.level,
                    fontSize = 12.sp,
                    color = AlfaTextGray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = workout.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = workout.muscleGroup,
                fontSize = 13.sp,
                color = AlfaTextGray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = AlfaTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${workout.exerciseCount} exercícios",
                        fontSize = 12.sp,
                        color = AlfaTextMuted
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = AlfaTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "~${workout.estimatedDurationMin} min",
                        fontSize = 12.sp,
                        color = AlfaTextMuted
                    )
                }

                // Start button badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(AlfaNeonLime)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INICIAR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = AlfaBlack
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = AlfaBlack,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
