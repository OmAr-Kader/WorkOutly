package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.util.BaseRepoImp
import com.ramo.workoutly.data.util.QueryListAWS
import com.ramo.workoutly.global.base.*
import io.ktor.client.HttpClient

class ExerciseRepoImp(client: HttpClient, clientSocket: HttpClient) : BaseRepoImp(client, clientSocket), ExerciseRepo {

    override suspend fun createExercise(exercise: Exercise): Exercise? {
        return postCreate(EXERCISE_BASE, exercise)
    }

    override suspend fun getExercise(id: String): Exercise? {
        return get(EXERCISE_BY_ID + id)
    }

    override suspend fun fetchExercises(): List<Exercise> {
        return get<QueryListAWS<Exercise>>(EXERCISE_GET_ALL_BY_COLUMNS + "id,title,video_url,date,length,views,liker,cato")?.list ?: emptyList()
    }

    override suspend fun fetchExercisesAllData(): List<Exercise> {
        return get<QueryListAWS<Exercise>>(EXERCISE_GET_ALL)?.list ?: emptyList()
    }

    override suspend fun incrementViewCount(exercise: Exercise): Int {
        return patchLess(EXERCISE_VIEW_INCREMENT + exercise.id)
    }

    override suspend fun addLike(exercise: Exercise, userId: String): Int {
        return patchLess(EXERCISE_ADD_LIKE + exercise.id)
    }

    override suspend fun removeLike(exercise: Exercise, userId: String): Int {
        return patchLess(EXERCISE_REMOVE_LIKE + exercise.id)
    }

    override suspend fun deleteExercise(id: String): Int {
        return delete(EXERCISE_BY_ID + id)
    }
}