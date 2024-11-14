package com.ramo.workoutly.data.model

@kotlinx.serialization.Serializable
data class Message(
    val id: Long = 0L,
    val userId: Long = 0L,
    val senderName: String = "",
    val message: String = "",
    val fileUrl: String = "",
    val type: Int = 0,
    val isFromCurrentUser: Boolean = false,
) {
    constructor() : this(0L, 0L, "", "", "", 0)
}