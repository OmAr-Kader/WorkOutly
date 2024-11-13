package com.ramo.workoutly.data.model

@kotlinx.serialization.Serializable
data class FitnessHistoryMetric(
    val data: String,
    val value: Long,
    val valueUnit: String,
) {
    constructor() : this("", 0L, "")

    val valueStr = value.toString() + valueUnit
}