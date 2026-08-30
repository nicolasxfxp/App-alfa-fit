package com.example.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.main.ActiveRunScreen
import com.example.ui.screens.main.ActiveWorkoutScreen
import com.example.ui.screens.main.MainContainerScreen
import com.example.ui.screens.main.WorkoutDetailScreen
import com.example.ui.theme.AlfaFitTheme
import com.example.ui.viewmodel.AlfaFitViewModel

object AlfaFitRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val MAIN = "main"
    const val WORKOUT_DETAIL = "workout_detail"
    const val ACTIVE_WORKOUT = "active_workout"
    const val ACTIVE_RUN = "active_run"
}

@Composable
fun AlfaFitApp(
    viewModel: AlfaFitViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    AlfaFitTheme {
        NavHost(
            navController = navController,
            startDestination = AlfaFitRoutes.LOGIN,
            modifier = modifier,
            enterTransition = { fadeIn(animationSpec = tween(250)) },
            exitTransition = { fadeOut(animationSpec = tween(250)) }
        ) {
            // 1. MANDATORY INITIAL SCREEN: LOGIN
            composable(AlfaFitRoutes.LOGIN) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(AlfaFitRoutes.MAIN) {
                            popUpTo(AlfaFitRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(AlfaFitRoutes.REGISTER)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(AlfaFitRoutes.FORGOT_PASSWORD)
                    }
                )
            }

            // 2. REGISTER SCREEN
            composable(AlfaFitRoutes.REGISTER) {
                RegisterScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = {
                        navController.navigate(AlfaFitRoutes.MAIN) {
                            popUpTo(AlfaFitRoutes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // 3. FORGOT PASSWORD
            composable(AlfaFitRoutes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 4. MAIN CONTAINER (HOME, TREINOS, CORRIDA, ALFA IA, PROGRESSO, PERFIL)
            composable(AlfaFitRoutes.MAIN) {
                MainContainerScreen(
                    viewModel = viewModel,
                    onNavigateToWorkoutDetail = { workout ->
                        viewModel.selectWorkout(workout)
                        navController.navigate(AlfaFitRoutes.WORKOUT_DETAIL)
                    },
                    onNavigateToActiveRun = {
                        navController.navigate(AlfaFitRoutes.ACTIVE_RUN)
                    },
                    onLogout = {
                        navController.navigate(AlfaFitRoutes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 5. WORKOUT DETAIL
            composable(AlfaFitRoutes.WORKOUT_DETAIL) {
                val selectedWorkout by viewModel.selectedWorkout.collectAsState()
                selectedWorkout?.let { workout ->
                    WorkoutDetailScreen(
                        workout = workout,
                        viewModel = viewModel,
                        onStartWorkout = {
                            navController.navigate(AlfaFitRoutes.ACTIVE_WORKOUT)
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            // 6. ACTIVE WORKOUT EXECUTION TRACKER
            composable(AlfaFitRoutes.ACTIVE_WORKOUT) {
                ActiveWorkoutScreen(
                    viewModel = viewModel,
                    onFinishAndExit = {
                        navController.popBackStack(AlfaFitRoutes.MAIN, inclusive = false)
                    }
                )
            }

            // 7. ACTIVE RUNNING GPS TRACKER
            composable(AlfaFitRoutes.ACTIVE_RUN) {
                ActiveRunScreen(
                    viewModel = viewModel,
                    onFinishRun = {
                        navController.popBackStack(AlfaFitRoutes.MAIN, inclusive = false)
                    }
                )
            }
        }
    }
}
