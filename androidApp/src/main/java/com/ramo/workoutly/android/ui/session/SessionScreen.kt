package com.ramo.workoutly.android.ui.session

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.BackButton
import com.ramo.workoutly.android.global.ui.OnLaunchScreen
import com.ramo.workoutly.android.global.ui.rememberDistance
import com.ramo.workoutly.android.global.ui.rememberFire
import com.ramo.workoutly.android.global.ui.rememberHeart
import com.ramo.workoutly.android.global.ui.rememberMetabolic
import com.ramo.workoutly.android.global.ui.rememberSleepMoon
import com.ramo.workoutly.android.global.ui.rememberSteps
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
import com.ramo.workoutly.global.base.HEART_RATE
import com.ramo.workoutly.global.base.METABOLIC_RATE
import com.ramo.workoutly.global.base.SLEEP
import com.ramo.workoutly.global.base.STEPS
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SessionScreen(
    screen: () -> Screen.SessionRoute?,
    backPress: suspend () -> Unit,
    viewModel: SessionViewModel = koinViewModel(),
    theme: Theme = koinInject()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    OnLaunchScreen {
        screen()?.metric?.let { viewModel.loadData(it) }
    }
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).background(theme.backgroundGradient)) {
            BackButton(theme.background ,theme.textColor) {
                scope.launch { backPress() }
            }
            Column(modifier = Modifier.padding(all = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                    FitnessMetricLabel(state.session, theme) {}
                }
                Spacer(Modifier.height(10.dp))
                Column(Modifier.fillMaxSize().padding(all = 16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = theme.background),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(Modifier.fillMaxWidth().padding(5.dp)) {
                                Text(
                                    text = "History",
                                    style = TextStyle(fontSize = 16.sp, color = theme.textGrayColor)
                                )
                            }
                            LazyColumn {
                                items(state.sessionHistories) { metric ->
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 6.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = metric.valueStr,
                                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = theme.textColor)
                                        )
                                        Spacer(Modifier.width(50.dp))
                                        Text(
                                            text = metric.data,
                                            style = TextStyle(fontSize = 14.sp, color = theme.textGrayColor)
                                        )
                                        Spacer(Modifier)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FitnessMetricLabel(metric: FitnessMetric, theme: Theme, invoke: () -> Unit) {
    val imageVector = when (metric.id) {
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
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.background),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = invoke)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = imageVector,
                contentDescription = metric.title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = metric.title,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = theme.textColor)
            )
            Text(
                text = metric.valueStr,
                style = TextStyle(fontSize = 14.sp, color = theme.textGrayColor)
            )
        }
    }
}