package com.ramo.workoutly.android.ui.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ramo.workoutly.android.data.health.HealthKitManager
import com.ramo.workoutly.android.global.base.darken
import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.model.PreferenceData
import com.ramo.workoutly.data.model.UserPref
import com.ramo.workoutly.data.model.messages
import com.ramo.workoutly.data.model.tempExercises
import com.ramo.workoutly.data.util.messagesFilter
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
import com.ramo.workoutly.global.base.HEART_RATE
import com.ramo.workoutly.global.base.METABOLIC_RATE
import com.ramo.workoutly.global.base.PREF_DAYS_COUNT
import com.ramo.workoutly.global.base.PREF_SORT_BY
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

    fun loadData(userPref: UserPref, days: Int, isDarkMode: Boolean, permission: () -> Unit) {
        //setIsProcess(true)
        launchBack {
            healthKit.requestPermissions({
                loadMetrics(days, isDarkMode).also { metrics ->
                    _uiState.update { state ->
                        state.copy(
                            metrics = metrics,
                            exercises = tempExercises,
                            days = days,
                            messages = messages.messagesFilter(userPref.id),
                            isProcess = false
                        )
                    }
                }
            }, permission)
        }
    }

    private suspend fun loadMetrics(days: Int, isDarkMode: Boolean) = kotlinx.coroutines.coroutineScope {
        val steps = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.StepsRecord::class, days)
        val distance = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.DistanceRecord::class, days)
        val cb = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.ActiveCaloriesBurnedRecord::class, days)
        val metabolicRate = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.BasalMetabolicRateRecord::class, days)
        val heartRate = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.HeartRateRecord::class, days)
        val sleep = healthKit.fetchHealthDataForLastThreeDays(androidx.health.connect.client.records.SleepSessionRecord::class, days)
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
            cb.sumOf { it.energy.inKilocalories }.toLong(),
            " kcal"
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
            " bpm"
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
        return@coroutineScope listOf(stepsMetric, distanceMetric, caloriesBurnedMetric, metabolicRateMetric, heartRateMetric, sleepMetric)
    }

    private fun reLoadData(days: Int, isDarkMode: Boolean) {
        launchBack {
            loadMetrics(days, isDarkMode).also { metrics ->
                _uiState.update { state ->
                    state.copy(
                        metrics = metrics,
                        isProcess = false
                    )
                }
            }
        }
    }

    fun setFilterDays(isDarkMode: Boolean, it: Int) {
        _uiState.update { state ->
            state.copy(days = it)
        }
        launchBack {
            project.pref.updatePref(listOf(PreferenceData().copy(keyString = PREF_DAYS_COUNT, value = it.toString())))
        }
        reLoadData(days = it, isDarkMode =  isDarkMode)
    }

    fun setSortBy(it: Int) {
        _uiState.update { state ->
            state.copy(sortBy = it)
        }
        launchBack {
            project.pref.updatePref(listOf(PreferenceData().copy(keyString = PREF_SORT_BY, value = it.toString())))
        }
        // @OmAr-Kader IMPLEMENTATION
    }

    fun setIsLiveVisible(it: Boolean) {
        _uiState.update { state ->
            state.copy(isLiveVisible = it)
        }
    }

    fun setFile(url: android.net.Uri, type: Int) {

    }

    private fun setIsProcess(@Suppress("SameParameterValue") it: Boolean) {
        _uiState.update { state ->
            state.copy(isProcess = it)
        }
    }

    data class State(
        val metrics: List<FitnessMetric> = emptyList(),
        val exercises: List<Exercise> = emptyList(),
        val messages: List<Message> = emptyList(),
        val days: Int = 3,
        val sortBy: Int = 1,
        val chatText: String = "",
        val isLiveVisible: Boolean = false,
        val isPickerVisible: Boolean = false,
        val isProcess: Boolean = true,
    )
}
