package com.ramo.workoutly.android.global.util

internal val android.content.Context.isDarkMode: Boolean
    get() {
        val nightModeFlags: Int =
            resources.configuration.uiMode and
                    android.content.res.Configuration.UI_MODE_NIGHT_MASK
        return when (nightModeFlags) {
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> true
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> false
            else -> true
        }
    }

internal val android.content.Context.isTablet: Boolean
    get() {
        return resources.configuration.screenLayout and android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK >=
                android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
    }

internal val android.content.Context.imageBuildr: (String) -> coil.request.ImageRequest
    get() = {
        coil.request.ImageRequest.Builder(this@imageBuildr)
            .data(it)
            .diskCacheKey(it)
            //.addLastModifiedToFileCacheKey(true)
            .networkCachePolicy(coil.request.CachePolicy.ENABLED)
            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
            .crossfade(true)
            .build()
    }

val String.videoType: String
    get() = reversed().split(".").getOrNull(0)?.reversed() ?: ""

/*
* val painter = rememberAsyncImagePainter(
    model = state.course.briefVideo,
    imageLoader = context.videoImageBuildr,
)*/
internal val android.content.Context.videoImageBuildr: coil.ImageLoader
    get() = coil.ImageLoader.Builder(this@videoImageBuildr)
        .components {
            add(coil.decode.VideoFrameDecoder.Factory())
        }
        //.addLastModifiedToFileCacheKey(true)
        .networkCachePolicy(coil.request.CachePolicy.ENABLED)
        .diskCachePolicy(coil.request.CachePolicy.ENABLED)
        .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
        .crossfade(true)
        .build()

fun android.content.Context.getMimeTypeExtension(uri: android.net.Uri): String = kotlin.runCatching {
    val extension = if (uri.scheme == android.content.ContentResolver.SCHEME_CONTENT) {
        val mime = android.webkit.MimeTypeMap.getSingleton()
        mime.getExtensionFromMimeType(contentResolver.getType(uri)) ?: ""
    } else {
        android.webkit.MimeTypeMap.getFileExtensionFromUrl(android.net.Uri.fromFile(java.io.File(uri.path ?: return@runCatching "")).toString())
    }
    return@runCatching ".$extension"
}.getOrDefault("")

fun getMimeType(extension: String): String? = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(
    extension.replace(".", "") // @OmAr-Kader Where i added before |Lock Up|
)

fun isImage(extension: String): Boolean = getMimeType(extension = extension)?.startsWith("image") == true

fun isVideo(extension: String): Boolean = getMimeType(extension = extension)?.startsWith("video") == true

@androidx.compose.runtime.Composable
fun android.content.Context.filePicker(
    invoke: (android.net.Uri, type: Int, extension: String) -> Unit,
): () -> Unit {
    val photoPicker = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            val extension = getMimeTypeExtension(it)
            val isImage = if (isImage(extension)) {
                com.ramo.workoutly.global.base.MSG_IMG
            } else if (isVideo(extension)) {
                com.ramo.workoutly.global.base.MSG_VID
            } else {
                null
            }
            isImage?.let { it1 -> invoke.invoke(it, it1, extension) }
        }
    }
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            photoPicker.launch(
                androidx.activity.result.PickVisualMediaRequest(
                    androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo
                )
            )
        }
    }
    return androidx.compose.runtime.remember {
        return@remember {
            when {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                }
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU -> {
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO)
                }
                else -> {
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }.let { per ->
                per.all {
                    androidx.core.content.ContextCompat.checkSelfPermission(this@filePicker,it) ==
                            android.content.pm.PackageManager.PERMISSION_GRANTED
                }.let { isGranted ->
                    if (isGranted) {
                        photoPicker.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo
                            )
                        )
                    } else {
                        launcher.launch(per)
                    }
                }
            }
        }
    }
}



@androidx.compose.runtime.Composable
fun android.content.Context.filePickerOnlyImage(
    invoke: (android.net.Uri, extension: String) -> Unit,
): () -> Unit {
    val photoPicker = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            val extension = getMimeTypeExtension(it)
            invoke.invoke(it, extension)
        }
    }
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            photoPicker.launch(
                androidx.activity.result.PickVisualMediaRequest(
                    androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo
                )
            )
        }
    }
    return androidx.compose.runtime.remember {
        return@remember {
            when {
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                }
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU -> {
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO)
                }
                else -> {
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }.let { per ->
                per.all {
                    androidx.core.content.ContextCompat.checkSelfPermission(this@filePickerOnlyImage, it) ==
                            android.content.pm.PackageManager.PERMISSION_GRANTED
                }.let { isGranted ->
                    if (isGranted) {
                        photoPicker.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
                            )
                        )
                    } else {
                        launcher.launch(per)
                    }
                }
            }
        }
    }
}


val videoItem: (String, String) -> io.sanghun.compose.video.uri.VideoPlayerMediaItem
    get() = { videoUri, videoTitle ->
        io.sanghun.compose.video.uri.VideoPlayerMediaItem.NetworkMediaItem(
            url = videoUri,
            mediaMetadata = androidx.media3.common.MediaMetadata.Builder().setSubtitle(videoTitle).setAlbumTitle(videoTitle)
                .setDisplayTitle(videoTitle).build(),
            mimeType = videoUri.videoType,
        )
    }

val videoConfig: io.sanghun.compose.video.controller.VideoPlayerControllerConfig
    get() {
        return io.sanghun.compose.video.controller.VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = true,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = false,//true
            showNextTrackButton = false,//true
            showRepeatModeButton = false,//true
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = true,
        )
    }

fun android.content.Context.shareLink(url: String) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain" // Specify the MIME type
        putExtra(android.content.Intent.EXTRA_TEXT, url) // Add the URL as extra text
    }
    val chooser = android.content.Intent.createChooser(intent, "Share link via")
    startActivity(chooser)
}


suspend fun android.content.Context.getByteArrayFromUri(uri: android.net.Uri): ByteArray? {
    return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
        return@withContext contentResolver.openInputStream(uri)?.use { inputStream ->
            val byteArrayOutputStream = java.io.ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, length)
            }
            byteArrayOutputStream.toByteArray()
        }
    }
}

fun android.content.Context.getVideoDuration(videoPath: android.net.Uri): Long {
    return try {
        return android.media.MediaPlayer.create(
            this@getVideoDuration,
            videoPath
        ).let { player ->
            player.duration.toLong().also {
                player.reset()
                player.release()
            }
        }
    } catch (e: Exception) {
        // Handle exception
        0
    }
}


