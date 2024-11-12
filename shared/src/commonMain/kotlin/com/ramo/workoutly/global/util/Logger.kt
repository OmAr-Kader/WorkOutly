package com.ramo.workoutly.global.util

import com.ramo.workoutly.di.getKoinInstance

fun logger(tag: String = "", error: String) {
    getKoinInstance<org.lighthousegames.logging.KmLog>().w(tag = "==> $tag") { error }
}
/*
fun loggerError(tag: String = "", error: String) {
    com.ramo.workoutly.di.getKoinInstance<org.lighthousegames.logging.KmLog>().w(tag = "==> $tag") { error }
}*/

fun loggerError(tag: String = "", error: Throwable) {
    getKoinInstance<org.lighthousegames.logging.KmLog>().e(tag = "==> $tag") { error.stackTraceToString() }
}