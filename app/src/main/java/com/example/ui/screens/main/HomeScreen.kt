package com.example.ui.screens.main

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AlfaFitTopBar
import com.example.ui.components.AlfaHeroFeatureCard
import com.example.ui.components.AlfaStatBox
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

data class QuickAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val testTag: String
)

@Composable
fun HomeScreen(
    viewModel: AlfaFitViewModel,
    onNavigateToWorkouts: () -> Unit,
    onNavigateToWorkoutDetail: (Long) -> Unit,
    onNavigateToRunning: () -> Unit,
    onNavigateToAlfaIa: () -> Unit,
    onNavigateToProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.loggedInUser.collectAsState()
    val totalWorkouts by viewModel.totalWorkoutsCount.collectAsState()
    val totalDistance by viewModel.totalRunDistance.collectAsState()
    val totalDurationSec by viewModel.totalWorkoutDurationSeconds.collectAsState()

    val formattedDuration = rememberFormattedDuration(totalDurationSec ?: 59520)

    val quickActions = listOf(
        QuickAction(
            title = "Quero Treinar",
            subtitle = "Musculação & Cargas",
            icon = Icons.Default.FitnessCenter,
            onClick = onNavigateToWorkouts,
            testTag = "quick_action_train"
        ),
        QuickAction(
            title = "Quero Correr",
            subtitle = "GPS & Pace ao Vivo",
            icon = Icons.Default.DirectionsRun,
            onClick = onNavigateToRunning,
            testTag = "quick_action_run"
        ),
        QuickAction(
            title = "Analisar Refeição",
            subtitle = "Alfa IA Nutrição",
            icon = Icons.Default.AutoAwesome,
            onClick = onNavigateToAlfaIa,
            testTag = "quick_action_ia"
        ),
        QuickAction(
            title = "Ver Meu Progresso",
            subtitle = "Evolução e Peso",
            icon = Icons.Default.TrendingUp,
            onClick = onNavigateToProgress,
            testTag = "quick_action_progress"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AlfaBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // TOP GREETING BAR
        AlfaFitTopBar(
            userName = user?.name?.split(" ")?.firstOrNull() ?: "Atleta"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // QUICK ACTION HUB
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "O que você quer fazer?",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = AlfaTextWhite
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(quickActions) { action ->
                    Box(
                        modifier = Modifier
                            .width(155.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(AlfaSurfaceCard)
                            .border(1.dp, AlfaBorder, RoundedCornerShape(16.dp))
                            .clickable { action.onClick() }
                            .padding(14.dp)
                            .testTag(action.testTag)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(AlfaNeonLimeGlow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = action.icon,
                                    contentDescription = null,
                                    tint = AlfaNeonLime,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = action.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlfaTextWhite
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = action.subtitle,
                                fontSize = 11.sp,
                                color = AlfaTextGray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // FEATURED CARDS SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CARD 1: TREINO DE HOJE
            AlfaHeroFeatureCard(
                tag = "Treino de hoje",
                title = "Pernas Explosivas",
                subtitle = "6 exercícios • ~35 min • Alta intensidade",
                buttonText = "VER TREINO",
                imageRes = R.drawable.img_workout_hero,
                onClick = { onNavigateToWorkoutDetail(1L) },
                testTag = "home_workout_card"
            )

            // CARD 2: CORRIDA
            AlfaHeroFeatureCard(
                tag = "Corrida",
                title = "Quer correr hoje?",
                subtitle = "Rastreie pace, distância e rota com GPS ao vivo",
                buttonText = "INICIAR CORRIDA",
                imageRes = R.drawable.img_running_hero,
                onClick = onNavigateToRunning,
                testTag = "home_running_card"
            )

            // CARD 3: ALFA IA
            AlfaHeroFeatureCard(
                tag = "Alfa IA",
                title = "Analise sua refeição",
                subtitle = "Tire uma foto e veja estimativa de calorias e macros",
                buttonText = "ANALISAR AGORA",
                imageRes = R.drawable.img_meal_hero,
                onClick = onNavigateToAlfaIa,
                testTag = "home_ia_card"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // RESUMO RÁPIDO DE PROGRESSO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resumo rápido de progresso",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = AlfaTextWhite
                )
                Text(
                    text = "Ver tudo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlfaNeonLime,
                    modifier = Modifier.clickable { onNavigateToProgress() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AlfaStatBox(
                    value = "$totalWorkouts",
                    label = "Treinos",
                    icon = Icons.Default.FitnessCenter,
                    modifier = Modifier.weight(1f)
                )
                AlfaStatBox(
                    value = String.format(java.util.Locale.US, "%.1f km", totalDistance ?: 42.8f),
                    label = "Corridas",
                    icon = Icons.Default.DirectionsRun,
                    modifier = Modifier.weight(1f),
                    highlight = true
                )
                AlfaStatBox(
                    value = formattedDuration,
                    label = "Tempo",
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun rememberFormattedDuration(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}
