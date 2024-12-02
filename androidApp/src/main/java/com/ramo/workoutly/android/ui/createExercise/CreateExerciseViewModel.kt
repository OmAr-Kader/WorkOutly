package com.ramo.workoutly.android.ui.createExercise

import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.android.global.util.getByteArrayFromUri
import com.ramo.workoutly.android.global.util.getVideoDuration
import com.ramo.workoutly.data.aws.uploadS3VideoExercise
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.util.generateUniqueId
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.util.dateNow
import com.ramo.workoutly.global.util.dateNowMills
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateExerciseViewModel(project: Project) : BaseViewModel(project) {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    fun android.content.Context.upload(invoke: () -> Unit) {
        setIsProcess(true)
        uiState.value.also { state ->
            launchBack {
                state.videoUrl?.also { videoUrl ->
                    getByteArrayFromUri(videoUrl.first)?.also { bytes ->
                        val uid = generateUniqueId
                        uploadS3VideoExercise(
                            bytes,
                            uid + dateNowMills.toString() + videoUrl.second
                        )?.also { cloudUrl ->
                            getVideoDuration(videoUrl.first).also { length ->
                                state.exercise.copy(
                                    id = uid,
                                    views = 0,
                                    liker = emptyList(),
                                    date = dateNow,
                                    videoUrl = cloudUrl,
                                    length = length
                                ).also { exercise ->
                                    project.exercise.createExercise(exercise).also {
                                        kotlinx.coroutines.coroutineScope {
                                            invoke()
                                        }
                                    }
                                }
                            }
                        } ?: setIsProcess(false)
                    } ?: setIsProcess(false)
                } ?: setIsProcess(false)
            }
        }
    }

    fun setTitle(it: String) { // slide = 0
        _uiState.update { state ->
            state.copy(exercise = state.exercise.copy(title = it.replace("\n", "")))
        }
    }

    fun setDescription(it: String) {  // slide = 1
        _uiState.update { state ->
            state.copy(exercise = state.exercise.copy(desc = it))
        }
    }

    fun setCato(it: String) { // slide = 2
        _uiState.update { state ->
            state.copy(exercise = state.exercise.copy(cato = it))
        }
    }

    fun setVideo(videoUrl: android.net.Uri, extension: String) { // slide = 3
        _uiState.update { state ->
            state.copy(videoUrl = Pair(videoUrl, extension))
        }
    }

    fun setSlide(it: Int) {
        _uiState.update { state ->
            state.copy(slide = it)
        }
    }

    private fun setIsProcess(@Suppress("SameParameterValue") it: Boolean) {
        _uiState.update { state ->
            state.copy(isProcess = it)
        }
    }

    data class State(
        val exercise: Exercise = Exercise(),
        val videoUrl : Pair<android.net.Uri, String>? = null,
        val slide: Int = 0,
        val isProcess: Boolean = false,
    )
}