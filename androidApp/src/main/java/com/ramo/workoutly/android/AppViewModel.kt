package com.ramo.workoutly.android

import com.ramo.workoutly.android.global.navigation.BaseViewModel
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.navigation.replace
import com.ramo.workoutly.android.global.navigation.valueOf
import com.ramo.workoutly.android.global.navigation.values
import com.ramo.workoutly.data.model.PreferenceData
import com.ramo.workoutly.data.model.UserPref
import com.ramo.workoutly.di.Project
import com.ramo.workoutly.global.base.PREF_ID
import com.ramo.workoutly.global.base.PREF_NAME
import com.ramo.workoutly.global.base.PREF_PROFILE_IMAGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel(project: Project) : BaseViewModel(project) {

    @Suppress("PropertyName")
    val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    private var prefsJob: kotlinx.coroutines.Job? = null
    private var userJob: kotlinx.coroutines.Job? = null

    private fun inti(invoke: List<PreferenceData>.() -> Unit) {
        prefsJob?.cancel()
        prefsJob = launchBack {
            project.pref.prefs {
                _uiState.update { state ->
                    state.copy(preferences = it)
                }
                invoke(it)
            }
        }
    }

    fun findUser(invoke: (UserPref?) -> Unit) {
        launchBack {
            findPrefString(PREF_ID) { id ->
                findPrefString(PREF_NAME) { name ->
                    findPrefString(PREF_PROFILE_IMAGE) { profileImage ->
                        launchBack {
                            /*userInfo()?.copy(id = id?.toLongOrNull() ?: 0L,name = name ?: "", profilePicture = profileImage ?: "")?.let {
                                _uiState.update { state ->
                                    state.copy(userPref = it)
                                }
                                invoke(it)
                            } ?: invoke(null)*/
                        }
                    }
                }
            }
        }
    }

    fun findUserLive(invoke: (UserPref?) -> Unit) {
        userJob = launchBack {
            /*fetchSupaBaseUser { userBase, status ->
                if (userBase != null) {*/
                    findPrefString(PREF_ID) { id ->
                        findPrefString(PREF_NAME) { name ->
                            findPrefString(PREF_PROFILE_IMAGE) { profileImage ->
                                /*userBase.copy(id = id?.toLongOrNull() ?: 0L, name = name ?: "", profilePicture = profileImage ?: "").let {
                                    _uiState.update { state ->
                                        state.copy(userPref = it, sessionStatus = status)
                                    }
                                    userJob?.cancel()
                                    userJob = null
                                    invoke(it)
                                }*/
                            }
                        }
                    }
                /*} else {
                     if (status == SessionStatusData.NotAuthenticated) {
                         invoke(null)
                     }
                    _uiState.update { state ->
                        state.copy(sessionStatus = status)
                    }
                }
            }*/
        }
    }

    fun findPrefString(
        key: String,
        value: (it: String?) -> Unit,
    ) {
        if (uiState.value.preferences.isEmpty()) {
            inti {
                value(this@inti.find { it1 -> it1.keyString == key }?.value)
            }
        } else {
            value(uiState.value.preferences.find { it.keyString == key }?.value)
        }
    }

    inline fun <reified T : Screen> findArg() = uiState.value.args.valueOf<T>()

    suspend inline fun <reified T : Screen> writeArguments(screen: T) = kotlinx.coroutines.coroutineScope {
        _uiState.update { state ->
            state.copy(args = state.args.toMutableList().replace(screen), dummy = state.dummy + 1)
        }
    }

    data class State(
        val isProcess: Boolean = false,
        val userPref: UserPref = UserPref(),
        //val sessionStatus: SessionStatusData = SessionStatusData.LoadingFromStorage,
        val args: List<Screen> = values(),
        val preferences: List<PreferenceData> = listOf(),
        val dummy: Int = 0,
    )
}