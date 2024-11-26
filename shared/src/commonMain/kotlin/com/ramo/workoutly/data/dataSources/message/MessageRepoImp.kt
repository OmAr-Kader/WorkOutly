package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.model.MessageAWS
import com.ramo.workoutly.data.util.BaseRepoImp
import com.ramo.workoutly.data.util.QueryListAWS
import com.ramo.workoutly.data.util.SocketAWS
import com.ramo.workoutly.global.base.AWS_DELETE
import com.ramo.workoutly.global.base.BASE_URL
import com.ramo.workoutly.global.base.MESSAGE_BASE
import com.ramo.workoutly.global.base.MESSAGE_BY_ID
import com.ramo.workoutly.global.base.MESSAGE_GET_ALL_BY_SESSION
import com.ramo.workoutly.global.base.URL_MESSAGE_SOCKET
import com.ramo.workoutly.global.util.logger
import io.ktor.client.HttpClient
import io.ktor.client.request.headers

class MessageRepoImp(client: HttpClient, clientSocket: HttpClient) : BaseRepoImp(client, clientSocket), MessageRepo {

    override suspend fun addMessage(message: Message): Message? {
        logger("---", message.jsonStr())
        return postCreate(MESSAGE_BASE, message)
    }

    override suspend fun fetchMessageSession(session: String): List<Message> {
        return get<QueryListAWS<Message>>(MESSAGE_GET_ALL_BY_SESSION + session)?.list ?: emptyList()
    }

    override suspend fun fetchNewMessages(invoke: (SocketAWS<MessageAWS>) -> Unit) {
        startObserving<MessageAWS>(URL_MESSAGE_SOCKET, {
            headers {
                append("Content-Type", "application/json")
            }
        }, invoke)
    }

    override suspend fun deleteMessage(id: String): Int {
        return delete(MESSAGE_BY_ID + id)
    }
}