package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ExerciseEntity
import com.example.data.model.MealAnalysisEntity
import com.example.data.model.RunLogEntity
import com.example.data.model.UserEntity
import com.example.data.model.WeightProgressEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        WorkoutEntity::class,
        ExerciseEntity::class,
        WorkoutLogEntity::class,
        RunLogEntity::class,
        WeightProgressEntity::class,
        MealAnalysisEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AlfaFitDatabase : RoomDatabase() {
    abstract fun dao(): AlfaFitDao

    companion object {
        @Volatile
        private var INSTANCE: AlfaFitDatabase? = null

        fun getInstance(context: Context): AlfaFitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlfaFitDatabase::class.java,
                    "alfa_fit_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialData(getInstance(context))
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(database: AlfaFitDatabase) {
            val dao = database.dao()

            // Initial demo workouts
            val workouts = listOf(
                WorkoutEntity(
                    id = 1,
                    name = "Pernas Explosivas",
                    category = "Pernas",
                    muscleGroup = "Quadríceps, Isquiotibiais e Panturrilhas",
                    exerciseCount = 6,
                    estimatedDurationMin = 35,
                    level = "Intermediário",
                    description = "Treino focado em força, hipertrofia e estabilidade articular para membros inferiores."
                ),
                WorkoutEntity(
                    id = 2,
                    name = "Peitoral Blindado & Tríceps",
                    category = "Peito",
                    muscleGroup = "Peitoral Maior, Tríceps e Deltoide Anterior",
                    exerciseCount = 5,
                    estimatedDurationMin = 40,
                    level = "Avançado",
                    description = "Alta intensidade com foco no pico de contração e volume progressivo de carga."
                ),
                WorkoutEntity(
                    id = 3,
                    name = "Costas V-Taper & Bíceps",
                    category = "Costas",
                    muscleGroup = "Dorsal, Trapézio, Romboide e Bíceps",
                    exerciseCount = 5,
                    estimatedDurationMin = 42,
                    level = "Intermediário",
                    description = "Foco na largura e densidade das costas com exercícios livres e cabos."
                ),
                WorkoutEntity(
                    id = 4,
                    name = "Ombros 3D (Deltoides)",
                    category = "Ombros",
                    muscleGroup = "Deltoide Lateral, Anterior e Posterior",
                    exerciseCount = 4,
                    estimatedDurationMin = 30,
                    level = "Intermediário",
                    description = "Esculpa ombros largos e simétricos com ênfase em elevações e desenvolvimentos."
                ),
                WorkoutEntity(
                    id = 5,
                    name = "Braços Gigantes (Bíceps & Tríceps)",
                    category = "Bíceps",
                    muscleGroup = "Bíceps Braquial, Braquiorradial e Tríceps",
                    exerciseCount = 6,
                    estimatedDurationMin = 35,
                    level = "Avançado",
                    description = "Superséries intensas para pump e hipertrofia de braços."
                ),
                WorkoutEntity(
                    id = 6,
                    name = "Abdômen de Aço & Core",
                    category = "Abdômen",
                    muscleGroup = "Reto Abdominal, Oblíquos e Core",
                    exerciseCount = 5,
                    estimatedDurationMin = 20,
                    level = "Iniciante",
                    description = "Fortalecimento do core e definição muscular com exercícios no solo e na barra."
                ),
                WorkoutEntity(
                    id = 7,
                    name = "Glúteos Máximos & Posteriores",
                    category = "Glúteos",
                    muscleGroup = "Glúteo Máximo, Médio e Isquiotibiais",
                    exerciseCount = 5,
                    estimatedDurationMin = 38,
                    level = "Intermediário",
                    description = "Foco em elevação pélvica pesada e exercícios de isolamento."
                ),
                WorkoutEntity(
                    id = 8,
                    name = "Full Body Condicionamento",
                    category = "Full Body",
                    muscleGroup = "Corpo Inteiro",
                    exerciseCount = 6,
                    estimatedDurationMin = 45,
                    level = "Avançado",
                    description = "Combinação de exercícios compostos para queima calórica e condicionamento geral."
                ),
                WorkoutEntity(
                    id = 9,
                    name = "Cardio HIIT Queima Rápida",
                    category = "Cardio",
                    muscleGroup = "Sistema Cardiovascular e Respiratório",
                    exerciseCount = 4,
                    estimatedDurationMin = 25,
                    level = "Todos os níveis",
                    description = "Intervalos de alta intensidade para acelerar o metabolismo e resistência."
                ),
                WorkoutEntity(
                    id = 10,
                    name = "Tríceps Ferradura",
                    category = "Tríceps",
                    muscleGroup = "Tríceps Cabeça Longa, Lateral e Medial",
                    exerciseCount = 4,
                    estimatedDurationMin = 28,
                    level = "Intermediário",
                    description = "Isolamento total com pegada supinada e pronada na polia e halteres."
                )
            )
            dao.insertWorkouts(workouts)

            // Initial Exercises for Legs (Workout 1)
            val legExercises = listOf(
                ExerciseEntity(
                    workoutId = 1,
                    name = "Agachamento Livre",
                    sets = 4,
                    reps = 10,
                    weightKg = 70f,
                    restTimeSeconds = 90,
                    instructions = "Pés na largura dos ombros, desça mantendo a coluna alinhada e joelhos apontando na direção dos pés.",
                    targetMuscle = "Quadríceps e Glúteos",
                    orderIndex = 0
                ),
                ExerciseEntity(
                    workoutId = 1,
                    name = "Leg Press 45°",
                    sets = 3,
                    reps = 12,
                    weightKg = 180f,
                    restTimeSeconds = 60,
                    instructions = "Apoie os pés no centro da plataforma, empurre com os calcanhares sem travar os joelhos no topo.",
                    targetMuscle = "Quadríceps e Glúteos",
                    orderIndex = 1
                ),
                ExerciseEntity(
                    workoutId = 1,
                    name = "Cadeira Extensora",
                    sets = 3,
                    reps = 12,
                    weightKg = 45f,
                    restTimeSeconds = 45,
                    instructions = "Contraia forte no topo por 1 segundo e desça de forma controlada.",
                    targetMuscle = "Quadríceps Isolado",
                    orderIndex = 2
                ),
                ExerciseEntity(
                    workoutId = 1,
                    name = "Mesa Flexora",
                    sets = 3,
                    reps = 12,
                    weightKg = 40f,
                    restTimeSeconds = 45,
                    instructions = "Flexione os joelhos trazendo o rolo em direção aos glúteos sem levantar o quadril do banco.",
                    targetMuscle = "Isquiotibiais",
                    orderIndex = 3
                ),
                ExerciseEntity(
                    workoutId = 1,
                    name = "Stiff com Halteres",
                    sets = 3,
                    reps = 10,
                    weightKg = 24f,
                    restTimeSeconds = 60,
                    instructions = "Mantenha as pernas semi-flexionadas, quadril projetado para trás e coluna reta.",
                    targetMuscle = "Posterior e Glúteo",
                    orderIndex = 4
                ),
                ExerciseEntity(
                    workoutId = 1,
                    name = "Panturrilha no Smith",
                    sets = 4,
                    reps = 15,
                    weightKg = 60f,
                    restTimeSeconds = 45,
                    instructions = "Amplitude máxima na subida e descida com pausa de 2 segundos na contração máxima.",
                    targetMuscle = "Panturrilhas",
                    orderIndex = 5
                )
            )
            dao.insertExercises(legExercises)

            // Exercises for Chest (Workout 2)
            val chestExercises = listOf(
                ExerciseEntity(
                    workoutId = 2,
                    name = "Supino Reto com Barra",
                    sets = 4,
                    reps = 10,
                    weightKg = 60f,
                    restTimeSeconds = 90,
                    instructions = "Escápulas retratadas, desça a barra até a linha dos mamilos e empurre com potência.",
                    targetMuscle = "Peitoral Maior",
                    orderIndex = 0
                ),
                ExerciseEntity(
                    workoutId = 2,
                    name = "Supino Inclinado Halteres",
                    sets = 3,
                    reps = 12,
                    weightKg = 26f,
                    restTimeSeconds = 60,
                    instructions = "Banco a 30 graus para focar na porção clavicular do peitoral.",
                    targetMuscle = "Peitoral Superior",
                    orderIndex = 1
                ),
                ExerciseEntity(
                    workoutId = 2,
                    name = "Crucifixo na Polia (Crossover)",
                    sets = 3,
                    reps = 12,
                    weightKg = 20f,
                    restTimeSeconds = 45,
                    instructions = "Cotovelos levemente flexionados, abrace no centro apertando o peito.",
                    targetMuscle = "Peitoral Esternal",
                    orderIndex = 2
                ),
                ExerciseEntity(
                    workoutId = 2,
                    name = "Tríceps Corda na Polia",
                    sets = 4,
                    reps = 12,
                    weightKg = 25f,
                    restTimeSeconds = 45,
                    instructions = "Abra a corda no final do movimento para contração máxima da cabeça lateral.",
                    targetMuscle = "Tríceps",
                    orderIndex = 3
                ),
                ExerciseEntity(
                    workoutId = 2,
                    name = "Tríceps Francês Halter",
                    sets = 3,
                    reps = 10,
                    weightKg = 22f,
                    restTimeSeconds = 60,
                    instructions = "Mantenha os cotovelos fechados apontando para frente.",
                    targetMuscle = "Tríceps Cabeça Longa",
                    orderIndex = 4
                )
            )
            dao.insertExercises(chestExercises)

            // Initial Weight progress samples
            val now = System.currentTimeMillis()
            val dayMillis = 24 * 60 * 60 * 1000L
            val weightEntries = listOf(
                WeightProgressEntity(weightKg = 74.7f, dateMillis = now - (30 * dayMillis), note = "Início do protocolo"),
                WeightProgressEntity(weightKg = 74.0f, dateMillis = now - (23 * dayMillis), note = "Ajuste na dieta"),
                WeightProgressEntity(weightKg = 73.5f, dateMillis = now - (16 * dayMillis), note = "Mais consistência no cardio"),
                WeightProgressEntity(weightKg = 72.9f, dateMillis = now - (9 * dayMillis), note = "Evolução de cargas"),
                WeightProgressEntity(weightKg = 72.4f, dateMillis = now - (2 * dayMillis), note = "Peso atual")
            )
            weightEntries.forEach { dao.insertWeightProgress(it) }

            // Pre-seed some workout & run logs so progress and home show real stats immediately
            val initialWorkoutsCount = 28
            for (i in 1..initialWorkoutsCount) {
                dao.insertWorkoutLog(
                    WorkoutLogEntity(
                        workoutId = if (i % 2 == 0) 1 else 2,
                        workoutName = if (i % 2 == 0) "Pernas Explosivas" else "Peitoral Blindado & Tríceps",
                        category = if (i % 2 == 0) "Pernas" else "Peito",
                        dateMillis = now - (i * dayMillis),
                        durationSeconds = 2100 + (i * 30),
                        completedExercisesCount = 6,
                        totalSetsCompleted = 18,
                        totalVolumeKg = 3400f + (i * 50)
                    )
                )
            }

            // Runs total around 42.8 km
            val initialRuns = listOf(
                RunLogEntity(
                    mode = "META",
                    targetKm = 5f,
                    distanceKm = 5.2f,
                    durationSeconds = 1850,
                    avgPaceMinKm = "5:55",
                    caloriesKcal = 390,
                    dateMillis = now - (3 * dayMillis)
                ),
                RunLogEntity(
                    mode = "LIVRE",
                    targetKm = null,
                    distanceKm = 10.0f,
                    durationSeconds = 3600,
                    avgPaceMinKm = "6:00",
                    caloriesKcal = 780,
                    dateMillis = now - (7 * dayMillis)
                ),
                RunLogEntity(
                    mode = "META",
                    targetKm = 8f,
                    distanceKm = 8.4f,
                    durationSeconds = 3010,
                    avgPaceMinKm = "5:58",
                    caloriesKcal = 640,
                    dateMillis = now - (12 * dayMillis)
                ),
                RunLogEntity(
                    mode = "LIVRE",
                    targetKm = null,
                    distanceKm = 19.2f,
                    durationSeconds = 6900,
                    avgPaceMinKm = "5:59",
                    caloriesKcal = 1450,
                    dateMillis = now - (20 * dayMillis)
                )
            )
            initialRuns.forEach { dao.insertRunLog(it) }

            // Pre-seed an initial meal analysis
            dao.insertMealAnalysis(
                MealAnalysisEntity(
                    foodItemsSummary = "Frango grelhado, Arroz branco, Brócolis e Cenoura",
                    caloriesKcal = 520,
                    proteinGrams = 42,
                    carbsGrams = 48,
                    fatsGrams = 15,
                    dateMillis = now - (4 * 3600 * 1000L)
                )
            )
        }
    }
}
