package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val birthDate: String = "15/05/1998",
    val heightCm: Float = 178f,
    val weightKg: Float = 72.4f,
    val goal: String = "Ganhar massa muscular",
    val photoUri: String? = null,
    val isLoggedIn: Boolean = true,
    val createdAtMillis: Long = System.currentTimeMillis()
)
