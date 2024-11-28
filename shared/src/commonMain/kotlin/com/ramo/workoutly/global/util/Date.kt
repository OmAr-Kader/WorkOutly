@file:Suppress("unused")

package com.ramo.workoutly.global.util

import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

inline val dateNow: String
    get() = kotlinx.datetime.Clock.System.now().toString()

inline val dateNowUTC: String
    get() = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC).toString()

inline val dateBeforeHourInstant: kotlinx.datetime.Instant
    get() = kotlinx.datetime.Clock.System.now().toEpochMilliseconds().let {
        kotlinx.datetime.Instant.fromEpochMilliseconds(it - 3600000)
    }

inline val dateBeforeHour: String
    get() = dateBeforeHourInstant.toString()

inline val dateNowMills: Long
    get() = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()

inline val dateNowUTCMILLS: Long
    get() = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC).toInstant(kotlinx.datetime.UtcOffset.ZERO).toEpochMilliseconds()

inline val dateNowUTCMILLSOnlyTour: String
    get() {
        return kotlinx.datetime.Clock.System.now().minus(1, kotlinx.datetime.DateTimeUnit.HOUR).toLocalDateTime(kotlinx.datetime.TimeZone.UTC).run {
            kotlinx.datetime.LocalDateTime(year, month, dayOfMonth, hour, minute, second)
        }.format(kotlinx.datetime.LocalDateTime.Format {
            byUnicodePattern("uuuu/MM/dd HH")
        })
    }

/*inline val dateNowUTCMILLSOnlyTour: String
    get() = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC).format(kotlinx.datetime.LocalDateTime.Format {
        byUnicodePattern("uuuu/MM/dd HH")
    })
*/
inline val dateNowOnlyTour: String
    get() = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.UTC).let { time ->
        val hour = time.hour % 12
        val adjustedHour = if (hour == 0) 12 else hour
        val period = if (time.hour < 12) "AM" else "PM"
        "$adjustedHour $period"
    }

val Long.toInstant: kotlinx.datetime.Instant get() = kotlinx.datetime.Instant.fromEpochMilliseconds(this)

val String.dateInstant: kotlinx.datetime.Instant get() = kotlinx.datetime.Instant.parse(this)

val kotlinx.datetime.Instant.dateStr: String
    get() = toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).toString()

val kotlinx.datetime.Instant.dateTime: kotlinx.datetime.LocalDateTime get() = toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())

val kotlinx.datetime.LocalDateTime.timestamp: String get() {
    return format(kotlinx.datetime.LocalDateTime.Format {
        byUnicodePattern("MM/dd HH:mm")
    })
}

val Long.toTimestamp: String get() {
    return toInstant.dateTime.format(kotlinx.datetime.LocalDateTime.Format {
        byUnicodePattern("MM/dd HH:mm")
    })
}

val String.toTimestampHourMin: String get() {
    return dateInstant.dateTime.format(kotlinx.datetime.LocalDateTime.Format {
        byUnicodePattern("HH:mm")
    })
}

val Long.toTimestampWithDays: String get() {
    return toInstant.dateTime.let { date ->
        date.dayOfWeek.name.substring(0, 3).uppercase().let {
            "$it, " + date.format(kotlinx.datetime.LocalDateTime.Format {
                byUnicodePattern("MM/dd HH:mm")
            })
        }
    }
}

val Long.toTimestampYear: String get() {
    return toInstant.dateTime.format(kotlinx.datetime.LocalDateTime.Format {
        byUnicodePattern("uuuu/MM/dd HH:mm")
    })
}

// Duration
val Long.formatMillisecondsToHours: String get () {
    val hours = this / 3_600_000
    val minutes = (this % 3_600_000) / 60_000
    val seconds = (this % 60_000) / 1_000
    val hoursStr = when {
        hours == 0L -> ""
        hours < 10L -> "0$hours:"
        else -> "$hours:"
    }
    val minutesStr = when {
        minutes == 0L -> ""
        minutes < 10L -> "0$minutes:"
        else -> "$minutes:"
    }
    val secondsStr = if (seconds < 10L) "0$seconds" else "$seconds"
    return "$hoursStr$minutesStr$secondsStr"
}

// Duration
val Long.fullFormatMillisecondsToHours: String get () {
    val hours = this / 3_600_000
    val minutes = (this % 3_600_000) / 60_000
    val seconds = (this % 60_000) / 1_000
    val hoursStr = when {
        hours == 0L -> ""
        hours < 10L -> "0$hours:"
        else -> "$hours:"
    }
    val minutesStr = if (minutes < 10L) "0$minutes:" else "$minutes:"
    val secondsStr = if (seconds < 10L) "0$seconds" else "$seconds"
    return "$hoursStr$minutesStr$secondsStr"
}

suspend fun observeSpecificTime(targetTime: kotlinx.datetime.LocalDateTime, onTimeReached: () -> Unit) {
    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
        val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
        val durationMillis = targetTime.toInstant(
            kotlinx.datetime.TimeZone.currentSystemDefault()
        ).toEpochMilliseconds() - now.toInstant(kotlinx.datetime.TimeZone.currentSystemDefault()).toEpochMilliseconds()

        if (durationMillis > 0) {
            kotlinx.coroutines.delay(durationMillis) // Wait until the target time
            kotlinx.coroutines.coroutineScope {
                onTimeReached()
            }
        }
    }
}

suspend fun observeNextHour(onTimeReached: () -> Unit) {
    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
        val now = kotlinx.datetime.Clock.System.now()
        val nextHour = now.plus(1, kotlinx.datetime.DateTimeUnit.HOUR).toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).run {
            kotlinx.datetime.LocalDateTime(year, month, dayOfMonth, hour, 0, 0)
        }
        val durationMillis = nextHour.toInstant(
            kotlinx.datetime.TimeZone.currentSystemDefault()
        ).toEpochMilliseconds() - now.toEpochMilliseconds()

        if (durationMillis > 0) {
            kotlinx.coroutines.delay(20000) // Wait until the target time
            kotlinx.coroutines.coroutineScope {
                onTimeReached()
            }
        }
    }
}

