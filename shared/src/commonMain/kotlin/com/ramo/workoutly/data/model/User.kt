package com.ramo.workoutly.data.model

import com.ramo.workoutly.data.util.BaseObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class User(
    @SerialName("id")
    val id: Long = 0,
    @SerialName("auth_id")
    val authId: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("phone")
    val phone: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("profile_picture")
    val profilePicture: String = "",
): BaseObject() {

    constructor() : this(0L, "", "", "", "", "")

    override fun json(): JsonObject {
        return kotlinx.serialization.json.Json.encodeToJsonElement(this.copy()).jsonObject.toMutableMap().apply {
            remove("id")
        }.let(::JsonObject)
    }
}

@Serializable
data class UserPref(
    @SerialName("auth_id")
    val authId: String = "",
    @SerialName("id")
    val id: Long = 0L,// Not From Supabase userInfo()
    @SerialName("email")
    val email: String = "",
    @SerialName("phone")
    val phone: String = "",
    @Transient
    val name: String = "",
    @Transient
    val profilePicture: String = ""
) {
    constructor() : this("", 0L, "", "", "", "")
}
