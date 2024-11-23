package com.ramo.workoutly.android.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.twotone.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.ramo.workoutly.android.global.base.Theme
import com.ramo.workoutly.android.global.navigation.Screen
import com.ramo.workoutly.android.global.ui.AnimatedFadeOnce
import com.ramo.workoutly.android.global.ui.BackButtonLess
import com.ramo.workoutly.android.global.ui.ImageForCurveItem
import com.ramo.workoutly.android.global.ui.LoadingScreen
import com.ramo.workoutly.android.global.ui.OnLaunchScreen
import com.ramo.workoutly.android.global.ui.VerticalGrid
import com.ramo.workoutly.android.global.ui.isPortraitMode
import com.ramo.workoutly.android.global.ui.rememberDistance
import com.ramo.workoutly.android.global.ui.rememberDumbbell
import com.ramo.workoutly.android.global.ui.rememberFile
import com.ramo.workoutly.android.global.ui.rememberFire
import com.ramo.workoutly.android.global.ui.rememberHeart
import com.ramo.workoutly.android.global.ui.rememberMetabolic
import com.ramo.workoutly.android.global.ui.rememberSleepMoon
import com.ramo.workoutly.android.global.ui.rememberSteps
import com.ramo.workoutly.android.global.ui.rememberTimer
import com.ramo.workoutly.android.global.util.checkActivityRecognition
import com.ramo.workoutly.android.global.util.filePicker
import com.ramo.workoutly.android.global.util.imageBuildr
import com.ramo.workoutly.android.global.util.videoConfig
import com.ramo.workoutly.android.global.util.videoImageBuildr
import com.ramo.workoutly.android.global.util.videoItem
import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.FitnessMetric
import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.model.UserPref
import com.ramo.workoutly.global.base.CALORIES_BURNED
import com.ramo.workoutly.global.base.DISTANCE
import com.ramo.workoutly.global.base.EXERCISE_SCREEN_ROUTE
import com.ramo.workoutly.global.base.HEART_RATE
import com.ramo.workoutly.global.base.METABOLIC_RATE
import com.ramo.workoutly.global.base.MSG_IMG
import com.ramo.workoutly.global.base.MSG_TEXT
import com.ramo.workoutly.global.base.MSG_VID
import com.ramo.workoutly.global.base.PREF_DAYS_COUNT
import com.ramo.workoutly.global.base.SESSION_SCREEN_ROUTE
import com.ramo.workoutly.global.base.SLEEP
import com.ramo.workoutly.global.base.STEPS
import com.ramo.workoutly.global.util.ifTrue
import com.ramo.workoutly.global.util.logger
import io.androidpoet.dropdown.Dropdown
import io.androidpoet.dropdown.Easing
import io.androidpoet.dropdown.EnterAnimation
import io.androidpoet.dropdown.ExitAnimation
import io.androidpoet.dropdown.MenuItem
import io.androidpoet.dropdown.dropDownMenu
import io.androidpoet.dropdown.dropDownMenuColors
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.uri.VideoPlayerMediaItem
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    userPref: UserPref,
    deepLink: String?,
    statusColor: (Boolean, Int) -> Unit,
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
    val imagePicker = context.filePicker { url, type ->
        viewModel.setFile(url, type)
    }
    val requestPermissions = rememberLauncherForActivityResult(
        viewModel.healthKit.contract
    ) { granted ->
        if (granted.containsAll(viewModel.healthKit.permissions)) {
            findPreference(PREF_DAYS_COUNT) {
                viewModel.loadData(
                    userPref,
                    deepLink,
                    it?.toIntOrNull() ?: 3,
                    theme.isDarkMode,
                    onLink = { screen, route ->
                        scope.launch { navigateToScreen(screen, route) }
                    }
                ) {
                }
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
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            findPreference(PREF_DAYS_COUNT) {
                viewModel.loadData(
                    userPref,
                    deepLink,
                    it?.toIntOrNull() ?: 3,
                    theme.isDarkMode,
                    onLink = { screen, route ->
                        scope.launch { navigateToScreen(screen, route) }
                    }
                ) {
                    scope.launch {
                        requestPermissions.launch(viewModel.healthKit.permissions)
                    }
                }
            }
        } else {
            scope.launch {
                scaffoldState.showSnackbar("Permissions is required")
            }
        }
    }
    SideEffect {
        statusColor(theme.isDarkStatusBarText, theme.gradientColor.toArgb())
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                context.checkActivityRecognition({
                    findPreference(PREF_DAYS_COUNT) {
                        viewModel.loadData(
                            userPref,
                            deepLink,
                            it?.toIntOrNull() ?: 3,
                            theme.isDarkMode,
                            onLink = { screen, route ->
                                scope.launch { navigateToScreen(screen, route) }
                            }
                        ) {
                            scope.launch {
                                requestPermissions.launch(viewModel.healthKit.permissions)
                            }
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
                    statusColor(!theme.isDarkMode, theme.background.toArgb())
                    viewModel.setIsLiveVisible(true)
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
        Column(modifier = Modifier
            .padding(padding)
            .background(theme.backgroundGradient)
        ) {
            BarMainScreen(
                userPref = userPref,
                days = state.days,
                sortBy = state.sortBy,
                changeDays = { viewModel.setFilterDays(theme.isDarkMode, it) },
                changeSortBy = viewModel::setSortBy
            ) {

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
                                navigateToScreen.invoke(Screen.SessionRoute(item, state.days), SESSION_SCREEN_ROUTE)
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
        }
        state.isLiveVisible.ifTrue {
            LiveSessionSheet(state.messages, theme, {
                statusColor(theme.isDarkStatusBarText, theme.gradientColor.toArgb())
                viewModel.setIsLiveVisible(false)
            }, imagePicker) {

            }
        }
        LoadingScreen(state.isProcess, theme)
    }
}

@Composable
fun BarMainScreen(
    userPref: UserPref,
    theme: Theme = koinInject(),
    days: Int,
    sortBy: Int,
    changeDays: (Int) -> Unit,
    changeSortBy: (Int) -> Unit,
    signOut: () -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val menu = getMenuItems(days, sortBy)
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
                    onClick = { expanded.value = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = theme.textForGradientColor
                    )
                }
                Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                    Dropdown(
                        isOpen = expanded.value,
                        menu = menu,
                        colors = dropDownMenuColors(theme.background, theme.textColor),
                        onItemSelected = onItemSelected@{
                            logger("===", it.toString())
                            when(it ?: return@onItemSelected) {
                                in 1..30 -> changeDays(it)
                                51 -> changeSortBy(1)
                                52 -> changeSortBy(2)
                                53 -> changeSortBy(3)
                                54 -> changeSortBy(4)
                                55 -> changeSortBy(5)
                                -1 -> signOut()
                            }
                            expanded.value = false
                        },
                        onDismiss = { expanded.value = false },
                        offset = DpOffset(8.dp, 0.dp),
                        enter = EnterAnimation.SharedAxisYForward,
                        exit = ExitAnimation.SharedAxisYBackward,
                        easing = Easing.FastOutSlowInEasing,
                        enterDuration = 250,
                        exitDuration = 250
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
fun getMenuItems(days: Int, sortBy: Int): MenuItem<Int> {
    return remember(days, sortBy) {
        val menu = dropDownMenu {
            item(0, "Metrics Days") {
                icon(Icons.TwoTone.DateRange)
                item(1, "Today${if (days == 1) "     ✔   " else "         "}")
                item(3, "Last 3 days${if (days == 3) "     ✔   " else "         "}")
                item(7, "Last 7 days${if (days == 7) "     ✔   " else "         "}")
                item(14, "Last 14 days${if (days == 14) "     ✔   " else "         "}")
                item(30, "Last 30 days${if (days == 30) "     ✔   " else "         "}")
            }
            item(1, "Sort By") {
                icon(Icons.AutoMirrored.Filled.List)
                item(51, "Newer First${if (sortBy == 1) "     ✔   " else "         "}")
                item(52, "Views Count${if (sortBy == 2) "     ✔   " else "         "}")
                item(53, "Top Coaches${if (sortBy == 3) "     ✔   " else "         "}")
                item(54, "Likes ${if (sortBy == 4) "     ✔   " else "         "}")
                item(55, "Name${if (sortBy == 5) "     ✔   " else "         "}")
            }
            item(-1, "Sign out") {
                icon(Icons.AutoMirrored.TwoTone.ExitToApp)
            }
        }
        return@remember menu
    }
}

@Composable
fun FitnessMetricItem(metric: FitnessMetric, theme: Theme, invoke: () -> Unit) {
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
                        .padding(5.dp)
                ) subBox@{
                    Text(
                        text = exercise.title,
                        color = theme.textColor,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 14.sp,
                        maxLines = 2, //1
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.Start)
                    )
                    /*Text(
                        text = exercise.description,
                        color = theme.textGrayColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 3.dp)
                    )*/
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

@Composable
fun LiveSessionSheet(
    messages: List<Message>,
    theme: Theme,
    onDismissRequest: (Boolean) -> Unit,
    filePicker: () -> Unit,
    send: (String) -> Unit,
) {
    val sheetModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest(false) },
        sheetState = sheetModalState,
        properties = ModalBottomSheetProperties(),
        containerColor = theme.background,
        contentColor = theme.textColor,
        dragHandle = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(theme.backDark),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .padding(top = 22.dp),
                    color = theme.textGrayColor,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Box(
                        Modifier
                            .size(
                                width = 32.dp,
                                height = 4.0.dp
                            )
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp), text = "Live Session", color = theme.textColor,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(5.dp))
            }
        }
    ) {
        ChatView(messages, theme, filePicker = filePicker, send)
    }
}

@Composable
fun ChatView(
    messages: List<Message>,
    theme: Theme,
    filePicker: () -> Unit,
    send: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    val chatText = remember { mutableStateOf("") }
    LaunchedEffect(messages) {
        if (messages.lastIndex > 1) {
            scrollState.scrollToItem(messages.lastIndex)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(start = 5.dp, end = 5.dp, bottom = 7.dp),
            state = scrollState,
        ) {
            items(messages) { msg ->
                Box {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(if (!msg.isFromCurrentUser) Alignment.CenterStart else Alignment.CenterEnd)
                    )
                    MessageItem(msg, theme)
                }
            }
            item {
                Spacer(Modifier.height(60.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    TrailingIcons(theme, filePicker) {
                        chatText.value.also {
                            chatText.value = ""
                        }.also(send)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = theme.backDark,
                    unfocusedContainerColor = theme.backDark,
                ),
                textStyle = TextStyle(color = theme.textColor, textDirection = TextDirection.Content),
                value = chatText.value,
                onValueChange = {
                    chatText.value = it
                },
                singleLine = false
            )
        }
    }
}

@Composable
fun BoxScope.MessageItem(msg: Message, theme: Theme) {
    val colorCard = remember {
        if (msg.isFromCurrentUser) {
            theme.gradientColor
        } else {
            theme.gradientSec
        }
    }
    val colorText = remember {
        if (msg.isFromCurrentUser) {
            theme.textForGradientColor
        } else {
            theme.textColor
        }
    }
    val isPopUp = remember { mutableStateOf(false) }
    val isPlayed = remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .widthIn(0.dp, 300.dp)
            .align(if (!msg.isFromCurrentUser) Alignment.CenterStart else Alignment.CenterEnd)
            .padding(5.dp),
        shape = RoundedCornerShape(20.dp),
        color = colorCard,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
    ) {
        Column(Modifier.padding(3.dp)) {
            if (!msg.isFromCurrentUser) {
                Text(
                    msg.senderName,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 2.5.dp)
                        .alpha(0.8F),
                    color = colorText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            when(msg.type) {
                MSG_TEXT -> {
                    Text(
                        msg.message,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(10.dp),
                        color = colorText,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                MSG_IMG -> {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(300.dp)) {
                        coil.compose.SubcomposeAsyncImage(
                            model = LocalContext.current.imageBuildr(msg.fileUrl),
                            success = { (painter, _) ->
                                AnimatedFadeOnce(duration = 200, label = msg.id.toString() + msg.type) {
                                    Image(
                                        contentScale = ContentScale.Crop,
                                        painter = painter,
                                        contentDescription = "Image",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .background(Color.Transparent)
                                            .height(300.dp)
                                            .clickable {
                                                isPopUp.value = true
                                            }
                                    )
                                }
                                isPopUp.value.ifTrue {
                                    ImageViewer(painter) {
                                        isPopUp.value = false
                                    }
                                }
                            },
                            error = {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = "aa")
                            },
                            onError = {
                            },
                            contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.None,
                            contentDescription = "Image"
                        )
                    }
                }
                MSG_VID -> {
                    val painter = rememberAsyncImagePainter(
                        model = msg.fileUrl,
                        imageLoader = LocalContext.current.videoImageBuildr,
                    )
                    AnimatedFadeOnce(
                        Modifier
                            .fillMaxWidth()
                            .clip(
                                shape = RoundedCornerShape(20.dp)
                            )
                            .height(200.dp)
                            .fillMaxWidth()
                            .clickable {
                                isPlayed.value = true
                            },
                        contentAlignment = Alignment.Center,
                        label = msg.id.toString() + msg.type
                    ) {
                        Image(
                            contentScale = ContentScale.Crop,
                            painter = painter,
                            contentDescription = "Image",
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(50, 50, 50).copy(alpha = 0.5F))
                        )
                        Icon(
                            Icons.Filled.PlayArrow,
                            tint = Color.White,
                            contentDescription = "play"
                        )
                        isPlayed.value.ifTrue {
                            val videoItems = remember(msg.fileUrl) { msg.let { listOf(videoItem(it.fileUrl, it.senderName)) } }
                            VideoViewer(videoItems) {
                                isPlayed.value = false
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrailingIcons(
    theme: Theme,
    filePicker: () -> Unit,
    send: () -> Unit,
) {
    Row {
        IconButton(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .align(Alignment.CenterVertically)
                .padding(5.dp),
            onClick = filePicker
        ) {
            Icon(
                rememberFile(color = theme.textGrayColor),
                tint = theme.textGrayColor,
                contentDescription = "Attach File",
            )
        }
        IconButton(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .align(Alignment.CenterVertically)
                .padding(5.dp),
            onClick = send
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                tint = theme.textGrayColor,
                contentDescription = "Send",
            )
        }
    }
}

@Composable
fun ImageViewer(painter: Painter, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(rememberZoomState()),
            )
            BackButtonLess(Color.White, onClose)
        }
    }
}

@Composable
fun VideoViewer(videoItems: List<VideoPlayerMediaItem>, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            val videoConfig = remember { videoConfig.copy(showFullScreenButton = false) }
            VideoPlayer(
                mediaItems = videoItems,
                handleLifecycle = true,
                autoPlay = false,
                usePlayerController = true,
                enablePip = true,
                handleAudioFocus = true,
                controllerConfig = videoConfig,
                repeatMode = RepeatMode.ALL,
                modifier = Modifier.fillMaxSize(),
            )
            BackButtonLess(Color.White, onClose)
        }
    }
}