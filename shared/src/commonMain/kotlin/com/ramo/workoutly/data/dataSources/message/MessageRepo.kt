package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.data.model.MessageAWS
import com.ramo.workoutly.data.util.SocketAWS

interface MessageRepo {

    suspend fun addMessage(message: Message): Message?
    suspend fun fetchMessageSession(session: String): List<Message>
    suspend fun fetchNewMessages(invoke: (SocketAWS<MessageAWS>) -> Unit)
    suspend fun deleteMessage(id: String): Int
}