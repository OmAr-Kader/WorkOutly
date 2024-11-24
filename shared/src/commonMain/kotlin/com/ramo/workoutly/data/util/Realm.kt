package com.ramo.workoutly.data.util

import com.ramo.workoutly.data.model.Preference

const val CLOUD_SUCCESS: Int = 1
const val CLOUD_FAILED: Int = -1

inline val listOfOnlyLocalSchemaRealmClass: Set<kotlin.reflect.KClass<out io.realm.kotlin.types.TypedRealmObject>>
    get() = setOf(Preference::class)