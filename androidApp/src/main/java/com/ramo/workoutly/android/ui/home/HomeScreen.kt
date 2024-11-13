package com.ramo.workoutly.android.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.ImageForCurveItem
import com.ramo.workoutly.android.global.ui.LoadingScreen
import com.ramo.workoutly.android.global.ui.OnLaunchScreen
import com.ramo.workoutly.android.global.ui.VerticalGrid
import com.ramo.workoutly.android.global.ui.isPortraitMode
import com.ramo.workoutly.android.global.ui.rememberDistance
import com.ramo.workoutly.android.global.ui.rememberDumbbell
import com.ramo.workoutly.android.global.ui.rememberFire
import com.ramo.workoutly.android.global.ui.rememberHeart
import com.ramo.workoutly.android.global.ui.rememberMetabolic
import com.ramo.workoutly.android.global.ui.rememberSleepMoon
import com.ramo.workoutly.android.global.ui.rememberSteps
import com.ramo.workoutly.android.global.ui.rememberTimer
import com.ramo.workoutly.android.global.util.checkActivityRecognition
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.data.model.UserPref
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
import com.ramo.workoutly.global.base.EXERCISE_SCREEN_ROUTE
import com.ramo.workoutly.global.base.HEART_RATE
import com.ramo.workoutly.global.base.METABOLIC_RATE
import com.ramo.workoutly.global.base.SESSION_SCREEN_ROUTE
import com.ramo.workoutly.global.base.SLEEP
import com.ramo.workoutly.global.base.STEPS
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Suppress("UNUSED_PARAMETER")
@Composable
fun HomeScreen(
    userPref: UserPref,
    findPreference: (String, (it: String?) -> Unit) -> Unit,
    navigateToScreen: suspend (Screen, String) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    theme: Theme = koinInject()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    val scaffoldState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val requestPermissions = rememberLauncherForActivityResult(
        viewModel.healthKit.contract
    ) { granted ->
        if (granted.containsAll(viewModel.healthKit.permissions)) {
            viewModel.loadData(theme.isDarkMode) {
            }
        } else {
            scope.launch {
                scaffoldState.showSnackbar("Permissions is required")
            }
        }
    }
    val permissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.any { it }
        if (isGranted) {
            viewModel.loadData(theme.isDarkMode) {
                scope.launch {
                    requestPermissions.launch(viewModel.healthKit.permissions)
                }
            }
        } else {
            scope.launch {
                scaffoldState.showSnackbar("Permissions is required")
            }
        }
    }
    val systemUiController = rememberSystemUiController()
    val statusBarColor = remember { Color(0xFF265160) } // Desired status bar color
    OnLaunchScreen {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = statusBarColor.luminance() > 0.5F // Adjust icon color based on color brightness
        )
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                context.checkActivityRecognition({
                    viewModel.loadData(theme.isDarkMode) {
                        scope.launch {
                            requestPermissions.launch(viewModel.healthKit.permissions)
                        }
                    }
                }) {
                    permissions.launch(it)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(scaffoldState) {
                Snackbar(it, containerColor = theme.backDarkSec, contentColor = theme.textColor)
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Live Session", color = theme.textForPrimaryColor) },
                onClick = {

                },
                containerColor = theme.primary,
                shape = RoundedCornerShape(15.dp),
                expanded = true,
                icon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = rememberDumbbell(theme.textForPrimaryColor),
                        contentDescription = "Live Chat",
                        tint = theme.textForPrimaryColor
                    )
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).background(theme.backgroundGradient)) {
            BarMainScreen(userPref) {

            }
            LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
                item {
                    VerticalGrid(
                        columns = if (isPortraitMode()) 2 else 4, // 2 columns in the grid
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentPadding = PaddingValues(),
                        state.metrics
                    ) { item ->
                        FitnessMetricItem(metric = item, theme = theme) {
                            scope.launch {
                                navigateToScreen.invoke(Screen.SessionRoute(item), SESSION_SCREEN_ROUTE)
                            }
                        }
                    }
                }
                items(state.exercises) { exercise ->
                    ExerciseItem(exercise = exercise, theme = theme) {
                        scope.launch {
                            navigateToScreen.invoke(Screen.ExerciseRoute(exercise), EXERCISE_SCREEN_ROUTE)
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(80.dp))
                }
            }
            /**
            LazyVerticalGrid(
            columns = GridCells.Fixed(if (isPortraitMode()) 2 else 4), // 2 columns in the grid
            modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            items(state.metrics) { item ->
            FitnessMetricItem(metric = item, theme = theme) {
            scope.launch {
            navigateToScreen.invoke(Screen.SessionRoute(item), SESSION_SCREEN_ROUTE)
            }
            }
            }
            }
             */
        }
        LoadingScreen(state.isProcess, theme)
    }
}

@Composable
fun BarMainScreen(
    userPref: UserPref,
    theme: Theme = koinInject(),
    onOptions: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {}
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOptions,
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = theme.textForGradientColor
                    )
                }
            }
        }
        Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            Text("Hi, ${userPref.name}", color = theme.textForGradientColor, fontSize = 20.sp)
        }
    }
}

@Composable
fun FitnessMetricItem(metric: FitnessMetric, theme: Theme, invoke: () -> Unit) {
    val imageVector = when (metric.recordId) {
        STEPS -> rememberSteps(Color(metric.iconColor))
        HEART_RATE -> rememberHeart(Color(metric.iconColor))
        CALORIES_BURNED -> rememberFire(Color(metric.iconColor))
        DISTANCE -> rememberDistance(Color(metric.iconColor))
        SLEEP -> rememberSleepMoon(Color(metric.iconColor))
        METABOLIC_RATE -> rememberMetabolic(Color(metric.iconColor))
        else -> rememberHeart(Color(metric.iconColor))
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.background),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = invoke)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = imageVector,
                contentDescription = metric.title,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = metric.title,
                maxLines = 1,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = theme.textColor)
            )
            Text(
                text = metric.valueStr,
                maxLines = 1,
                style = TextStyle(fontSize = 14.sp, color = theme.textGrayColor)
            )
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise, theme: Theme, onClick: () -> Unit) {
    Box(
        Modifier.padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = theme.background),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ImageForCurveItem(exercise.videoUri, 80.dp)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) subBox@{
                    Text(
                        text = exercise.title,
                        color = theme.textColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                    Text(
                        text = exercise.description,
                        color = theme.textGrayColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .align(Alignment.Start)
                            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Person",
                                tint = theme.textColor,
                                modifier = Modifier
                                    .width(15.dp)
                                    .height(15.dp)
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = exercise.views.toString(),
                                color = theme.textGrayColor,
                                fontSize = 10.sp,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        Row(
                            Modifier.padding(2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = rememberTimer(color = theme.primary),
                                contentDescription = "Money",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .width(15.dp)
                                    .height(15.dp)
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                text = exercise.lengthStr,
                                color = theme.textColor,
                                fontSize = 10.sp,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}

/*

*/
