package com.ramo.workoutly.android.ui.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ramo.workoutly.android.data.health.HealthKitManager
import com.ramo.workoutly.android.global.base.darken
import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.util.getByteArrayFromUri
import com.ramo.workoutly.data.aws.uploadS3Message
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.model.PreferenceData
import com.ramo.workoutly.data.model.UserPref
import com.ramo.workoutly.data.util.exercisesSort
import com.ramo.workoutly.data.util.generateUniqueId
import com.ramo.workoutly.data.util.messagesFilter
import com.ramo.workoutly.data.util.messagesSort
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
import com.ramo.workoutly.global.base.EXERCISE_BY_ID
import com.ramo.workoutly.global.base.EXERCISE_SCREEN_ROUTE
import com.ramo.workoutly.global.base.HEART_RATE
import com.ramo.workoutly.global.base.METABOLIC_RATE
import com.ramo.workoutly.global.base.MSG_TEXT
import com.ramo.workoutly.global.base.PREF_DAYS_COUNT
import com.ramo.workoutly.global.base.PREF_SORT_BY
import com.ramo.workoutly.global.base.SLEEP
import com.ramo.workoutly.global.base.STEPS
import com.ramo.workoutly.global.util.averageSafeDouble
import com.ramo.workoutly.global.util.averageSafeLong
import com.ramo.workoutly.global.util.dateNow
import com.ramo.workoutly.global.util.dateNowMills
import com.ramo.workoutly.global.util.dateNowOnlyHour
import com.ramo.workoutly.global.util.dateNowUTCMILLSOnlyHour
import com.ramo.workoutly.global.util.formatMillisecondsToHours
import com.ramo.workoutly.global.util.logger
import com.ramo.workoutly.global.util.observeNextHour
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Duration

class HomeViewModel(project: Project, val healthKit: HealthKitManager) : BaseViewModel(project) {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    fun loadData(userPref: UserPref, deepLink: String?, days: Int, isDarkMode: Boolean, onLink: (Screen, String) -> Unit, permission: () -> Unit) {
        launchBack {
            healthKit.requestPermissions({
                loadMetrics(days, isDarkMode).also { metrics ->
                   project.exercise.fetchExercises().exercisesSort().also { exercises -> // @OmAr-Kader sortBy
                        project.message.fetchMessageSession(dateNowUTCMILLSOnlyHour).messagesFilter(userPref.id).messagesSort().also { messages ->
                            deepLink?.also { link ->
                                handleDeepLink(link, onLink = onLink) { id ->
                                    exercises.find { it.id == id }?.let { Screen.ExerciseRoute(it) }
                                }
                            }
                            kotlinx.coroutines.coroutineScope {
                                _uiState.update { state ->
                                    state.copy(
                                        metrics = metrics,
                                        exercises = exercises,
                                        displayExercises = exercises,
                                        days = days,
                                        currentSession = dateNowOnlyHour,
                                        messages = messages,
                                        isProcess = false
                                    )
                                }
                            }
                            observeSessionChanged(userPref.id)
                            startMessagesObserving(userPref.id)
                        }
                    }
                }
            }, permission)
        }
    }

    private fun observeSessionChanged(id: String) {
        launchBack {
            observeNextHour {
                launchBack {
                    project.message.fetchMessageSession(dateNowUTCMILLSOnlyHour).messagesFilter(id).messagesSort().also { messages ->
                        _uiState.update { state ->
                            state.copy(
                                messages = messages,
                                currentSession = dateNowOnlyHour,
                                isProcess = false
                            )
                        }
                    }
                    kotlinx.coroutines.coroutineScope {
                        observeSessionChanged(id)
                    }
                }
            }
        }
    }

    private suspend fun startMessagesObserving(userId: String) {
        project.message.fetchNewMessages(inserted =  { new ->
            _uiState.update { state ->
                state.copy(messages = (state.messages + new.copy(isFromCurrentUser = new.userId == userId)).messagesSort())
            }
        }, changed = {

        }, deleted = { delete ->
            uiState.value.messages.filter { it.id != delete }.messagesSort().also { refreshed ->
                _uiState.update { state ->
                    state.copy(messages = refreshed)
                }
            }
        })
    }

    private fun handleDeepLink(deepLink: String, onLink: (Screen, String) -> Unit, exercise: (String) -> Screen?) {
        if (deepLink.contains(EXERCISE_BY_ID)) {
            deepLink.split(EXERCISE_BY_ID).lastOrNull()?.also {
                exercise(it)?.also { screen ->
                    onLink(screen, EXERCISE_SCREEN_ROUTE)
                }
            }
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

    private fun reLoadMetrics(days: Int, isDarkMode: Boolean) {
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
        reLoadMetrics(days = it, isDarkMode =  isDarkMode)
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

    fun android.content.Context.setFile(url: android.net.Uri, type: Int, extension: String, user: UserPref, failed: () -> Unit) {
        setIsProcess(true)
        launchBack {
            getByteArrayFromUri(url)?.also {
                uploadS3Message(imageBytes = it, fileName = user.id + dateNowMills.toString() + extension, type = type)?.also { url ->
                    logger("uploadS3Message", url)
                    sendFile(fileUrl = url, type = type, user = user)
                } ?: failed()
            } ?: kotlin.run {
                setIsProcess(false)
                failed()
                logger("getByteArrayFromUri", "null")
            }
        }
    }

    private fun sendFile(fileUrl: String, type: Int, user: UserPref) {
        launchBack {
            project.message.addMessage(
                Message(
                    id = generateUniqueId,
                    userId = user.id,
                    senderName = user.name,
                    message = "",
                    fileUrl = fileUrl,
                    type = type,
                    session = dateNowUTCMILLSOnlyHour,
                    date = dateNow,
                )
            )?.also { new ->
                _uiState.update { state ->
                    state.copy(messages = (state.messages + new.copy(isFromCurrentUser = true)).messagesSort(), isProcess = false)
                }
            } ?: setIsProcess(false)
        }
    }

    fun send(message: String, user: UserPref) {
        launchBack {
            project.message.addMessage(
                Message(
                    id = generateUniqueId,
                    userId = user.id,
                    senderName = user.name,
                    message = message,
                    fileUrl = "",
                    type = MSG_TEXT,
                    session = dateNowUTCMILLSOnlyHour,
                    date = dateNow,
                )
            )?.also { new ->
                _uiState.update { state ->
                    state.copy(messages = (state.messages + new.copy(isFromCurrentUser = true)).messagesSort())
                }
            }
        }
    }

    fun setChosenCato(cato: String) {
        if (cato.isEmpty()) {
            uiState.value.exercises.toList() // @OmAr-Kader sortBy
        } else {
            uiState.value.exercises.toList().filter { it.cato == cato } // @OmAr-Kader sortBy
        }.exercisesSort().also { exercises ->
            _uiState.update { state ->
                state.copy(displayExercises = exercises, chosenCato = cato)
            }
        }
    }

    @Suppress("unused")
    private fun setIsProcess(@Suppress("SameParameterValue") it: Boolean) {
        _uiState.update { state ->
            state.copy(isProcess = it)
        }
    }

    data class State(
        val metrics: List<FitnessMetric> = emptyList(),
        val exercises: List<Exercise> = emptyList(),
        val displayExercises: List<Exercise> = emptyList(),
        val messages: List<Message> = emptyList(),
        val days: Int = 3,
        val sortBy: Int = 1,
        val chatText: String = "",
        val chosenCato: String = "",
        val currentSession: String = "",
        val isProcess: Boolean = true,
    )
}
