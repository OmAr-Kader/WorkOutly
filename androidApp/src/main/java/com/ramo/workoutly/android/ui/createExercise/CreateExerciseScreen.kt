package com.ramo.workoutly.android.ui.createExercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.base.darken
import com.ramo.workoutly.android.global.base.outlinedTextFieldStyle
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.BackButton
import com.ramo.workoutly.android.global.ui.LoadingScreen
import com.ramo.workoutly.android.global.ui.VerticalGrid
import com.ramo.workoutly.android.global.ui.isPortraitMode
import com.ramo.workoutly.android.global.ui.rememberCloudUpload
import com.ramo.workoutly.android.global.ui.rememberNext
import com.ramo.workoutly.android.global.ui.rememberPrevious
import com.ramo.workoutly.android.global.ui.rememberReplace
import com.ramo.workoutly.android.global.util.filePickerOnlyImage
import com.ramo.workoutly.android.global.util.videoImageBuildr
import com.ramo.workoutly.data.model.exerciseCategories
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun CreateExerciseScreen(
    @Suppress("UNUSED_PARAMETER") screen: () -> Screen.CreateExerciseRoute?,
    backPress: suspend () -> Unit,
    viewModel: CreateExerciseViewModel = koinViewModel(),
    theme: Theme = koinInject()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    val scaffoldState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val imagePicker = context.filePickerOnlyImage { url, extension ->
        viewModel.apply {
            viewModel.setVideo(url, extension)
        }
    }

    val painter = rememberAsyncImagePainter(
        model = state.videoUrl?.first,
        imageLoader = LocalContext.current.videoImageBuildr,
    )
    Scaffold(
        snackbarHost = {
            SnackbarHost(scaffoldState) {
                Snackbar(it, containerColor = theme.backDarkSec, contentColor = theme.textColor)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(theme.backgroundGradient).padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().weight(1F)) {
                BackButton(theme.background, theme.textColor) {
                    scope.launch { backPress() }
                }
                when (state.slide) {
                    0 -> VideoAndTitleItem(state, theme, painter, imagePicker, viewModel::setTitle)
                    1 -> DescriptionItem(state, theme, viewModel::setDescription)
                    2 -> CategoriesItem(state, theme, viewModel::setCato)
                }
                Spacer(Modifier)
            }
            Row(
                Modifier.fillMaxWidth().height(80.dp).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AnimatedVisibility(state.slide != 0) {
                    Button(
                        onClick = onClick@{
                            if (state.slide > 0) {
                                viewModel.setSlide(state.slide - 1)
                            }
                        },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = theme.backDark),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(
                                modifier = Modifier.height(30.dp),
                                imageVector = rememberPrevious(theme.textColor),
                                contentDescription = "Previous",
                                tint = theme.textColor
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(text = "Previous", color = theme.textColor)
                        }
                    }
                }
                Spacer(Modifier.width(10.dp))
                ExtendedFloatingActionButton(
                    text = { Text(text = "Post", color = theme.textForPrimaryColor) },
                    onClick = {
                        if (state.exercise.cato.isNotEmpty()) {
                            viewModel.apply {
                                context.upload {
                                    scope.launch {
                                        scaffoldState.showSnackbar("Uploaded Successfully")
                                        kotlinx.coroutines.delay(1000L)
                                        backPress()
                                    }
                                }
                            }
                        } else {
                            scope.launch { scaffoldState.showSnackbar("Not Completed") }
                        }
                    },
                    modifier = Modifier.padding(2.dp),
                    containerColor = if (state.exercise.cato.isNotEmpty()) theme.primary else theme.primary.darken(0.3F),
                    shape = RoundedCornerShape(15.dp),
                    expanded = true,
                    icon = {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = rememberCloudUpload(theme.textForPrimaryColor),
                            contentDescription = "Live Chat",
                            tint = theme.textForPrimaryColor
                        )
                    }
                )
                Spacer(Modifier.width(10.dp))
                AnimatedVisibility(state.slide != 2) {
                    Button(
                        onClick = onClick@{
                            if (state.slide == 0 &&(state.exercise.title.isEmpty() || state.videoUrl == null)) {
                                scope.launch { scaffoldState.showSnackbar("Please Fill the required details") }
                                return@onClick
                            } else if (state.slide == 1 &&(state.exercise.description.isEmpty())) {
                                scope.launch { scaffoldState.showSnackbar("Please Fill the required details") }
                                return@onClick
                            } else if (state.slide == 2) {
                                return@onClick
                            }
                            viewModel.setSlide(state.slide + 1)
                        },
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = theme.backDark),
                        contentPadding = PaddingValues(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Text(text = "Next", color = theme.textColor)
                            Spacer(Modifier.width(2.dp))
                            Icon(
                                modifier = Modifier.height(30.dp),
                                imageVector = rememberNext(theme.textColor),
                                contentDescription = "Next",
                                tint = theme.textColor
                            )
                        }
                    }
                }
            }
        }
        LoadingScreen(state.isProcess, theme)
    }
}

@Composable
fun VideoAndTitleItem(state: CreateExerciseViewModel.State, theme: Theme, painter: AsyncImagePainter, imagePicker: () -> Unit, setTitle: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 25.dp).height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.background),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            state.videoUrl?.first?.also {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = painter,
                    contentDescription = "Image",
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(50, 50, 50).copy(alpha = 0.5F))
                        .clickable(onClick = imagePicker)
                )
                Icon(
                    imageVector = rememberReplace(theme.textColor),
                    tint = Color.White,
                    contentDescription = "play"
                )
            } ?: Box(Modifier.fillMaxSize().clickable(onClick = imagePicker), contentAlignment = Alignment.Center) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = rememberCloudUpload(theme.textColor),
                    tint = Color.White,
                    contentDescription = "play"
                )
            }
        }
    }
    Box(
        Modifier.padding(vertical = 10.dp, horizontal = 25.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = theme.background),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Row(
                Modifier.padding(10.dp),
            ) {
                OutlinedTextField(
                    value = state.exercise.title,
                    onValueChange = setTitle,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text(text = "Enter Exercise Title", color = theme.textHintColor, fontSize = 16.sp) },
                    label = { Text(text = "Title", fontSize = 16.sp) },
                    maxLines = 2,
                    textStyle = TextStyle(fontSize = 16.sp, color = theme.textColor),
                    colors = theme.outlinedTextFieldStyle(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                )
            }
        }
    }
}

@Composable
fun DescriptionItem(state: CreateExerciseViewModel.State, theme: Theme, setDescription: (String) -> Unit) {
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
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.exercise.description,
                        onValueChange = setDescription,
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text(text = "Enter Exercise Description", color = theme.textHintColor, fontSize = 16.sp) },
                        label = { Text(text = "Description", fontSize = 16.sp) },
                        singleLine = false,
                        textStyle = TextStyle(fontSize = 16.sp, color = theme.textColor),
                        colors = theme.outlinedTextFieldStyle(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                }
            }
        }
    }
}


@Composable
fun CategoriesItem(state: CreateExerciseViewModel.State, theme: Theme, setCato: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxSize().padding(20.dp)
                ,
                contentAlignment = Alignment.Center
            ) {
                Text(
                    overflow = TextOverflow.Visible,
                    text = "Chose a muscle group", color = theme.textColor
                )
            }
        }
        item {
            VerticalGrid(
                columns = if (isPortraitMode()) 2 else 4,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(),
                exerciseCategories
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(50, 50, 50).copy(alpha = 0.3F)),
                ) {
                    Column(
                        Modifier.background(if (state.exercise.cato == it) theme.background else Color.Transparent).clickable {
                            setCato(it)
                        }.fillMaxSize().padding(10.dp)
                    ) {
                        Text(
                            overflow = TextOverflow.Visible,
                            text = it, color = theme.textColor
                        )
                    }
                }
            }
        }
    }
}