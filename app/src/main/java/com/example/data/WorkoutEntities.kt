package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val isLoggedIn: Boolean = false,
    val mTargetWeight: Float = 185f,
    val mTargetReps: String = "8 - 10",
    val todayMoveTarget: Int = 1000, // KCAL Target
    val weekTrainTarget: Int = 300, // Minutes Target
    val sleepTargetHours: Float = 8f
)

@Entity(tableName = "workout_plans")
data class WorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "HIIT", "Strength", "Cardio", "Yoga"
    val durationMin: Int,
    val intensity: String, // "High Intensity", "Hypertrophy", "Intermediate", etc.
    val scheduledTime: String, // e.g. "16:00" or "Completed"
    val isCompleted: Boolean = false,
    val detailInstructions: String = "Perform 4 sets of compound movements. Stay hydrated!"
)

@Entity(tableName = "completed_workouts")
data class CompletedWorkout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val durationMin: Int,
    val caloriesBurned: Int,
    val intensity: String,
    val timestamp: Long = System.currentTimeMillis(),
    val displayMetricValue: String, // e.g., "8.2", "65", "1.5"
    val displayMetricUnit: String // e.g., "km", "min", "km"
)
