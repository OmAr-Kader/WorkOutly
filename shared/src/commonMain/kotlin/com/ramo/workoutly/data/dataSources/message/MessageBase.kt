package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message

class MessageBase(
    private val repo: MessageRepo
) {
    suspend fun addMessage(message: Message): Message? = repo.addMessage(message)
    suspend fun fetchMessageSession(session: String): List<Message> = repo.fetchMessageSession(session)
    suspend fun fetchNewMessages(invoke: (Message) -> Unit) = repo.fetchNewMessages(invoke)
    suspend fun deleteMessage(id: String): Int = repo.deleteMessage(id)
}