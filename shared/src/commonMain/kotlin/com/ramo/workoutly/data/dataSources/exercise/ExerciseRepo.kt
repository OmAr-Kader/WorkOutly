package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise

interface ExerciseRepo {

    suspend fun createExercise(exercise: Exercise): Exercise?
    suspend fun getExercise(id: String): Exercise?
    suspend fun fetchExercises(): List<Exercise>
    suspend fun fetchExercisesAllData(): List<Exercise>
    suspend fun deleteExercise(id: String): Int
    suspend fun incrementViewCount(exercise: Exercise): Int
    suspend fun addLike(exercise: Exercise, userId: String): Int
    suspend fun removeLike(exercise: Exercise, userId: String): Int
}