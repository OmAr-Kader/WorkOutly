package com.ramo.workoutly.data.dataSources.message

import com.ramo.workoutly.data.model.Message
import com.ramo.workoutly.global.base.AWS_DELETE
import com.ramo.workoutly.global.base.AWS_INSERT
import com.ramo.workoutly.global.base.AWS_MODIFY

class MessageBase(
    private val repo: MessageRepo
) {
    suspend fun addMessage(message: Message): Message? = repo.addMessage(message)
    suspend fun fetchMessageSession(session: String): List<Message> = repo.fetchMessageSession(session)
    suspend fun fetchNewMessages(inserted: (Message) -> Unit, changed: (Message) -> Unit, deleted: (String) -> Unit) {
        repo.fetchNewMessages {
            when (it.action) {
                AWS_INSERT -> inserted(Message(aws = it.data))
                AWS_MODIFY -> changed(Message(aws = it.data))
                AWS_DELETE -> deleted(it.data.id.value)
            }
        }
    }
    suspend fun deleteMessage(id: String): Int = repo.deleteMessage(id)
}