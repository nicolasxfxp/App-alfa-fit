package com.example.ui.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutEntity
import com.example.ui.theme.AlfaBlack
import com.example.ui.theme.AlfaBorder
import com.example.ui.theme.AlfaNeonLime
import com.example.ui.theme.AlfaSurfaceDark
import com.example.ui.theme.AlfaTextGray
import com.example.ui.theme.AlfaTextMuted
import com.example.ui.viewmodel.AlfaFitViewModel

data class AlfaNavDestination(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun MainContainerScreen(
    viewModel: AlfaFitViewModel,
    onNavigateToWorkoutDetail: (WorkoutEntity) -> Unit,
    onNavigateToActiveRun: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val destinations = listOf(
        AlfaNavDestination("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        AlfaNavDestination("Treinos", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter, "nav_workouts"),
        AlfaNavDestination("Corrida", Icons.Filled.DirectionsRun, Icons.Outlined.DirectionsRun, "nav_running"),
        AlfaNavDestination("Alfa IA", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome, "nav_ia"),
        AlfaNavDestination("Progresso", Icons.Filled.TrendingUp, Icons.Outlined.TrendingUp, "nav_progress"),
        AlfaNavDestination("Perfil", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AlfaBlack,
        bottomBar = {
            NavigationBar(
                containerColor = AlfaSurfaceDark,
                contentColor = AlfaNeonLime,
                tonalElevation = 8.dp
            ) {
                destinations.forEachIndexed { index, dest ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) dest.selectedIcon else dest.unselectedIcon,
                                contentDescription = dest.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = dest.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AlfaBlack,
                            selectedTextColor = AlfaNeonLime,
                            indicatorColor = AlfaNeonLime,
                            unselectedIconColor = AlfaTextGray,
                            unselectedTextColor = AlfaTextMuted
                        ),
                        modifier = Modifier.testTag(dest.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToWorkouts = { selectedTab = 1 },
                    onNavigateToWorkoutDetail = { workoutId ->
                        // Load and navigate to workout detail
                        viewModel.allWorkouts.value.find { it.id == workoutId }?.let {
                            viewModel.selectWorkout(it)
                            onNavigateToWorkoutDetail(it)
                        } ?: run {
                            selectedTab = 1
                        }
                    },
                    onNavigateToRunning = { selectedTab = 2 },
                    onNavigateToAlfaIa = { selectedTab = 3 },
                    onNavigateToProgress = { selectedTab = 4 }
                )
                1 -> WorkoutsScreen(
                    viewModel = viewModel,
                    onSelectWorkout = { workout ->
                        viewModel.selectWorkout(workout)
                        onNavigateToWorkoutDetail(workout)
                    }
                )
                2 -> RunningScreen(
                    viewModel = viewModel,
                    onStartRun = onNavigateToActiveRun
                )
                3 -> AlfaIaScreen(
                    viewModel = viewModel
                )
                4 -> ProgressScreen(
                    viewModel = viewModel
                )
                5 -> ProfileScreen(
                    viewModel = viewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}
