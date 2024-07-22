package example.com.data

import example.com.data.model.Message

interface MessageDataSource {
    suspend fun getAllMessages():List<Message>
    suspend fun InsertMessage(message: Message)
    suspend fun deleteMessage(id:String)
}