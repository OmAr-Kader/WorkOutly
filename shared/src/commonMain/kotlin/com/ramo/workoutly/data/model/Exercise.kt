package com.ramo.workoutly.data.model

import com.ramo.workoutly.data.util.BaseObject
import com.ramo.workoutly.global.util.formatMillisecondsToHours
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@kotlinx.serialization.Serializable
data class Exercise(
    val id: String, //generateUniqueId
    val title: String,
    val videoUri: String,
    val length: Long,
    val description: String,
    val date: String,
    @kotlinx.serialization.Transient
    val isLiked: Boolean = false,
    @kotlinx.serialization.Transient
    val likes: Int = 0,
    @kotlinx.serialization.Transient
    val views: Long = 0,
    val _rid: String = "",
    val _ts: String = "",
    val _self: String = "",
    val _etag: String = "",
    val _attachments: String = "",
): BaseObject() {

    constructor() : this("", "", "", 0L,"",  "")

    val lengthStr: String = length.formatMillisecondsToHours

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }

    override fun jsonPost(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject.toMutableMap().apply {
            remove("_rid")
            remove("_ts")
            remove("_self")
            remove("_etag")
            remove("_attachments")
        }.let(::JsonObject)
    }
}

@kotlinx.serialization.Serializable
data class ExerciseViews(
    val id: String,
    val exerciseId: Long,
    val count: Long
): BaseObject() {

    @Suppress("unused")
    constructor() : this("", 0L, 0L)

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }
}

@kotlinx.serialization.Serializable
data class ExerciseLikes(
    val id: String,
    val exerciseId: Long,
    val liker: List<Long>,
    val count: Long
): BaseObject() {

    @Suppress("unused")
    constructor() : this("", 0L, emptyList(),0L)

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }
}