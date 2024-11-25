package com.ramo.workoutly.data.util

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

abstract class BaseObject : Any() {
    abstract fun json(): JsonObject

    fun jsonStr(): String {
        return json().toString()
    }
}

@kotlinx.serialization.Serializable
data class QueryListAWS<T>(
    @kotlinx.serialization.SerialName("items")
    val list: List<T>
) : BaseObject() {
    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }
}