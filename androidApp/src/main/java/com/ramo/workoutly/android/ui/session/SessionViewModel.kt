package com.ramo.workoutly.android.ui.session

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.health.connect.client.records.StepsRecord
import com.ramo.workoutly.android.data.health.HealthKitManager
import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.data.model.FitnessHistoryMetric
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.data.util.regenerateHistories
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.base.STEPS
import com.ramo.workoutly.global.util.toTimestampWithDays
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SessionViewModel(project: Project, private val healthKit: HealthKitManager) : BaseViewModel(project) {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    fun loadData(metric: FitnessMetric, days: Int) {
        _uiState.update { state ->
            state.copy(session = metric)
        }
        launchBack {
            when (metric.id) {
                STEPS -> {
                    loadStepsData(days)
                }
            }
        }
    }

    private suspend fun loadStepsData(days: Int) {
        healthKit.fetchHealthDataForLastThreeDays(StepsRecord::class, days).also { histories ->
            histories.map { step ->
                step.metadata.lastModifiedTime.toEpochMilli().let {
                    FitnessHistoryMetric(it, it.toTimestampWithDays, step.count, "")
                }
            }.regenerateHistories().also { history ->
                _uiState.update { state ->
                    state.copy(sessionHistories = history)
                }
            }
        }
    }

    private fun setIsProcess(it: Boolean) {
        _uiState.update { state ->
            state.copy(isProcess = it)
        }
    }

    data class State(
        val session: FitnessMetric = FitnessMetric(0,  "", Color.Transparent.toArgb().toLong(), 0L, ""),
        val sessionHistories: List<FitnessHistoryMetric> = emptyList(),
        val isProcess: Boolean = true,
    )
}
