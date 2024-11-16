package com.ramo.workoutly.data.dataSources.pref

import com.ramo.workoutly.data.model.Preference

interface PrefRepo {

    suspend fun prefs(): List<Preference>

    suspend fun prefs(invoke: (List<Preference>) -> Unit)

    suspend fun insertPref(pref: Preference): Preference?

    suspend fun insertPref(prefs: List<Preference>): List<Preference>?

    suspend fun updatePref(pref: Preference, newValue: String): Preference

    suspend fun updatePref(prefs: List<Preference>): List<Preference>

    suspend fun deletePref(key: String): Int

    suspend fun deletePrefAll(): Int

}