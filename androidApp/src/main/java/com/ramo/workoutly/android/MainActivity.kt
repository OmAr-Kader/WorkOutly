package com.ramo.workoutly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramo.workoutly.android.global.base.MyApplicationTheme
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.AnimatedExpandOnce
import com.ramo.workoutly.android.global.util.isDarkMode
import com.ramo.workoutly.android.ui.createExercise.CreateExerciseScreen
import com.ramo.workoutly.android.ui.exercise.ExerciseScreen
import com.ramo.workoutly.android.ui.home.HomeScreen
import com.ramo.workoutly.android.ui.session.SessionScreen
import com.ramo.workoutly.global.base.BASE_SHARED_DOMAIN
import com.ramo.workoutly.global.base.BASE_SHARED_DOMAIN_HTTPS
import com.ramo.workoutly.global.base.CREATE_EXERCISE_SCREEN_ROUTE
import com.ramo.workoutly.global.base.EXERCISE_SCREEN_ROUTE
import com.ramo.workoutly.global.base.HOME_SCREEN_ROUTE
import com.ramo.workoutly.global.base.SESSION_SCREEN_ROUTE
import com.ramo.workoutly.global.util.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.module.dsl.viewModel
import kotlin.math.log

class MainActivity : ComponentActivity() {

    private val checkDeepLink: String? get() {
        return intent?.data?.toString()?.let { url ->
            if (android.content.Intent.ACTION_VIEW == intent?.action && url.contains(BASE_SHARED_DOMAIN)) {
                url.split(BASE_SHARED_DOMAIN_HTTPS).lastOrNull()
            } else {
                null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            androidx.core.splashscreen.SplashScreen.apply {
                installSplashScreen().apply {
                    setKeepOnScreenCondition {
                        true
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
        val deepLink = checkDeepLink
        val theme = com.ramo.workoutly.android.global.base.generateTheme(isDarkMode = isDarkMode)
        setContent {
            val mainRoute = remember { mutableStateOf<String?>(null) }
            mainRoute.value?.also {
                Main(mainRoute = it, deepLink = deepLink) { isTextDark, color ->
                    enableEdgeToEdge(
                        statusBarStyle = if (isTextDark)
                            SystemBarStyle.auto(lightScrim = color, darkScrim = color)
                        else
                            SystemBarStyle.dark(scrim = color)
                    )
                }
            } ?: SplashScreen(theme, koinModules = ::koinModules) {
                mainRoute.value = it
            }
        }
    }

    private suspend fun koinModules(theme: Theme) {
        if (org.koin.core.context.GlobalContext.getKoinApplicationOrNull() != null) {
            logger("startKoin","Already Initialed")
            return
        }
        com.ramo.workoutly.di.appModuleSus(BuildConfig.DEBUG).apply {
            org.koin.core.context.startKoin {
                androidContext(this@MainActivity)
                modules(this@apply + org.koin.dsl.module {
                    single<Theme> { theme }
                    single<com.ramo.workoutly.android.data.health.HealthKitManager> {
                        com.ramo.workoutly.android.data.health.HealthKitManager(this@MainActivity)
                    }
                    viewModel { AppViewModel(get()) }
                    viewModel { com.ramo.workoutly.android.ui.session.SessionViewModel(get(), get()) }
                    viewModel { com.ramo.workoutly.android.ui.exercise.ExerciseViewModel(get()) }
                    viewModel { com.ramo.workoutly.android.ui.createExercise.CreateExerciseViewModel(get()) }
                    single { com.ramo.workoutly.android.ui.home.HomeViewModel(get(), get()) }
                })
            }

        }
    }
}

@Composable
fun Main(
    mainRoute: String,
    deepLink: String?,
    theme: Theme = koinInject(),
    appViewModel: AppViewModel = koinViewModel(),
    statusColor: (isDark: Boolean, Int) -> Unit
) {
    val stateApp by appViewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val checkDeepLink =  remember { mutableStateOf(deepLink) }
    @Suppress("UNUSED_VARIABLE") val navigateHome: suspend (String) -> Unit = { route ->
        navController.navigate(route = route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }
    @Suppress("UNUSED_VARIABLE") val navigateTo: suspend (String) -> Unit = { route ->
        navController.navigate(route = route)
    }
    val findPreference: (String, (it: String?) -> Unit) -> Unit = appViewModel::findPrefString
    val navigateToScreen: suspend (Screen, String) -> Unit = { screen , route ->
        appViewModel.writeArguments(screen)
        kotlinx.coroutines.coroutineScope {
            navController.navigate(route = route)
        }
    }
    val backPress: suspend () -> Unit = {
        navController.navigateUp()
    }
    SideEffect {
        statusColor(!theme.isDarkMode, theme.background.toArgb())
    }
    MyApplicationTheme(theme = theme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = theme.background
        ) {
            Surface(
                color = theme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = mainRoute//HOME_SCREEN_ROUTE
                ) {
                    /*composable(route = SPLASH_SCREEN_ROUTE) {
                        SplashScreen(navigateHome = navigateHome, appViewModel = appViewModel)
                    }*/
                    /*composable(route = AUTH_SCREEN_ROUTE) {
                        AuthScreen(appViewModel = appViewModel, navigateHome = navigateHome)
                    }*/
                    composable(route = HOME_SCREEN_ROUTE) {
                        HomeScreen(stateApp.userPref, checkDeepLink, statusColor, findPreference = findPreference, navigateToScreen = navigateToScreen)
                    }
                    composable(route = SESSION_SCREEN_ROUTE) {
                        SessionScreen(appViewModel::findArg, backPress = backPress)
                    }
                    composable(route = EXERCISE_SCREEN_ROUTE) {
                        ExerciseScreen(appViewModel::findArg, backPress = backPress)
                    }
                    composable(route = CREATE_EXERCISE_SCREEN_ROUTE) {
                        CreateExerciseScreen(appViewModel::findArg, backPress = backPress)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    theme: Theme,
    koinModules: suspend (Theme) -> Unit,
    route: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                scope.launch {
                    kotlinx.coroutines.withContext(Dispatchers.IO) {
                        koinModules(theme)
                        kotlinx.coroutines.delay(100L)
                        kotlinx.coroutines.coroutineScope {
                            /*appViewModel.findUserLive {
                            scope.launch {
                                navigateHome(
                                    if (it == null) AUTH_SCREEN_ROUTE else HOME_SCREEN_ROUTE
                                )
                            }
                        }*/
                        }
                    }
                    kotlinx.coroutines.withContext(Dispatchers.Main) {
                        route(HOME_SCREEN_ROUTE)//if (it == null) AUTH_SCREEN_ROUTE else HOME_SCREEN_ROUTE
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Surface(Modifier.fillMaxSize(), color = theme.background) {
        androidx.compose.foundation.layout.Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            AnimatedExpandOnce(Modifier.size(250.dp), label = "AppIcon", duration = 300) {
                Image(
                    painter = painterResource(R.drawable.ic_work_outly),
                    contentScale = ContentScale.Fit,
                    contentDescription = "AppIcon",
                )
            }
        }
    }
}