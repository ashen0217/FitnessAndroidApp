package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    val repository = WorkoutRepository(database.workoutDao())

    // UI Navigation & tab control states
    private val _currentTab = MutableStateFlow(0) // 0 = Home, 1 = Workouts, 2 = Active, 3 = Stats, 4 = Profile
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Screen overrides (Auth states)
    private val _isRegistering = MutableStateFlow(false)
    val isRegistering: StateFlow<Boolean> = _isRegistering.asStateFlow()

    // Filter library search states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All Workouts")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Active Tracking Workout State
    private val _activeWorkoutTitle = MutableStateFlow("Bench Press")
    val activeWorkoutTitle: StateFlow<String> = _activeWorkoutTitle.asStateFlow()

    private val _activeWorkoutCategory = MutableStateFlow("Strength")
    val activeWorkoutCategory: StateFlow<String> = _activeWorkoutCategory.asStateFlow()

    private val _targetWeight = MutableStateFlow("185 lbs")
    val targetWeight: StateFlow<String> = _targetWeight.asStateFlow()

    private val _targetReps = MutableStateFlow("8 - 10")
    val targetReps: StateFlow<String> = _targetReps.asStateFlow()

    private val _activeSet = MutableStateFlow("Set 2/4")
    val activeSet: StateFlow<String> = _activeSet.asStateFlow()

    // Real-time ticking parameters
    private val _timerSeconds = MutableStateFlow(2722) // starts at 45m 22s matching screenshot (2722 seconds)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(true)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _liveHeartRate = MutableStateFlow(154) // starts at 154 BPM matching screenshot
    val liveHeartRate: StateFlow<Int> = _liveHeartRate.asStateFlow()

    private val _liveCaloriesBurned = MutableStateFlow(412) // starts at 412 kcal matching screenshot
    val liveCaloriesBurned: StateFlow<Int> = _liveCaloriesBurned.asStateFlow()

    // Local flow binders
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allPlans: StateFlow<List<WorkoutPlan>> = repository.allPlans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentActivities: StateFlow<List<CompletedWorkout>> = repository.recentActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var timerJob: Job? = null
    private var heartRateJob: Job? = null

    init {
        viewModelScope.launch {
            // Seed DB on first open to show mock-free active statistics
            val existing = repository.getUserProfileDirect()
            if (existing == null) {
                repository.seedDatabase()
            }
            startActiveBackgroundCalculations()
        }
    }

    private fun startActiveBackgroundCalculations() {
        // Ticking stopwatch coroutine
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_isTimerRunning.value) {
                    _timerSeconds.update { it + 1 }
                    // burn calories slowly during workout (e.g. 1 kcal every 5 seconds)
                    if (_timerSeconds.value % 5 == 0) {
                        _liveCaloriesBurned.update { it + 1 }
                    }
                }
            }
        }

        // Live heart-rate fluctuations to simulate activity sensor
        heartRateJob?.cancel()
        heartRateJob = viewModelScope.launch {
            while (true) {
                delay(3000)
                val base = if (_currentTab.value == 2) 150 else 120 // higher during active screen
                _liveHeartRate.update { base + Random.nextInt(-6, 7) }
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    fun setRegistering(registering: Boolean) {
        _isRegistering.value = registering
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Active screen controls
    fun toggleTimer() {
        _isTimerRunning.update { !it }
    }

    fun startWorkoutSession(title: String, category: String, instructions: String = "") {
        _activeWorkoutTitle.value = title
        _activeWorkoutCategory.value = category
        // Adjust specs depending on name
        when (category) {
            "HIIT" -> {
                _targetWeight.value = "Body weight"
                _targetReps.value = "High Intensity intervals"
                _activeSet.value = "Interval 3/8"
            }
            "Yoga" -> {
                _targetWeight.value = "Stretching Focus"
                _targetReps.value = "Breathing flow"
                _activeSet.value = "Part 1/3"
            }
            "Cardio" -> {
                _targetWeight.value = "Incline: 1.5"
                _targetReps.value = "Speed: 10.5 km/h"
                _activeSet.value = "Mile 2/4"
            }
            else -> {
                _targetWeight.value = "185 lbs"
                _targetReps.value = "8 - 10"
                _activeSet.value = "Set 2/4"
            }
        }
        _timerSeconds.value = 0
        _liveCaloriesBurned.value = 0
        _isTimerRunning.value = true
        _currentTab.value = 2 // Switch tab to Active
    }

    fun finishCurrentWorkout() {
        viewModelScope.launch {
            val title = _activeWorkoutTitle.value
            val category = _activeWorkoutCategory.value
            val seconds = _timerSeconds.value
            val durationMin = if (seconds > 0) (seconds / 60) else 45
            val calories = _liveCaloriesBurned.value

            val metricValue = when (category) {
                "HIIT", "Cardio" -> "%.1f".format((seconds.toFloat() / 3600f) * 11.2f) // km running
                "Yoga" -> "${durationMin}"
                else -> "${durationMin}"
            }
            val metricUnit = when (category) {
                "HIIT", "Cardio" -> "km"
                "Yoga" -> "min"
                else -> "min"
            }

            val log = CompletedWorkout(
                title = title,
                category = category,
                durationMin = if (durationMin > 0) durationMin else 1,
                caloriesBurned = if (calories > 0) calories else 320,
                intensity = "Active Interval",
                timestamp = System.currentTimeMillis(),
                displayMetricValue = metricValue,
                displayMetricUnit = metricUnit
            )
            repository.insertCompletedWorkout(log)

            // Switch to Stats / Insights tab
            _currentTab.value = 3
        }
    }

    // Settings adjustments / user profiles login simulated flow
    fun loginUser(email: String, name: String) {
        viewModelScope.launch {
            val current = repository.getUserProfileDirect() ?: UserProfile(name = name, email = email)
            val updated = current.copy(
                isLoggedIn = true,
                name = if (name.isNotBlank()) name else current.name,
                email = if (email.isNotBlank()) email else current.email
            )
            repository.createOrUpdateProfile(updated)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.updateLoginStatus(false)
            _currentTab.value = 0
        }
    }

    fun updateProfileTargets(targetWeight: Float, targetRepsSpec: String) {
        viewModelScope.launch {
            val current = repository.getUserProfileDirect()
            if (current != null) {
                val updated = current.copy(
                    mTargetWeight = targetWeight,
                    mTargetReps = targetRepsSpec
                )
                repository.createOrUpdateProfile(updated)
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            database.workoutDao().clearHistory()
        }
    }

    fun triggerSeeding() {
        viewModelScope.launch {
            repository.seedDatabase()
            val profile = repository.getUserProfileDirect()
            if (profile != null) {
                repository.createOrUpdateProfile(profile.copy(isLoggedIn = true))
            }
        }
    }
}
