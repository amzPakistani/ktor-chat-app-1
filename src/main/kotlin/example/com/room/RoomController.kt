package example.com.room

import example.com.data.MessageDataSource
import example.com.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(private val messageDataSource: MessageDataSource) {
    val members =ConcurrentHashMap<String,Member>()

    fun onJoin(username:String, sessionId:String, socketSession: WebSocketSession){
        if(members.contains(username)){
            throw MemberExistsError()
        }

        members[username] = Member(
            username = username,
            sessionId = sessionId,
            socketSession = socketSession
        )
    }

    suspend fun sendMessage(senderUsername:String,message:String){
        members.values.forEach{member ->
            val messageEntity = Message(
                username = senderUsername,
                timeStamp = System.currentTimeMillis(),
                message = message
            )
            messageDataSource.InsertMessage(messageEntity)
            val parsedMessage = Json.encodeToString(messageEntity)
            member.socketSession.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun deleteMessage(id: String) {
        messageDataSource.deleteMessage(id)
        broadcastDeletion(id)
    }

    private suspend fun broadcastDeletion(id: String) {
        val deletionMessage = Json.encodeToString(mapOf("action" to "delete", "id" to id))
        members.values.forEach { member ->
            member.socketSession.send(Frame.Text(deletionMessage))
        }
    }

    suspend fun editMessage(message: Message) {
        messageDataSource.editMessage(message)
        broadcastEdit(message)
    }

    private suspend fun broadcastEdit(message: Message) {
        val editedMessage = Json.encodeToString(message)
        members.values.forEach { member ->
            member.socketSession.send(Frame.Text(editedMessage))
        }
    }


    suspend fun getAllMessages():List<Message>{
        return messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String){
        members[username]?.socketSession?.close()
        members.remove(username)
    }
}