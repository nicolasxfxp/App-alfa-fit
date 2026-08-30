package com.example.data.repository

import com.example.data.local.AlfaFitDao
import com.example.data.model.ExerciseEntity
import com.example.data.model.MealAnalysisEntity
import com.example.data.model.RunLogEntity
import com.example.data.model.UserEntity
import com.example.data.model.WeightProgressEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

class AlfaFitRepository(private val dao: AlfaFitDao) {

    val loggedInUser: Flow<UserEntity?> = dao.getLoggedInUser()
    val allWorkouts: Flow<List<WorkoutEntity>> = dao.getAllWorkouts()
    val allWorkoutLogs: Flow<List<WorkoutLogEntity>> = dao.getAllWorkoutLogs()
    val allRunLogs: Flow<List<RunLogEntity>> = dao.getAllRunLogs()
    val allWeightProgress: Flow<List<WeightProgressEntity>> = dao.getAllWeightProgress()
    val allMealAnalyses: Flow<List<MealAnalysisEntity>> = dao.getAllMealAnalyses()

    val totalWorkoutsCount: Flow<Int> = dao.getWorkoutCount()
    val totalRunDistance: Flow<Float?> = dao.getTotalRunDistance()
    val totalWorkoutDurationSeconds: Flow<Int?> = dao.getTotalWorkoutDurationSeconds()
    val totalRunDurationSeconds: Flow<Int?> = dao.getTotalRunDurationSeconds()

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun authenticate(email: String, passwordRaw: String): Result<UserEntity> {
        val user = dao.getUserByEmail(email.trim().lowercase())
            ?: return Result.failure(Exception("Usuário não encontrado com este e-mail"))

        val hash = hashPassword(passwordRaw)
        if (user.passwordHash != hash) {
            return Result.failure(Exception("Senha incorreta"))
        }

        dao.logoutAllUsers()
        dao.setLoggedInUser(user.id)
        return Result.success(user.copy(isLoggedIn = true))
    }

    suspend fun registerUser(
        name: String,
        email: String,
        passwordRaw: String,
        birthDate: String,
        heightCm: Float,
        weightKg: Float,
        goal: String
    ): Result<UserEntity> {
        val normalizedEmail = email.trim().lowercase()
        val existing = dao.getUserByEmail(normalizedEmail)
        if (existing != null) {
            return Result.failure(Exception("Já existe uma conta com este e-mail"))
        }

        dao.logoutAllUsers()
        val newUser = UserEntity(
            name = name.trim(),
            email = normalizedEmail,
            passwordHash = hashPassword(passwordRaw),
            birthDate = birthDate,
            heightCm = heightCm,
            weightKg = weightKg,
            goal = goal,
            isLoggedIn = true
        )
        val newId = dao.insertUser(newUser)
        val created = newUser.copy(id = newId)

        // Add first weight entry
        dao.insertWeightProgress(
            WeightProgressEntity(
                userId = newId,
                weightKg = weightKg,
                dateMillis = System.currentTimeMillis(),
                note = "Cadastro inicial"
            )
        )

        return Result.success(created)
    }

    suspend fun signInWithGoogleDemo(googleAccountName: String, googleEmail: String): UserEntity {
        dao.logoutAllUsers()
        val existing = dao.getUserByEmail(googleEmail.trim().lowercase())
        if (existing != null) {
            dao.setLoggedInUser(existing.id)
            return existing.copy(isLoggedIn = true)
        }
        val newUser = UserEntity(
            name = googleAccountName,
            email = googleEmail.trim().lowercase(),
            passwordHash = hashPassword("google_oauth_auth_token_secure"),
            birthDate = "10/10/1997",
            heightCm = 176f,
            weightKg = 72.4f,
            goal = "Ganhar massa muscular",
            isLoggedIn = true
        )
        val id = dao.insertUser(newUser)
        return newUser.copy(id = id)
    }

    suspend fun logout() {
        dao.logoutAllUsers()
    }

    suspend fun updateUserProfile(user: UserEntity) {
        dao.updateUser(user)
    }

    fun getWorkoutsByCategory(category: String): Flow<List<WorkoutEntity>> {
        return dao.getWorkoutsByCategory(category)
    }

    suspend fun getWorkoutById(id: Long): WorkoutEntity? {
        return dao.getWorkoutById(id)
    }

    fun getExercisesForWorkout(workoutId: Long): Flow<List<ExerciseEntity>> {
        return dao.getExercisesForWorkout(workoutId)
    }

    suspend fun getExercisesListForWorkout(workoutId: Long): List<ExerciseEntity> {
        return dao.getExercisesListForWorkout(workoutId)
    }

    suspend fun saveWorkoutLog(log: WorkoutLogEntity): Long {
        return dao.insertWorkoutLog(log)
    }

    suspend fun saveRunLog(runLog: RunLogEntity): Long {
        return dao.insertRunLog(runLog)
    }

    suspend fun addWeightProgress(weightKg: Float, note: String = ""): Long {
        return dao.insertWeightProgress(
            WeightProgressEntity(
                weightKg = weightKg,
                dateMillis = System.currentTimeMillis(),
                note = note
            )
        )
    }

    suspend fun saveMealAnalysis(analysis: MealAnalysisEntity): Long {
        return dao.insertMealAnalysis(analysis)
    }
}
