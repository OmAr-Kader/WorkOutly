package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message

interface MessageRepo {

    suspend fun addMessage(message: Message): Message?
    suspend fun fetchMessageSession(session: String): List<Message>
    suspend fun fetchNewMessages(invoke: (Message) -> Unit): () -> Unit
    suspend fun deleteMessage(id: String): Int
}