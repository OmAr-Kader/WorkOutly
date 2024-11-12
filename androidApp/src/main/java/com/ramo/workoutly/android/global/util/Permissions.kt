package com.ramo.workoutly.android.global.util

inline fun android.content.Context.checkLocationPermission(invoke: () -> Unit, failed: (Array<String>) -> Unit) {
    if (androidx.core.app.ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED || androidx.core.app.ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        invoke()
    } else {
        failed(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }
}

inline fun android.content.Context.checkActivityRecognition(invoke: () -> Unit, failed: (Array<String>) -> Unit) {
    if (androidx.core.app.ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED || androidx.core.app.ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.BODY_SENSORS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        invoke()
    } else {
        failed(
            arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION, android.Manifest.permission.BODY_SENSORS)
        )
    }
}