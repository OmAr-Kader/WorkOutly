package com.ramo.workoutly.android.ui.session

import androidx.compose.ui.graphics.Color
import androidx.health.connect.client.records.StepsRecord
import com.ramo.workoutly.android.data.health.HealthKitManager
import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.android.ui.home.FitnessHistoryMetric
import com.ramo.workoutly.android.ui.home.FitnessMetric
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.base.STEPS
import com.ramo.workoutly.global.util.toTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SessionVewModel(project: Project, private val healthKit: HealthKitManager) : BaseViewModel(project) {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    fun loadData(metric: FitnessMetric) {
        _uiState.update { state ->
            state.copy(session = metric)
        }
        launchBack {
            when (metric.recordId) {
                STEPS -> {
                    loadStepsData()
                }
            }
        }
    }

    private suspend fun loadStepsData() {
        healthKit.fetchHealthDataForLastThreeDays(StepsRecord::class).also {
            it.map { step ->
                FitnessHistoryMetric(step.metadata.lastModifiedTime.toEpochMilli().toTimestamp, step.count, "")
            }.also { history ->
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
        val session: FitnessMetric = FitnessMetric(0,  "", Color.Transparent, 0L, ""),
        val sessionHistories: List<FitnessHistoryMetric> = emptyList(),
        val isProcess: Boolean = true,
    )
}
