package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.util.BaseRepoImp
import com.ramo.workoutly.data.util.QueryListAWS
import com.ramo.workoutly.global.base.BASE_URL
import com.ramo.workoutly.global.base.MESSAGE_BASE
import com.ramo.workoutly.global.base.MESSAGE_BY_ID
import com.ramo.workoutly.global.base.MESSAGE_GET_ALL_BY_SESSION
import com.ramo.workoutly.global.base.URL_MESSAGE_SOCKET
import io.ktor.client.HttpClient
import io.ktor.client.request.headers

class MessageRepoImp(client: HttpClient, clientSocket: HttpClient) : BaseRepoImp(client, clientSocket), MessageRepo {

    override suspend fun addMessage(message: Message): Message? {
        return postCreate(BASE_URL + MESSAGE_BASE, message)
    }

    override suspend fun fetchMessageSession(session: String): List<Message> {
        return get<QueryListAWS<Message>>(BASE_URL + MESSAGE_GET_ALL_BY_SESSION + session)?.list ?: emptyList()
    }

    override suspend fun fetchNewMessages(invoke: (Message) -> Unit): () -> Unit {
        return startObserving(URL_MESSAGE_SOCKET, {
            headers {
                append("Content-Type", "application/json")
            }
        }, invoke)
    }

    override suspend fun deleteMessage(id: String): Int {
        return delete(BASE_URL + MESSAGE_BY_ID + id)
    }
}