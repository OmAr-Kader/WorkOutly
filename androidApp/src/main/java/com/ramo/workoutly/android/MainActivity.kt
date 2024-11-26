package com.ramo.workoutly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramo.workoutly.android.global.base.MyApplicationTheme
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.OnLaunchScreen
import com.ramo.workoutly.android.ui.exercise.ExerciseScreen
import com.ramo.workoutly.android.ui.home.HomeScreen
import com.ramo.workoutly.android.ui.session.SessionScreen
import com.ramo.workoutly.global.base.BASE_SHARED_DOMAIN
import com.ramo.workoutly.global.base.BASE_SHARED_DOMAIN_HTTPS
import com.ramo.workoutly.global.base.EXERCISE_SCREEN_ROUTE
import com.ramo.workoutly.global.base.HOME_SCREEN_ROUTE
import com.ramo.workoutly.global.base.SESSION_SCREEN_ROUTE
import com.ramo.workoutly.global.base.SPLASH_SCREEN_ROUTE
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deepLink = intent?.data?.toString()?.let { url ->
            if (android.content.Intent.ACTION_VIEW == intent?.action && url.contains(BASE_SHARED_DOMAIN)) {
                url.split(BASE_SHARED_DOMAIN_HTTPS).lastOrNull()
            } else {
                null
            }
        }
        setContent {
            Main(deepLink) { isTextDark, color ->
                enableEdgeToEdge(
                    statusBarStyle = if (isTextDark) SystemBarStyle.auto(
                        lightScrim = color,
                        darkScrim = color
                    ) else SystemBarStyle.dark(
                        scrim = color,
                    )
                )
            }
        }
    }
}

@Composable
fun Main(deepLink: String?, theme: Theme = koinInject(), appViewModel: AppViewModel = koinViewModel(), statusColor: (isDark: Boolean, Int) -> Unit) {
    val stateApp by appViewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navigateHome: suspend (String) -> Unit = { route ->
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
                    startDestination = SPLASH_SCREEN_ROUTE
                ) {
                    composable(route = SPLASH_SCREEN_ROUTE) {
                        SplashScreen(navigateHome = navigateHome, appViewModel = appViewModel)
                    }
                    /*composable(route = AUTH_SCREEN_ROUTE) {
                        AuthScreen(appViewModel = appViewModel, navigateHome = navigateHome)
                    }*/
                    composable(route = HOME_SCREEN_ROUTE) {
                        HomeScreen(stateApp.userPref, deepLink, statusColor, findPreference = findPreference, navigateToScreen = navigateToScreen)
                    }
                    composable(route = SESSION_SCREEN_ROUTE) {
                        SessionScreen(appViewModel::findArg, backPress = backPress)
                    }
                    composable(route = EXERCISE_SCREEN_ROUTE) {
                        ExerciseScreen(appViewModel::findArg, backPress = backPress)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    navigateHome: suspend (String) -> Unit,
    @Suppress("UNUSED_PARAMETER") appViewModel: AppViewModel,
    theme: Theme = koinInject(),
) {
    val scope = rememberCoroutineScope()
    OnLaunchScreen {
        /*appViewModel.findUserLive {
            scope.launch {
                navigateHome(
                    if (it == null) AUTH_SCREEN_ROUTE else HOME_SCREEN_ROUTE
                )
            }
        }*/
        scope.launch {
            kotlinx.coroutines.delay(300L)
            kotlinx.coroutines.coroutineScope {
                navigateHome(
                    HOME_SCREEN_ROUTE
                )
            }
        }
    }
    Surface(color = theme.background) {
        androidx.compose.foundation.layout.Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            AnimatedVisibility(
                visible = true,
                modifier = Modifier
                    .size(100.dp),
                enter = fadeIn(initialAlpha = 0.3F) + expandIn(expandFrom = androidx.compose.ui.Alignment.Center),
                label = "AppIcon"
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_work_outly),
                    contentScale = ContentScale.Fit,
                    contentDescription = "AppIcon",
                )
            }
        }
    }
}