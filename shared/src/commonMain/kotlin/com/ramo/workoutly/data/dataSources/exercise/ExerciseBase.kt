package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.ExerciseLikes
import com.ramo.workoutly.data.model.ExerciseViews

class ExerciseBase(
    private val repo: ExerciseRepo
) {

    suspend fun createExercise(exercise: Exercise): Exercise? = repo.createExercise(exercise)
    suspend fun getExercise(id: String): Exercise? = repo.getExercise(id)
    suspend fun fetchExercises(): List<Exercise> = repo.fetchExercises()
    suspend fun updateExercise(exercise: Exercise): Exercise? = repo.updateExercise(exercise)
    suspend fun deleteExercise(id: String): Int = repo.deleteExercise(id)
    suspend fun getExerciseViews(exerciseId: String): ExerciseViews? = repo.getExerciseViews(exerciseId)
    suspend fun getExerciseLikes(exerciseId: String): ExerciseLikes? = repo.getExerciseLikes(exerciseId)
    suspend fun incrementViewCount(exerciseId: String): ExerciseViews? = repo.incrementViewCount(exerciseId)
    suspend fun addLike(exerciseId: String, likerId: String): ExerciseLikes? = repo.addLike(exerciseId, likerId)
}