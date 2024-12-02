package com.ramo.workoutly.data.model

@kotlinx.serialization.Serializable
data class FitnessMetric(
    val id: Int, //recordId
    val title: String,
    val iconColor: Long,
    val value: Long,
    val valueUnit: String,
    @kotlinx.serialization.Transient
    val valueStr: String = value.toString() + valueUnit
) {
    constructor() : this(0, "", 0L, 0L, "", "")
}

@kotlinx.serialization.Serializable
data class FitnessHistoryMetric(
    val milliSec: Long,
    val date: String,
    val value: Long,
    val valueUnit: String,
    @kotlinx.serialization.Transient
    val isMoreThanPreviousSession: Boolean = true
) {
    constructor() : this(0L, "", 0L, "", true)

    val valueStr = value.toString() + valueUnit
}