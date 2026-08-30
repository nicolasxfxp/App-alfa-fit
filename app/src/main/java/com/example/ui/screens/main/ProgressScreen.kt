package com.example.ui.screens.main

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeightProgressEntity
import com.example.ui.components.AlfaPrimaryButton
import com.example.ui.components.AlfaSecondaryButton
import com.example.ui.components.AlfaStatBox
import com.example.ui.components.AlfaTextField
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaBorderSubtle
import com.example.ui.theme.AlfaDarkNavy
import com.example.ui.theme.AlfaNeonGreen
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaNeonLimeGlow
import com.example.ui.theme.AlfaSurfaceCard
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.theme.AlfaTextWhite
import com.example.ui.viewmodel.AlfaFitViewModel

@Composable
fun ProgressScreen(
    viewModel: AlfaFitViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Geral", "Treinos", "Corridas")

    val user by viewModel.loggedInUser.collectAsState()
    val weightList by viewModel.allWeightProgress.collectAsState()
    val workoutLogs by viewModel.allWorkoutLogs.collectAsState()
    val runLogs by viewModel.allRunLogs.collectAsState()
    val totalWorkouts by viewModel.totalWorkoutsCount.collectAsState()
    val totalDistance by viewModel.totalRunDistance.collectAsState()

    var showAddWeightDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
    ) {
        // TOP HEADER
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                text = "Evolução & Progresso",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Acompanhe seus resultados e histórico de dedicação",
                fontSize = 13.sp,
                color = AlfaTextGray
            )
        }

        // TABS: Geral, Treinos, Corridas
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = AlfaSurfaceDark,
            contentColor = AlfaNeonLime,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AlfaNeonLime,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = tabTitle,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Black else FontWeight.Medium,
                            color = if (selectedTab == index) AlfaNeonLime else AlfaTextGray
                        )
                    },
                    modifier = Modifier.testTag("progress_tab_$tabTitle")
                )
            }
        }

        when (selectedTab) {
            0 -> TabGeralContent(
                userWeight = user?.weightKg ?: 72.4f,
                weightHistory = weightList,
                onAddWeightClick = { showAddWeightDialog = true }
            )
            1 -> TabTreinosContent(
                totalWorkouts = totalWorkouts,
                workoutLogs = workoutLogs
            )
            2 -> TabCorridasContent(
                totalDistance = totalDistance ?: 42.8f,
                runLogs = runLogs
            )
        }
    }

    if (showAddWeightDialog) {
        AddWeightDialog(
            currentWeight = user?.weightKg ?: 72.4f,
            onDismiss = { showAddWeightDialog = false },
            onConfirm = { newWeight, note ->
                viewModel.addWeightProgressEntry(newWeight, note)
                showAddWeightDialog = false
            }
        )
    }
}

@Composable
fun TabGeralContent(
    userWeight: Float,
    weightHistory: List<WeightProgressEntity>,
    onAddWeightClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // WEIGHT SUMMARY CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Peso Atual",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlfaTextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = String.format(java.util.Locale.US, "%.1f", userWeight),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black,
                                color = AlfaNeonLime
                            )
                            Text(
                                text = " kg",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlfaTextWhite,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }

                    // Variation badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(AlfaNeonGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = AlfaNeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "-2.3 kg desde o início",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AlfaNeonGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Weight Progression Chart Canvas
                WeightChartCanvas(weightHistory = weightHistory)

                Spacer(modifier = Modifier.height(16.dp))

                AlfaSecondaryButton(
                    text = "Registrar Novo Peso",
                    icon = Icons.Default.Add,
                    onClick = onAddWeightClick,
                    height = 44.dp,
                    testTag = "add_weight_button"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // STREAK & CONSISTENCY CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AlfaNeonLimeGlow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Whatshot,
                        contentDescription = null,
                        tint = AlfaNeonLime,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Sequência de 12 Dias Ativos 🔥",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = AlfaTextWhite
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Você treinou 5 vezes esta semana. Mantenha o ritmo!",
                        fontSize = 12.sp,
                        color = AlfaTextGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun WeightChartCanvas(weightHistory: List<WeightProgressEntity>) {
    val points = if (weightHistory.size >= 2) weightHistory.map { it.weightKg } else listOf(74.7f, 74.0f, 73.5f, 72.9f, 72.4f)
    val min = points.minOrNull() ?: 70f
    val max = points.maxOrNull() ?: 75f
    val range = if (max - min > 0) max - min else 1f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AlfaDarkNavy)
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val stepX = width / (points.size - 1)

            val path = Path()
            points.forEachIndexed { i, w ->
                val x = i * stepX
                val normalized = (w - min) / range
                val y = height - (normalized * (height - 20.dp.toPx())) - 10.dp.toPx()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)

                // Draw point circle
                drawCircle(
                    color = AlfaNeonLime,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }

            drawPath(
                path = path,
                color = AlfaNeonLime,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun TabTreinosContent(
    totalWorkouts: Int,
    workoutLogs: List<com.example.data.model.WorkoutLogEntity>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AlfaStatBox(
                value = "$totalWorkouts",
                label = "Treinos Feitos",
                icon = Icons.Default.FitnessCenter,
                highlight = true,
                modifier = Modifier.weight(1f)
            )
            AlfaStatBox(
                value = "16h 32m",
                label = "Tempo Total",
                icon = Icons.Default.Timer,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Histórico de Treinos",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = AlfaTextWhite
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            workoutLogs.take(10).forEach { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = log.workoutName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlfaTextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${log.totalSetsCompleted} séries • ${log.durationSeconds / 60} min • ${log.totalVolumeKg.toInt()} kg volume",
                                fontSize = 12.sp,
                                color = AlfaTextGray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AlfaNeonLimeGlow)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = log.category.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = AlfaNeonLime
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun TabCorridasContent(
    totalDistance: Float,
    runLogs: List<com.example.data.model.RunLogEntity>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AlfaStatBox(
                value = String.format(java.util.Locale.US, "%.1f km", totalDistance),
                label = "Distância Total",
                icon = Icons.Default.DirectionsRun,
                highlight = true,
                modifier = Modifier.weight(1f)
            )
            AlfaStatBox(
                value = "5:58 /km",
                label = "Pace Médio",
                icon = Icons.Default.Speed,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Histórico de Corridas",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = AlfaTextWhite
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            runLogs.take(10).forEach { run ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AlfaSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AlfaBorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = String.format(java.util.Locale.US, "%.2f km", run.distanceKm),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = AlfaNeonLime
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${run.durationSeconds / 60} min • Pace ${run.avgPaceMinKm} • ${run.caloriesKcal} kcal",
                                fontSize = 12.sp,
                                color = AlfaTextGray
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AlfaSurfaceDark)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = run.mode,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlfaTextWhite
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun AddWeightDialog(
    currentWeight: Float,
    onDismiss: () -> Unit,
    onConfirm: (Float, String) -> Unit
) {
    var weightStr by remember { mutableStateOf(String.format(java.util.Locale.US, "%.1f", currentWeight)) }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AlfaSurfaceDark,
        titleContentColor = AlfaTextWhite,
        textContentColor = AlfaTextGray,
        title = {
            Text(text = "Registrar Peso", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                AlfaTextField(
                    value = weightStr,
                    onValueChange = { weightStr = it },
                    label = "Peso em kg",
                    placeholder = "Ex: 72.4",
                    leadingIcon = Icons.Default.MonitorWeight,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    testTag = "new_weight_input"
                )
                Spacer(modifier = Modifier.height(10.dp))
                AlfaTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = "Nota (opcional)",
                    placeholder = "Ex: Pós-treino",
                    testTag = "weight_note_input"
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val w = weightStr.toFloatOrNull() ?: currentWeight
                    onConfirm(w, note)
                }
            ) {
                Text("Salvar", color = AlfaNeonLime, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = AlfaTextGray)
            }
        }
    )
}
