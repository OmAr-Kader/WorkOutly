package com.ramo.workoutly.android.ui.exercise

import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.di.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ExerciseViewModel(project: Project) : BaseViewModel(project) {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    fun loadData(exercise: Exercise) {
        _uiState.update { state ->
            state.copy(exercise = exercise)
        }
        launchBack {
            project.exercise.getExercise(exercise.id)?.also {
                _uiState.update { state ->
                    state.copy(exercise = it)
                }
            }
        }
    }

    private fun setIsProcess(@Suppress("SameParameterValue") it: Boolean) {
        _uiState.update { state ->
            state.copy(isProcess = it)
        }
    }

    data class State(
        val exercise: Exercise = Exercise(),
        val isProcess: Boolean = true,
    )
}