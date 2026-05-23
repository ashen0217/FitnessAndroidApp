package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Profiling queries
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET isLoggedIn = :isLoggedIn WHERE id = 1")
    suspend fun updateLoginStatus(isLoggedIn: Boolean)

    // Plans queries
    @Query("SELECT * FROM workout_plans ORDER BY id ASC")
    fun getAllPlansFlow(): Flow<List<WorkoutPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: WorkoutPlan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlans(plans: List<WorkoutPlan>)

    @Query("UPDATE workout_plans SET isCompleted = 1, scheduledTime = 'Completed' WHERE id = :planId")
    suspend fun markPlanCompleted(planId: Int)

    // Completed Workouts queries
    @Query("SELECT * FROM completed_workouts ORDER BY timestamp DESC")
    fun getRecentActivityFlow(): Flow<List<CompletedWorkout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedWorkout(workout: CompletedWorkout)

    @Query("DELETE FROM completed_workouts")
    suspend fun clearHistory()
}
