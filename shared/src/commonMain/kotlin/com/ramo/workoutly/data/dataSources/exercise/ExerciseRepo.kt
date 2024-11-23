package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.ExerciseLikes
import com.ramo.workoutly.data.model.ExerciseViews

interface ExerciseRepo {

    suspend fun createExercise(exercise: Exercise): Exercise?
    suspend fun getExercise(id: String): Exercise?
    suspend fun fetchExercises(): List<Exercise>
    suspend fun updateExercise(exercise: Exercise): Exercise?
    suspend fun deleteExercise(id: String): Int
    suspend fun getExerciseViews(exerciseId: String): ExerciseViews?
    suspend fun getExerciseLikes(exerciseId: String): ExerciseLikes?
    suspend fun incrementViewCount(exerciseId: String): ExerciseViews?
    suspend fun addLike(exerciseId: String, likerId: String): ExerciseLikes?

}