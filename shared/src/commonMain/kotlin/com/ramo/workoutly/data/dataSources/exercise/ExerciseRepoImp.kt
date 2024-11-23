package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.model.ExerciseLikes
import com.ramo.workoutly.data.model.ExerciseViews
import com.ramo.workoutly.data.util.BaseRepoImp
import com.ramo.workoutly.data.util.QueryListAzure
import com.ramo.workoutly.global.base.BASE_URL_EXERCISE
import com.ramo.workoutly.global.base.DB_COLLECTION_EXERCISE
import io.ktor.client.HttpClient

class ExerciseRepoImp(client: HttpClient) : BaseRepoImp(client), ExerciseRepo {

    override suspend fun createExercise(exercise: Exercise): Exercise? {
        return post(BASE_URL_EXERCISE, exercise) {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun getExercise(id: String): Exercise? {
        """
            {
                "query": "SELECT c.id, c.title, c.videoUri, c.date, c.length FROM $DB_COLLECTION_EXERCISE c
            }
        """.trimIndent().let { body ->
            return getLess("$BASE_URL_EXERCISE$id") {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this@getLess.body = body
            }
        }
    }

    override suspend fun fetchExercises(): List<Exercise> {
        """
            {
                "query": "SELECT c.id, c.title, c.videoUri, c.date, c.length FROM $DB_COLLECTION_EXERCISE c
            }
        """.trimIndent().let { body ->
            return getLess<QueryListAzure<Exercise>>(BASE_URL_EXERCISE) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this@getLess.body = body
            }?.list ?: emptyList()
        }
    }

    override suspend fun updateExercise(exercise: Exercise): Exercise? {
        return  put("$exercise/${exercise.id}", exercise) {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun deleteExercise(id: String): Int {
        return delete("exercise/$id") {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    override suspend fun getExerciseViews(exerciseId: String): ExerciseViews? {
        return get("exercise/$exerciseId/views") {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun getExerciseLikes(exerciseId: String): ExerciseLikes? {
        return get("exercise/$exerciseId/views") {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun incrementViewCount(exerciseId: String): ExerciseViews? {
        return post("exercise/$exerciseId/views") {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun addLike(exerciseId: String, likerId: String): ExerciseLikes? {
        return postLess("exercise/$exerciseId/likes") {
            //header(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
            body = ("""{ "likerId": $likerId }""")
        }
    }
}