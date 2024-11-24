package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise

class ExerciseBase(
    private val repo: ExerciseRepo
) {

    suspend fun createExercise(exercise: Exercise): Exercise? = repo.createExercise(exercise)
    suspend fun getExercise(id: String): Exercise? = repo.getExercise(id)
    suspend fun fetchExercises(): List<Exercise> = repo.fetchExercises()
    suspend fun fetchExercisesAllData(): List<Exercise> = repo.fetchExercisesAllData()
    suspend fun deleteExercise(id: String): Int = repo.deleteExercise(id)
    suspend fun incrementViewCount(exercise: Exercise): Int = repo.incrementViewCount(exercise)
    suspend fun addLike(exercise: Exercise, userId: String): Int = repo.addLike(exercise, userId)
    suspend fun removeLike(exercise: Exercise, userId: String): Int = repo.removeLike(exercise, userId)
}