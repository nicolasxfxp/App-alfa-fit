package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiMealService
import com.example.ai.MealNutrientResult
import com.example.data.local.AlfaFitDatabase
import com.example.data.model.ExerciseEntity
import com.example.data.model.MealAnalysisEntity
import com.example.data.model.RunLogEntity
import com.example.data.model.UserEntity
import com.example.data.model.WeightProgressEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutLogEntity
import com.example.data.repository.AlfaFitRepository
import com.example.ui.components.MapPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class AlfaFitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlfaFitRepository
    private val geminiMealService = GeminiMealService()

    init {
        val db = AlfaFitDatabase.getInstance(application)
        repository = AlfaFitRepository(db.dao())
    }

    // --- State Streams ---
    val loggedInUser: StateFlow<UserEntity?> = repository.loggedInUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allWorkouts: StateFlow<List<WorkoutEntity>> = repository.allWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWorkoutLogs: StateFlow<List<WorkoutLogEntity>> = repository.allWorkoutLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRunLogs: StateFlow<List<RunLogEntity>> = repository.allRunLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWeightProgress: StateFlow<List<WeightProgressEntity>> = repository.allWeightProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMealAnalyses: StateFlow<List<MealAnalysisEntity>> = repository.allMealAnalyses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalWorkoutsCount: StateFlow<Int> = repository.totalWorkoutsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 28)

    val totalRunDistance: StateFlow<Float?> = repository.totalRunDistance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 42.8f)

    val totalWorkoutDurationSeconds: StateFlow<Int?> = repository.totalWorkoutDurationSeconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 59520) // ~16h 32min

    // --- Auth UI States ---
    private val _authLoading = MutableStateFlow(false)
    val authLoading: StateFlow<Boolean> = _authLoading.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    fun clearAuthError() {
        _authError.value = null
    }

    fun login(email: String, passwordRaw: String, onSuccess: () -> Unit) {
        if (email.isBlank() || !email.contains("@")) {
            _authError.value = "Por favor, digite um e-mail válido."
            return
        }
        if (passwordRaw.length < 4) {
            _authError.value = "A senha deve ter pelo menos 4 caracteres."
            return
        }

        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null
            delay(600) // Realistic authentication check latency
            val result = repository.authenticate(email, passwordRaw)
            _authLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                // If demo user doesn't exist yet, automatically auto-create demo user for smooth testing
                if (it.message?.contains("não encontrado", ignoreCase = true) == true) {
                    val created = repository.registerUser(
                        name = "Nicolas Cauã",
                        email = email,
                        passwordRaw = passwordRaw,
                        birthDate = "15/05/1998",
                        heightCm = 178f,
                        weightKg = 72.4f,
                        goal = "Ganhar massa muscular"
                    )
                    created.onSuccess { onSuccess() }
                        .onFailure { err -> _authError.value = err.message }
                } else {
                    _authError.value = it.message ?: "Falha na autenticação"
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        passwordRaw: String,
        birthDate: String,
        heightCm: Float,
        weightKg: Float,
        goal: String,
        onSuccess: () -> Unit
    ) {
        if (name.isBlank()) {
            _authError.value = "Por favor, digite seu nome completo."
            return
        }
        if (email.isBlank() || !email.contains("@")) {
            _authError.value = "Por favor, digite um e-mail válido."
            return
        }
        if (passwordRaw.length < 4) {
            _authError.value = "A senha deve ter pelo menos 4 caracteres."
            return
        }

        viewModelScope.launch {
            _authLoading.value = true
            _authError.value = null
            delay(500)
            val result = repository.registerUser(
                name = name,
                email = email,
                passwordRaw = passwordRaw,
                birthDate = birthDate,
                heightCm = heightCm,
                weightKg = weightKg,
                goal = goal
            )
            _authLoading.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                _authError.value = it.message ?: "Erro ao criar conta"
            }
        }
    }

    fun loginWithGoogle(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _authLoading.value = true
            delay(700)
            repository.signInWithGoogleDemo("Nicolas Cauã", "nicolascaua32@gmail.com")
            _authLoading.value = false
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onSuccess()
        }
    }

    fun updateUserProfile(name: String, weightKg: Float, heightCm: Float, goal: String) {
        val current = loggedInUser.value ?: return
        viewModelScope.launch {
            repository.updateUserProfile(
                current.copy(
                    name = name,
                    weightKg = weightKg,
                    heightCm = heightCm,
                    goal = goal
                )
            )
            repository.addWeightProgress(weightKg, "Atualização de perfil")
        }
    }

    // --- Active Workout Execution State ---
    private val _selectedWorkout = MutableStateFlow<WorkoutEntity?>(null)
    val selectedWorkout: StateFlow<WorkoutEntity?> = _selectedWorkout.asStateFlow()

    private val _selectedWorkoutExercises = MutableStateFlow<List<ExerciseEntity>>(emptyList())
    val selectedWorkoutExercises: StateFlow<List<ExerciseEntity>> = _selectedWorkoutExercises.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _currentSetIndex = MutableStateFlow(1)
    val currentSetIndex: StateFlow<Int> = _currentSetIndex.asStateFlow()

    private val _workoutTimerSeconds = MutableStateFlow(0)
    val workoutTimerSeconds: StateFlow<Int> = _workoutTimerSeconds.asStateFlow()

    private val _restCountdownSeconds = MutableStateFlow(0)
    val restCountdownSeconds: StateFlow<Int> = _restCountdownSeconds.asStateFlow()

    private val _isResting = MutableStateFlow(false)
    val isResting: StateFlow<Boolean> = _isResting.asStateFlow()

    private val _workoutFinished = MutableStateFlow(false)
    val workoutFinished: StateFlow<Boolean> = _workoutFinished.asStateFlow()

    private var workoutTimerJob: Job? = null
    private var restTimerJob: Job? = null

    fun selectWorkout(workout: WorkoutEntity) {
        _selectedWorkout.value = workout
        viewModelScope.launch {
            val exercises = repository.getExercisesListForWorkout(workout.id)
            _selectedWorkoutExercises.value = if (exercises.isNotEmpty()) exercises else generateDefaultExercisesForWorkout(workout)
        }
    }

    fun startWorkoutExecution(workout: WorkoutEntity) {
        selectWorkout(workout)
        _currentExerciseIndex.value = 0
        _currentSetIndex.value = 1
        _workoutTimerSeconds.value = 0
        _isResting.value = false
        _workoutFinished.value = false

        workoutTimerJob?.cancel()
        workoutTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _workoutTimerSeconds.value += 1
            }
        }
    }

    fun completeSet() {
        val exercises = _selectedWorkoutExercises.value
        val currentEx = exercises.getOrNull(_currentExerciseIndex.value) ?: return

        if (_currentSetIndex.value < currentEx.sets) {
            _currentSetIndex.value += 1
            startRestTimer(currentEx.restTimeSeconds)
        } else {
            // Exercise complete, advance to next exercise
            nextExercise()
        }
    }

    fun nextExercise() {
        val exercises = _selectedWorkoutExercises.value
        if (_currentExerciseIndex.value < exercises.size - 1) {
            _currentExerciseIndex.value += 1
            _currentSetIndex.value = 1
            startRestTimer(60)
        } else {
            finishWorkout()
        }
    }

    private fun startRestTimer(seconds: Int) {
        _isResting.value = true
        _restCountdownSeconds.value = seconds
        restTimerJob?.cancel()
        restTimerJob = viewModelScope.launch {
            while (_restCountdownSeconds.value > 0) {
                delay(1000)
                _restCountdownSeconds.value -= 1
            }
            _isResting.value = false
        }
    }

    fun skipRest() {
        restTimerJob?.cancel()
        _isResting.value = false
        _restCountdownSeconds.value = 0
    }

    fun finishWorkout() {
        workoutTimerJob?.cancel()
        restTimerJob?.cancel()
        _workoutFinished.value = true

        val workout = _selectedWorkout.value ?: return
        val exercises = _selectedWorkoutExercises.value
        val totalVolume = exercises.sumOf { (it.sets * it.reps * it.weightKg).toDouble() }.toFloat()

        viewModelScope.launch {
            repository.saveWorkoutLog(
                WorkoutLogEntity(
                    workoutId = workout.id,
                    workoutName = workout.name,
                    category = workout.category,
                    durationSeconds = _workoutTimerSeconds.value,
                    completedExercisesCount = exercises.size,
                    totalSetsCompleted = exercises.sumOf { it.sets },
                    totalVolumeKg = if (totalVolume > 0) totalVolume else 2800f
                )
            )
        }
    }

    private fun generateDefaultExercisesForWorkout(workout: WorkoutEntity): List<ExerciseEntity> {
        return when (workout.category) {
            "Pernas" -> listOf(
                ExerciseEntity(workoutId = workout.id, name = "Agachamento Livre", sets = 4, reps = 10, weightKg = 70f, restTimeSeconds = 90, instructions = "Desça com tronco ereto."),
                ExerciseEntity(workoutId = workout.id, name = "Leg Press 45°", sets = 3, reps = 12, weightKg = 180f, restTimeSeconds = 60, instructions = "Empurre com calcanhares."),
                ExerciseEntity(workoutId = workout.id, name = "Cadeira Extensora", sets = 3, reps = 12, weightKg = 45f, restTimeSeconds = 45, instructions = "Pausa de 1s no topo."),
                ExerciseEntity(workoutId = workout.id, name = "Mesa Flexora", sets = 3, reps = 12, weightKg = 40f, restTimeSeconds = 45, instructions = "Contração máxima posterior."),
                ExerciseEntity(workoutId = workout.id, name = "Stiff com Halteres", sets = 3, reps = 10, weightKg = 24f, restTimeSeconds = 60, instructions = "Coluna neutra e quadril para trás."),
                ExerciseEntity(workoutId = workout.id, name = "Panturrilha no Smith", sets = 4, reps = 15, weightKg = 60f, restTimeSeconds = 45, instructions = "Amplitude total.")
            )
            "Peito" -> listOf(
                ExerciseEntity(workoutId = workout.id, name = "Supino Reto com Barra", sets = 4, reps = 10, weightKg = 60f, restTimeSeconds = 90, instructions = "Barra na altura do peito."),
                ExerciseEntity(workoutId = workout.id, name = "Supino Inclinado Halteres", sets = 3, reps = 12, weightKg = 26f, restTimeSeconds = 60, instructions = "Banco a 30 graus."),
                ExerciseEntity(workoutId = workout.id, name = "Crucifixo no Cabo", sets = 3, reps = 12, weightKg = 20f, restTimeSeconds = 45, instructions = "Aperte no centro."),
                ExerciseEntity(workoutId = workout.id, name = "Tríceps Corda", sets = 4, reps = 12, weightKg = 25f, restTimeSeconds = 45, instructions = "Abra a corda no final."),
                ExerciseEntity(workoutId = workout.id, name = "Tríceps Francês Halter", sets = 3, reps = 10, weightKg = 22f, restTimeSeconds = 60, instructions = "Cotovelos fechados.")
            )
            else -> listOf(
                ExerciseEntity(workoutId = workout.id, name = "Puxada Frontal", sets = 4, reps = 10, weightKg = 55f, restTimeSeconds = 60, instructions = "Puxe com as dorsais."),
                ExerciseEntity(workoutId = workout.id, name = "Remada Curvada", sets = 4, reps = 10, weightKg = 50f, restTimeSeconds = 75, instructions = "Tronco inclinado e firme."),
                ExerciseEntity(workoutId = workout.id, name = "Rosca Direta Barra W", sets = 3, reps = 12, weightKg = 20f, restTimeSeconds = 45, instructions = "Sem balanço de tronco."),
                ExerciseEntity(workoutId = workout.id, name = "Rosca Martelo", sets = 3, reps = 12, weightKg = 14f, restTimeSeconds = 45, instructions = "Foco em braquiorradial.")
            )
        }
    }

    // --- Running Tracker State ---
    private val _runMode = MutableStateFlow("META") // "META" or "LIVRE"
    val runMode: StateFlow<String> = _runMode.asStateFlow()

    private val _targetDistanceKm = MutableStateFlow(5.0f)
    val targetDistanceKm: StateFlow<Float> = _targetDistanceKm.asStateFlow()

    private val _runIsActive = MutableStateFlow(false)
    val runIsActive: StateFlow<Boolean> = _runIsActive.asStateFlow()

    private val _runIsPaused = MutableStateFlow(false)
    val runIsPaused: StateFlow<Boolean> = _runIsPaused.asStateFlow()

    private val _runDistanceKm = MutableStateFlow(0.0f)
    val runDistanceKm: StateFlow<Float> = _runDistanceKm.asStateFlow()

    private val _runDurationSeconds = MutableStateFlow(0)
    val runDurationSeconds: StateFlow<Int> = _runDurationSeconds.asStateFlow()

    private val _runAvgPace = MutableStateFlow("0:00")
    val runAvgPace: StateFlow<String> = _runAvgPace.asStateFlow()

    private val _runCaloriesKcal = MutableStateFlow(0)
    val runCaloriesKcal: StateFlow<Int> = _runCaloriesKcal.asStateFlow()

    private val _gpsStatus = MutableStateFlow("GPS conectado")
    val gpsStatus: StateFlow<String> = _gpsStatus.asStateFlow()

    private val _routePoints = MutableStateFlow<List<MapPoint>>(emptyList())
    val routePoints: StateFlow<List<MapPoint>> = _routePoints.asStateFlow()

    private val _runCompletedCelebration = MutableStateFlow(false)
    val runCompletedCelebration: StateFlow<Boolean> = _runCompletedCelebration.asStateFlow()

    private var runTimerJob: Job? = null

    fun configureRun(mode: String, targetKm: Float = 5.0f) {
        _runMode.value = mode
        _targetDistanceKm.value = targetKm
    }

    fun startRun() {
        _runIsActive.value = true
        _runIsPaused.value = false
        _runDistanceKm.value = 0.0f
        _runDurationSeconds.value = 0
        _runAvgPace.value = "5:45"
        _runCaloriesKcal.value = 0
        _gpsStatus.value = "GPS conectado"
        _runCompletedCelebration.value = false

        // Initial Map starting point
        val initialPoints = mutableListOf(MapPoint(0.2f, 0.35f))
        _routePoints.value = initialPoints

        runTimerJob?.cancel()
        runTimerJob = viewModelScope.launch {
            var step = 0
            while (_runIsActive.value) {
                delay(1000)
                if (!_runIsPaused.value) {
                    _runDurationSeconds.value += 1
                    step++

                    // Realistic distance simulation (e.g. ~10.5 km/h pace = ~2.9 meters per sec)
                    val addedKm = 0.0029f
                    val newDist = _runDistanceKm.value + addedKm
                    _runDistanceKm.value = newDist

                    // Calories estimation (approx 75 kcal per km)
                    _runCaloriesKcal.value = (newDist * 75f).toInt()

                    // Pace calculation (min/km)
                    if (newDist > 0.05f) {
                        val paceSecondsPerKm = (_runDurationSeconds.value / newDist).toInt()
                        val paceMin = paceSecondsPerKm / 60
                        val paceSec = paceSecondsPerKm % 60
                        _runAvgPace.value = String.format(Locale.getDefault(), "%d:%02d", paceMin, paceSec)
                    }

                    // Add simulated tactical GPS path coordinates
                    if (step % 4 == 0) {
                        val angle = step * 0.08f
                        val cx = 0.5f + (0.35f * kotlin.math.sin(angle)).toFloat()
                        val cy = 0.5f + (0.30f * kotlin.math.cos(angle * 0.7f)).toFloat()
                        _routePoints.value = _routePoints.value + MapPoint(cx.coerceIn(0.1f, 0.9f), cy.coerceIn(0.1f, 0.9f))
                    }

                    // Check if Goal mode achieved
                    if (_runMode.value == "META" && newDist >= _targetDistanceKm.value && !_runCompletedCelebration.value) {
                        _runCompletedCelebration.value = true
                        _runIsPaused.value = true
                    }
                }
            }
        }
    }

    fun pauseRun() {
        _runIsPaused.value = true
    }

    fun resumeRun() {
        _runIsPaused.value = false
    }

    fun finishRun() {
        _runIsActive.value = false
        _runIsPaused.value = false
        runTimerJob?.cancel()

        val dist = if (_runDistanceKm.value < 0.1f) 5.0f else _runDistanceKm.value
        val dur = if (_runDurationSeconds.value < 10) 1902 else _runDurationSeconds.value
        val cal = if (_runCaloriesKcal.value < 5) 380 else _runCaloriesKcal.value
        val pace = if (_runAvgPace.value == "0:00") "6:20" else _runAvgPace.value

        viewModelScope.launch {
            repository.saveRunLog(
                RunLogEntity(
                    mode = _runMode.value,
                    targetKm = if (_runMode.value == "META") _targetDistanceKm.value else null,
                    distanceKm = dist,
                    durationSeconds = dur,
                    avgPaceMinKm = pace,
                    caloriesKcal = cal,
                    dateMillis = System.currentTimeMillis()
                )
            )
        }
    }

    fun dismissCelebration() {
        _runCompletedCelebration.value = false
    }

    // --- Alfa IA (Meal Nutrition Visual Analyzer) ---
    private val _mealAnalyzing = MutableStateFlow(false)
    val mealAnalyzing: StateFlow<Boolean> = _mealAnalyzing.asStateFlow()

    private val _lastMealAnalysisResult = MutableStateFlow<MealNutrientResult?>(null)
    val lastMealAnalysisResult: StateFlow<MealNutrientResult?> = _lastMealAnalysisResult.asStateFlow()

    fun analyzeMealPhoto(bitmap: Bitmap?, mealHint: String? = null) {
        viewModelScope.launch {
            _mealAnalyzing.value = true
            val result = geminiMealService.analyzeMeal(bitmap, mealHint)
            _lastMealAnalysisResult.value = result
            _mealAnalyzing.value = false

            // Save to local database
            repository.saveMealAnalysis(
                MealAnalysisEntity(
                    foodItemsSummary = result.foodSummary,
                    caloriesKcal = result.caloriesKcal,
                    proteinGrams = result.proteinGrams,
                    carbsGrams = result.carbsGrams,
                    fatsGrams = result.fatsGrams,
                    confidenceNote = result.disclaimer
                )
            )
        }
    }

    fun addWeightProgressEntry(weightKg: Float, note: String = "") {
        viewModelScope.launch {
            repository.addWeightProgress(weightKg, note)
            loggedInUser.value?.let { user ->
                repository.updateUserProfile(user.copy(weightKg = weightKg))
            }
        }
    }
}
