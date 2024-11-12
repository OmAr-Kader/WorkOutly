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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.OnLaunchScreen
import com.ramo.workoutly.android.global.ui.VerticalGrid
import com.ramo.workoutly.android.global.ui.isPortraitMode
import com.ramo.workoutly.android.global.ui.rememberDistance
import com.ramo.workoutly.android.global.ui.rememberFire
import com.ramo.workoutly.android.global.ui.rememberHeart
import com.ramo.workoutly.android.global.ui.rememberMetabolic
import com.ramo.workoutly.android.global.ui.rememberSleepMoon
import com.ramo.workoutly.android.global.ui.rememberSteps
import com.ramo.workoutly.android.global.util.checkActivityRecognition
import com.ramo.workoutly.data.model.UserPref
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
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
    navigateHome: suspend (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    theme: Theme = koinInject()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    val scaffoldState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val requestPermissions = rememberLauncherForActivityResult( //registerForActivityResult
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
    val permissions = rememberLauncherForActivityResult( //registerForActivityResult
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
    val statusBarColor = Color(0xFF265160) // Desired status bar color
    OnLaunchScreen {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = statusBarColor.luminance() > 0.5F // Adjust icon color based on color brightness
        )
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {

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
                .padding(start = 15.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {

            }
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOptions,
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = theme.textForPrimaryColor
                    )
                }
            }
        }
        Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            Text("Hi, ${userPref.name}", color = theme.textForPrimaryColor, fontSize = 20.sp)
        }
    }
}

@Composable
fun FitnessMetricItem(metric: FitnessMetric, theme: Theme, invoke: () -> Unit) {
    val imageVector = when (metric.recordId) {
        STEPS -> rememberSteps(metric.iconColor)
        HEART_RATE -> rememberHeart(metric.iconColor)
        CALORIES_BURNED -> rememberFire(metric.iconColor)
        DISTANCE -> rememberDistance(metric.iconColor)
        SLEEP -> rememberSleepMoon(metric.iconColor)
        METABOLIC_RATE -> rememberMetabolic(metric.iconColor)
        else -> rememberHeart(metric.iconColor)
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


// Sample data class for fitness metric items
data class FitnessMetric(
    val recordId: Int,
    val title: String,
    val iconColor: Color,
    val value: Long,
    val valueUnit: String,
    val valueStr: String = value.toString() + valueUnit
)
// Sample data class for fitness metric items
data class FitnessHistoryMetric(
    val data: String,
    val value: Long,
    val valueUnit: String,
) {
    val valueStr = value.toString() + valueUnit
}
/*

*/
