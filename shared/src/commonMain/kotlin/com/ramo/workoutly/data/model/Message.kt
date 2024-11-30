package com.ramo.workoutly.data.model

import com.ramo.workoutly.data.util.BaseObject
import com.ramo.workoutly.global.util.toTimestampHourMinAMPM
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@kotlinx.serialization.Serializable
data class Message(
    val id: String,
    @kotlinx.serialization.SerialName("user_id")
    val userId: String,
    @kotlinx.serialization.SerialName("sender_name")
    val senderName: String,
    val message: String,
    @kotlinx.serialization.SerialName("file_url")
    val fileUrl: String,
    val type: Int,
    val session: String, // GMT And Drop min and sec
    val date: String,
    @kotlinx.serialization.Transient
    val isFromCurrentUser: Boolean = false
): BaseObject() {

    constructor() : this("", "", "", "", "", 0, "", "")

    val dateStr: String = date.toTimestampHourMinAMPM

    constructor(aws: MessageAWS) : this(aws.id.value, aws.userId.value, aws.senderName.value, aws.message.value, aws.fileUrl.value, aws.type.asInt(), aws.senderName.value, aws.date.value)

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }
}


data class DataResponse(
    val action: String,
    val data: MessageAWS
)

@kotlinx.serialization.Serializable
data class MessageAWS(
    val id: StringWrapper,
    @kotlinx.serialization.SerialName("user_id")
    val userId: StringWrapper = StringWrapper(""),
    @kotlinx.serialization.SerialName("sender_name")
    val senderName: StringWrapper = StringWrapper(""),
    val message: StringWrapper = StringWrapper(""),
    @kotlinx.serialization.SerialName("file_url")
    val fileUrl: StringWrapper = StringWrapper(""),
    val type: NumberWrapper = NumberWrapper("0"),
    val session: StringWrapper = StringWrapper(""), // GMT And Drop min and sec
    val date: StringWrapper = StringWrapper("")
)

@kotlinx.serialization.Serializable
data class StringWrapper(
    @kotlinx.serialization.SerialName("S")
    val value: String
)

@kotlinx.serialization.Serializable
data class NumberWrapper(
    @kotlinx.serialization.SerialName("N")
    val value: String
) {
    // Convert N to a number if needed
    fun asInt() = value.toInt()
    fun asDouble() = value.toDouble()
}
