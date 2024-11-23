package com.ramo.workoutly.android

import com.ramo.workoutly.android.global.util.isDarkMode
import com.ramo.workoutly.android.ui.exercise.ExerciseViewModel
import com.ramo.workoutly.android.ui.home.HomeViewModel
import com.ramo.workoutly.android.ui.session.SessionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel

class App : androidx.multidex.MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        org.koin.core.context.startKoin {
            androidContext(this@App)
            modules(com.ramo.workoutly.di.appModule(BuildConfig.DEBUG) + org.koin.dsl.module {
                single<com.ramo.workoutly.android.global.base.Theme> {
                    com.ramo.workoutly.android.global.base.generateTheme(isDarkMode = isDarkMode)
                }
                single<com.ramo.workoutly.android.data.health.HealthKitManager> {
                    com.ramo.workoutly.android.data.health.HealthKitManager(this@App)
                }
                viewModel { AppViewModel(get()) }
                viewModel { SessionViewModel(get(), get()) }
                viewModel { ExerciseViewModel(get()) }
                single { HomeViewModel(get(), get()) }
            })
        }
    }
}