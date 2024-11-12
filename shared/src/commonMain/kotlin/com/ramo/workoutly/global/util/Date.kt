@file:Suppress("unused")

package com.ramo.workoutly.global.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

inline val dateNow: String
    get() = kotlinx.datetime.Clock.System.now().toString()

inline val dateBeforeHourInstant: Instant
    get() = kotlinx.datetime.Clock.System.now().toEpochMilliseconds().let {
        Instant.fromEpochMilliseconds(it - 3600000)
    }

inline val dateBeforeHour: String
    get() = dateBeforeHourInstant.toString()

inline val dateNowMills: Long
    get() = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()

val Long.toInstant: Instant get() = Instant.fromEpochMilliseconds(this)

val String.dateInstant: Instant get() = Instant.parse(this)

val Instant.dateStr: String
    get() = toLocalDateTime(TimeZone.currentSystemDefault()).toString()

val Instant.dateTime: LocalDateTime get() = toLocalDateTime(TimeZone.currentSystemDefault())

val LocalDateTime.timestamp: String get() {
    return format(LocalDateTime.Format {
        byUnicodePattern("MM/dd HH:mm")
    })
}

val Long.toTimestamp: String get() {
    return toInstant.dateTime.format(LocalDateTime.Format {
        byUnicodePattern("MM/dd HH:mm")
    })
}

val Long.toHoursMinSecs : String get() {
    return toInstant.dateTime.format(LocalDateTime.Format {
        byUnicodePattern("HH:mm:ss")
    })
}


val Long.formatMillisecondsToHours: String get () {
    val hours = this / 3_600_000
    val minutes = (this % 3_600_000) / 60_000
    val seconds = (this % 60_000) / 1_000
    val hoursStr = if (hours < 10) "0$hours" else "$hours"
    val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
    val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"

    return "$hoursStr:$minutesStr:$secondsStr"
}