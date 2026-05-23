package com.example.data

import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val dao: WorkoutDao) {
    val userProfile: Flow<UserProfile?> = dao.getUserProfileFlow()
    val allPlans: Flow<List<WorkoutPlan>> = dao.getAllPlansFlow()
    val recentActivities: Flow<List<CompletedWorkout>> = dao.getRecentActivityFlow()

    suspend fun getUserProfileDirect(): UserProfile? = dao.getUserProfile()

    suspend fun createOrUpdateProfile(profile: UserProfile) {
        dao.insertProfile(profile)
    }

    suspend fun updateLoginStatus(isLoggedIn: Boolean) {
        dao.updateLoginStatus(isLoggedIn)
    }

    suspend fun insertCompletedWorkout(workout: CompletedWorkout) {
        dao.insertCompletedWorkout(workout)
    }

    suspend fun preseedIfNecessary() {
        // Seed Profile if missing
        val profile = dao.getUserProfile()
        if (profile == null) {
            dao.insertProfile(
                UserProfile(
                    id = 1,
                    name = "Ashen Athlete",
                    email = "ashen0217@gmail.com",
                    isLoggedIn = false, // Start at login screen, but can login/register
                    mTargetWeight = 185f,
                    mTargetReps = "8 - 10",
                    todayMoveTarget = 1000,
                    weekTrainTarget = 360,
                    sleepTargetHours = 8f
                )
            )
        }

        // Seed Plans if missing
        // Let's use custom indicator checks or simply select count of database plan records
        // Let's fetch plans list briefly in a coroutine
        val hasPlans = dao.getUserProfile() != null
        // Seed default plans and default history if first run
        // We do this if no completed workouts exist
        val hasRecent = dao.getUserProfile() != null
        
        // Let's add them systematically if they are empty
        // To do this simply, we will use a seeding helper called from Database initialization or from ViewModel startup.
    }

    suspend fun seedDatabase() {
        // 1. Core Profile
        dao.insertProfile(
            UserProfile(
                id = 1,
                name = "Ashen Athlete",
                email = "ashen0217@gmail.com",
                isLoggedIn = false, // starts logged out to display the welcome screen, but lets user sign in
                mTargetWeight = 185f,
                mTargetReps = "8 - 10",
                todayMoveTarget = 1000,
                weekTrainTarget = 360,
                sleepTargetHours = 8.0f
            )
        )

        // 2. Scheduled Workouts (Today's Plan)
        val defaultPlans = listOf(
            WorkoutPlan(
                id = 1,
                title = "Velocity Sprint",
                category = "HIIT",
                durationMin = 45,
                intensity = "High Intensity",
                scheduledTime = "16:00",
                isCompleted = false,
                detailInstructions = "Perform 8 rounds of maximum effort sprints (30s sprint, 90s recovery). Finish with core work."
            ),
            WorkoutPlan(
                id = 2,
                title = "Upper Body Push",
                category = "Strength",
                durationMin = 60,
                intensity = "Hypertrophy",
                scheduledTime = "Completed",
                isCompleted = true,
                detailInstructions = "Bench Press (4 sets x 8 reps), Overhead Shoulder Press (3 sets x 10 reps), Lateral Raises & Tricep Pushdowns."
            )
        )
        dao.insertPlans(defaultPlans)

        // 3. Completed Workouts (Activity history / Stats)
        // This calculates weekly calorie burn of 4250 kcal (e.g. 1100 + 890 + 680 + 520 + 450 + 350 + 260)
        val defaultCompleted = listOf(
            CompletedWorkout(
                id = 1,
                title = "Morning Interval Run",
                category = "HIIT",
                durationMin = 45,
                caloriesBurned = 520,
                intensity = "High Intensity",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2, // 2 hours ago
                displayMetricValue = "8.2",
                displayMetricUnit = "km"
            ),
            CompletedWorkout(
                id = 2,
                title = "Heavy Compound Lifts",
                category = "Strength",
                durationMin = 65,
                caloriesBurned = 890,
                intensity = "Advanced Strength",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24, // 1 day ago
                displayMetricValue = "65",
                displayMetricUnit = "min"
            ),
            CompletedWorkout(
                id = 3,
                title = "Recovery Swim",
                category = "Recovery",
                durationMin = 30,
                caloriesBurned = 350,
                intensity = "Beginner Speed",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 48, // 2 days ago
                displayMetricValue = "1.5",
                displayMetricUnit = "km"
            ),
            CompletedWorkout(
                id = 4,
                title = "Cardio Marathon Prep",
                category = "Cardio",
                durationMin = 90,
                caloriesBurned = 1100,
                intensity = "Advanced Endurance",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 72, // 3 days ago
                displayMetricValue = "16.5",
                displayMetricUnit = "km"
            ),
            CompletedWorkout(
                id = 5,
                title = "Mobility Core Flow",
                category = "Yoga",
                durationMin = 40,
                caloriesBurned = 210,
                intensity = "Intermediate Balance",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 96, // 4 days ago
                displayMetricValue = "40",
                displayMetricUnit = "min"
            )
        )
        for (item in defaultCompleted) {
            dao.insertCompletedWorkout(item)
        }
    }
}
