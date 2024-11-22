package com.ramo.workoutly.data.model

@kotlinx.serialization.Serializable
data class FitnessHistoryMetric(
    val milliSec: Long,
    val data: String,
    val value: Long,
    val valueUnit: String,
    val isMoreThanPreviousSession: Boolean = true
) {
    constructor() : this(0L, "", 0L, "", true)

    val valueStr = value.toString() + valueUnit
}