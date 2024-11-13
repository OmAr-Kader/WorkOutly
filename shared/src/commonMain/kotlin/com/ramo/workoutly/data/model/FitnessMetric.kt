package com.ramo.workoutly.data.model

@kotlinx.serialization.Serializable
data class FitnessMetric(
    val recordId: Int,
    val title: String,
    val iconColor: Long,
    val value: Long,
    val valueUnit: String,
    val valueStr: String = value.toString() + valueUnit
) {
    constructor() : this(0, "", 0L, 0L, "", "")
}
