package com.ramo.workoutly.data.util

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

abstract class BaseObject : Any() {
    abstract fun json(): JsonObject
    open fun jsonCreate(): JsonObject {
        return json()
    }
}

@kotlinx.serialization.Serializable
data class QueryListAzure<T>(
    @kotlinx.serialization.SerialName("Documents")
    val list: List<T>
) : BaseObject() {
    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject
    }
}