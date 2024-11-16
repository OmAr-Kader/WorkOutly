package com.ramo.workoutly.android.ui.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.BackButton
import com.ramo.workoutly.android.global.ui.OnLaunchScreen
import com.ramo.workoutly.android.global.ui.rememberTimer
import com.ramo.workoutly.android.global.util.videoConfig
import com.ramo.workoutly.android.global.util.videoItem
import com.ramo.workoutly.data.model.Exercise
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ExerciseScreen(
    screen: () -> Screen.ExerciseRoute?,
    backPress: suspend () -> Unit,
    viewModel: ExerciseViewModel = koinViewModel(),
    theme: Theme = koinInject()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    val videoItem = remember(state.exercise.videoUri) { state.exercise.let { videoItem(it.videoUri, it.title) } }
    val videoConfig = remember { videoConfig }
    OnLaunchScreen {
        screen()?.metric?.let { viewModel.loadData(it) }
    }
    Scaffold { padding ->
        Column(modifier = Modifier.background(theme.backgroundGradient).padding(padding)) {
            BackButton(theme.background ,theme.textColor) {
                scope.launch { backPress() }
            }
            Column(modifier = Modifier) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 25.dp).height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = theme.background),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    VideoPlayer(
                        mediaItems = listOf(videoItem),
                        handleLifecycle = true,
                        autoPlay = false,
                        usePlayerController = true,
                        enablePip = true,
                        handleAudioFocus = true,
                        controllerConfig = videoConfig,
                        repeatMode = RepeatMode.ALL,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                    )
                }
                ExerciseTitle(exercise = state.exercise, theme = theme)
                Column(Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 25.dp)) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = theme.background),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.Start) {
                                Text(
                                    text = "Description",
                                    style = TextStyle(fontSize = 16.sp, color = theme.textGrayColor)
                                )
                            }
                            LazyColumn {
                                item {
                                    Text(
                                        text = state.exercise.description +"\n",
                                        color = theme.textHintColor,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 15.dp, end = 15.dp)
                                    )
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
fun ExerciseTitle(exercise: Exercise, theme: Theme) {
    Box(
        Modifier.padding(vertical = 10.dp, horizontal = 25.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = theme.background),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) subBox@{
                    Text(
                        text = exercise.title,
                        color = theme.textColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(horizontal = 5.dp, vertical = 5.dp)
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

val String.makeValidLink: String get() {
    return try {
        URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
            .replace("/", "%2F") // Decode slashes
            .replace(":", "%3A")
            .replace(" ", "%20")
            .replace("\n", "")
    } catch (e: Exception) {
        this // Return null if the URL is invalid
    }
}