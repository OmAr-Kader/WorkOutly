package com.ramo.workoutly.data.model

import com.ramo.workoutly.data.util.BaseObject
import com.ramo.workoutly.global.util.fullFormatMillisecondsToHours
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@kotlinx.serialization.Serializable
data class Exercise(
    val id: String = "", //generateUniqueId
    val date: String = "",
    val title: String = "",
    @kotlinx.serialization.SerialName("video_url")
    val videoUrl: String = "",
    val length: Long = 0L,
    val cato: String = "",
    @kotlinx.serialization.SerialName("views")
    val views: Long,
    val liker: List<String>,
    val description: String = "",
    @kotlinx.serialization.Transient
    val isLiked: Boolean = false,
    @kotlinx.serialization.Transient
    val likes: Int = liker.size,
): BaseObject() {

    constructor() : this("", "","", "", 0L, "",  0L, emptyList(), "")

    val lengthStr: String = length.fullFormatMillisecondsToHours

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }
}