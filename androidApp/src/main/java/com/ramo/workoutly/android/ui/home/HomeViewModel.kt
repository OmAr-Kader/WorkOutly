package com.ramo.workoutly.android.ui.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ramo.workoutly.android.data.health.HealthKitManager
import com.ramo.workoutly.android.global.base.darken
import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.data.model.tempExercises
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
import com.ramo.workoutly.global.base.HEART_RATE
import com.ramo.workoutly.global.base.METABOLIC_RATE
import com.ramo.workoutly.global.base.SLEEP
import com.ramo.workoutly.global.base.STEPS
import com.ramo.workoutly.global.util.averageSafeDouble
import com.ramo.workoutly.global.util.averageSafeLong
import com.ramo.workoutly.global.util.formatMillisecondsToHours
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Duration

class HomeViewModel(project: Project, val healthKit: HealthKitManager) : BaseViewModel(project) {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    fun loadData(isDarkMode: Boolean, permission: () -> Unit) {
        //setIsProcess(true)
        launchBack {
            healthKit.requestPermissions({
                val steps = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.StepsRecord::class)
                val distance = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.DistanceRecord::class)
                val caloriesBurned =
                    healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.ActiveCaloriesBurnedRecord::class)
                val metabolicRate = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.BasalMetabolicRateRecord::class)
                val heartRate = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.HeartRateRecord::class)
                val sleep = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.SleepSessionRecord::class)
                val stepsMetric = FitnessMetric(
                    STEPS,
                    "Steps",
                    Color(29, 108, 245, 255).let { if (isDarkMode) it else it.darken(0.3F) }.toArgb().toLong(),
                    steps.sumOf { it.count },
                    ""
                )
                val distanceMetric = FitnessMetric(
                    DISTANCE,
                    "Distance",
                    Color.Green.let { if (isDarkMode) it else it.darken(0.3F) }.toArgb().toLong(),
                    distance.sumOf { it.distance.inMeters }.toLong(),
                    " m"
                )
                val caloriesBurnedMetric = FitnessMetric(
                    CALORIES_BURNED,
                    "Calories",
                    Color.Yellow.let { if (isDarkMode) it else it.darken(0.3F) }.toArgb().toLong(),
                    caloriesBurned.sumOf { it.energy.inCalories }.toLong(),
                    " cal"
                )
                val metabolicRateMetric = FitnessMetric(
                    METABOLIC_RATE,
                    "Metabolic Rate",
                    Color(245, 73, 29, 255).let { if (isDarkMode) it else it.darken(0.3F) }.toArgb().toLong(),
                    metabolicRate.map { it.basalMetabolicRate.inKilocaloriesPerDay }.averageSafeDouble().toLong(),
                    " kcal/day"
                )
                val heartRateMetric = FitnessMetric(
                    HEART_RATE,
                    "Heart Rate",
                    Color.Red.let { if (isDarkMode) it else it.darken(0.3F) }.toArgb().toLong(),
                    heartRate.map { it.samples.map { i -> i.beatsPerMinute }.averageSafeLong() }.averageSafeDouble().toLong(),
                    " kcal/day"
                )
                val sleepMetric = FitnessMetric(
                    SLEEP,
                    "Sleep",
                    Color(28, 199, 139, 255).darken(0.2F).let { if (isDarkMode) it else it.darken(0.3F) }.toArgb().toLong(),
                    0,
                    "",
                    valueStr = sleep.map { it.stages.map { i -> Duration.between(i.endTime, i.startTime).toMillis() }.averageSafeLong() }
                        .averageSafeDouble().toLong().formatMillisecondsToHours
                )
                _uiState.update { state ->
                    state.copy(
                        metrics = listOf(stepsMetric, distanceMetric, caloriesBurnedMetric, metabolicRateMetric, heartRateMetric, sleepMetric),
                        exercises = tempExercises,
                        isProcess = false
                    )
                }
            }, permission)
        }
    }

    private fun setIsProcess(@Suppress("SameParameterValue") it: Boolean) {
        _uiState.update { state ->
            state.copy(isProcess = it)
        }
    }

    data class State(
        val metrics: List<FitnessMetric> = emptyList(),
        val exercises: List<Exercise> = emptyList(),
        val isProcess: Boolean = true,
    )
}
