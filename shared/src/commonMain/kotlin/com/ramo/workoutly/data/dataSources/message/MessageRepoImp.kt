package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.util.BaseRepoImp
import com.ramo.workoutly.data.util.QueryListAzure
import com.ramo.workoutly.global.base.BASE_URL_MESSAGE
import com.ramo.workoutly.global.base.URL_MESSAGE_SOCKET
import io.ktor.client.HttpClient
import io.ktor.client.request.headers

class MessageRepoImp(client: HttpClient, clientSocket: HttpClient) : BaseRepoImp(client, clientSocket), MessageRepo {

    override suspend fun addMessage(message: Message): Message? {
        return postCreate(BASE_URL_MESSAGE, message) {
            append("x-ms-documentdb-partitionkey", message.id)
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }

    override suspend fun fetchMessageSession(session: String): List<Message> {
        """
            {
                "query": "SELECT * FROM $BASE_URL_MESSAGE c WHERE f.session = @session,
                "parameters": [  
                    {  
                      "name": "@session",  
                      "value": "$session"  
                    }
                  ]
            }
        """.trimIndent().let { body ->
            return get<QueryListAzure<Message>>(BASE_URL_MESSAGE) {
                //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
                this@get.body = body
            }?.list ?: emptyList()
        }
    }

    override suspend fun fetchNewMessages(invoke: (Message) -> Unit): () -> Unit {
        return startObserving(URL_MESSAGE_SOCKET, {
            headers {
                append("Accept", "application/json")
                append("Content-Type", "application/json")
            }
        }, invoke)
    }

    override suspend fun deleteMessage(id: String): Int {
        return delete(BASE_URL_MESSAGE + id) {
            //set(io.ktor.http.HttpHeaders.Authorization, authorizationHeader)
        }
    }
}