package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // Peito, Costas, Ombros, Bíceps, Tríceps, Pernas, Glúteos, Abdômen, Full Body, Cardio
    val muscleGroup: String,
    val exerciseCount: Int,
    val estimatedDurationMin: Int,
    val level: String = "Intermediário", // Iniciante, Intermediário, Avançado
    val description: String = "",
    val imageDrawableName: String = "img_workout_hero"
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long,
    val name: String,
    val sets: Int = 3,
    val reps: Int = 12,
    val weightKg: Float = 20f,
    val restTimeSeconds: Int = 60,
    val instructions: String = "",
    val targetMuscle: String = "",
    val orderIndex: Int = 0
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 1,
    val workoutId: Long,
    val workoutName: String,
    val category: String,
    val dateMillis: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val completedExercisesCount: Int,
    val totalSetsCompleted: Int,
    val totalVolumeKg: Float
)

@Entity(tableName = "run_logs")
data class RunLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 1,
    val mode: String, // "META" or "LIVRE"
    val targetKm: Float? = null,
    val distanceKm: Float,
    val durationSeconds: Int,
    val avgPaceMinKm: String,
    val caloriesKcal: Int,
    val dateMillis: Long = System.currentTimeMillis(),
    val polylineCoordinatesJson: String = ""
)

@Entity(tableName = "weight_progress")
data class WeightProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 1,
    val weightKg: Float,
    val dateMillis: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "meal_analyses")
data class MealAnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 1,
    val dateMillis: Long = System.currentTimeMillis(),
    val imageUri: String? = null,
    val foodItemsSummary: String, // e.g. "Frango grelhado, Arroz branco, Brócolis"
    val caloriesKcal: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatsGrams: Int,
    val confidenceNote: String = "Os valores são estimativas e podem não ser exatos."
)
