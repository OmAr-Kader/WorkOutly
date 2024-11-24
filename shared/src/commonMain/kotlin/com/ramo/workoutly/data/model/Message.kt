package com.ramo.workoutly.data.model

import com.ramo.workoutly.data.util.BaseObject
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@kotlinx.serialization.Serializable
data class Message(
    val id: String = "",
    @kotlinx.serialization.SerialName("user_id")
    val userId: String = "",
    @kotlinx.serialization.SerialName("sender_name")
    val senderName: String = "",
    val message: String = "",
    @kotlinx.serialization.SerialName("file_url")
    val fileUrl: String = "",
    val type: Int = 0,
    val session: String = "", // GMT And Drop min and sec
    val date: String = "",
    @kotlinx.serialization.Transient
    val isFromCurrentUser: Boolean = false,
    val _rid: String = "",
    val _ts: String = "",
    val _self: String = "",
    val _etag: String = "",
    val _attachments: String = "",
): BaseObject() {
    constructor() : this("", "", "", "", "", 0, "", "")

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }

    override fun jsonCreate(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject.toMutableMap().apply {
            remove("_rid")
            remove("_ts")
            remove("_self")
            remove("_etag")
            remove("_attachments")
        }.let(::JsonObject)
    }
}