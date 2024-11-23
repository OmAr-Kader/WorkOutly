package com.ramo.workoutly.data.model

@kotlinx.serialization.Serializable
data class Message(
    val id: String = "",
    val userId: Long = 0L,
    val senderName: String = "",
    val message: String = "",
    val fileUrl: String = "",
    val type: Int = 0,
    @kotlinx.serialization.Transient
    val isFromCurrentUser: Boolean = false,
) {
    constructor() : this("", 0L, "", "", "", 0)
}