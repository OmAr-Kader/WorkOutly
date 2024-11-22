package com.ramo.workoutly.data.model

import com.ramo.workoutly.global.util.formatMillisecondsToHours

@kotlinx.serialization.Serializable
data class Exercise(
    val id: Long,
    val title: String,
    val videoUri: String,
    val description: String,
    val views: Long,
    val length: Long
) {
    constructor() : this(0L, "", "", "", 0, 0)

    val lengthStr: String = length.formatMillisecondsToHours
}

@kotlinx.serialization.Serializable
data class ExerciseSum(
    val id: Long,
    val exerciseId: Long,
    val count: Long
) {
    constructor() : this(0L, 0L, 0L)
}