package com.ramo.workoutly.data.dataSources.exercise

import com.ramo.workoutly.data.model.Exercise
import com.ramo.workoutly.data.util.BaseRepoImp
import com.ramo.workoutly.data.util.QueryListAzure
import com.ramo.workoutly.global.base.BASE_URL_EXERCISE
import com.ramo.workoutly.global.base.DB_COLLECTION_EXERCISE
import io.ktor.client.HttpClient
import io.ktor.http.headers
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class ExerciseRepoImp(client: HttpClient, clientSocket: HttpClient) : BaseRepoImp(client, clientSocket), ExerciseRepo {

    override suspend fun createExercise(exercise: Exercise): Exercise? {
        return postCreate(BASE_URL_EXERCISE, exercise) {
            append("x-ms-documentdb-partitionkey", exercise.id)
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun getExercise(id: String): Exercise? {
        mutableMapOf<String, JsonElement>().apply {
            this["query"] = JsonPrimitive("SELECT * FROM $DB_COLLECTION_EXERCISE c")
        }.let(::JsonObject).let { body ->

        }
         // Or
        """
            {
                "query": "SELECT * FROM $DB_COLLECTION_EXERCISE c
            }
        """.trimIndent().let { body ->
            return get(BASE_URL_EXERCISE + id) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                headers {
                    append("x-ms-documentdb-partitionkey", id)
                }
                this@get.body = body
            }
        }
    }

    override suspend fun fetchExercises(): List<Exercise> {
        """
            {
                "query": "SELECT c.id, c.title, c.video_uri, c.date, c.length, c.views, c.liker FROM $DB_COLLECTION_EXERCISE c
            }
        """.trimIndent().let { body ->
            return get<QueryListAzure<Exercise>>(BASE_URL_EXERCISE) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this@get.body = body
            }?.list ?: emptyList()
        }
    }

    override suspend fun fetchExercisesAllData(): List<Exercise> {
        """
            {
                "query": "SELECT * FROM $DB_COLLECTION_EXERCISE c
            }
        """.trimIndent().let { body ->
            return get<QueryListAzure<Exercise>>(BASE_URL_EXERCISE) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this@get.body = body
            }?.list ?: emptyList()
        }
    }

    override suspend fun incrementViewCount(exercise: Exercise): Int {
        """
            {
                "operations": [
                    {
                      "op": "add",
                      "path": "/views",
                      "value": 1
                    }
                  ]
            }
        """.trimIndent().let { body ->
            return patchLess(BASE_URL_EXERCISE + exercise.id) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this.body = body
            }
        }
    }

    override suspend fun addLike(exercise: Exercise, userId: String): Int {
        """
            {
                "operations": [
                    {
                      "op": "add",
                      "path": "/liker/-",
                      "value": $userId
                    }
                  ]
            }
        """.trimIndent().let { body ->
            return patchLess(BASE_URL_EXERCISE + exercise.id) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this.body = body
            }
        }
    }

    override suspend fun removeLike(exercise: Exercise, userId: String): Int {
        """
            {
                "condition":"from c where c.liker ='$userId' "
                "operations": [
                    {
                      "op": "remove",
                      "path": "/liker/-",
                    }
                  ]
            }
        """.trimIndent().let { body ->
            return patchLess("exercise/${exercise.id}") {
                this.body = body
            }
        }
    }

    override suspend fun deleteExercise(id: String): Int {
        return delete(BASE_URL_EXERCISE + id) {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }
}