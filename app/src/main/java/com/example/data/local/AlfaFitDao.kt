package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ExerciseEntity
import com.example.data.model.MealAnalysisEntity
import com.example.data.model.RunLogEntity
import com.example.data.model.UserEntity
import com.example.data.model.WeightProgressEntity
import com.example.data.model.WorkoutEntity
import com.example.data.model.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlfaFitDao {

    // User Operations
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()

    @Query("UPDATE users SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun setLoggedInUser(userId: Long)

    // Workout Operations
    @Query("SELECT * FROM workouts ORDER BY id ASC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE category = :category ORDER BY id ASC")
    fun getWorkoutsByCategory(category: String): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id LIMIT 1")
    suspend fun getWorkoutById(id: Long): WorkoutEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    // Exercise Operations
    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId ORDER BY orderIndex ASC")
    fun getExercisesForWorkout(workoutId: Long): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId ORDER BY orderIndex ASC")
    suspend fun getExercisesListForWorkout(workoutId: Long): List<ExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    // Workout Logs
    @Query("SELECT * FROM workout_logs ORDER BY dateMillis DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLogEntity>>

    @Query("SELECT COUNT(*) FROM workout_logs")
    fun getWorkoutCount(): Flow<Int>

    @Query("SELECT SUM(durationSeconds) FROM workout_logs")
    fun getTotalWorkoutDurationSeconds(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLogEntity): Long

    // Run Logs
    @Query("SELECT * FROM run_logs ORDER BY dateMillis DESC")
    fun getAllRunLogs(): Flow<List<RunLogEntity>>

    @Query("SELECT COUNT(*) FROM run_logs")
    fun getRunCount(): Flow<Int>

    @Query("SELECT SUM(distanceKm) FROM run_logs")
    fun getTotalRunDistance(): Flow<Float?>

    @Query("SELECT SUM(durationSeconds) FROM run_logs")
    fun getTotalRunDurationSeconds(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunLog(run: RunLogEntity): Long

    // Weight Progress
    @Query("SELECT * FROM weight_progress ORDER BY dateMillis ASC")
    fun getAllWeightProgress(): Flow<List<WeightProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightProgress(progress: WeightProgressEntity): Long

    // Meal Analyses (Alfa IA)
    @Query("SELECT * FROM meal_analyses ORDER BY dateMillis DESC")
    fun getAllMealAnalyses(): Flow<List<MealAnalysisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealAnalysis(analysis: MealAnalysisEntity): Long
}
