package example.com.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val username: String,
    val id: String,
    val message: String,
    val timeStamp: Long
) {
    fun toMessage(): Message {
        return Message(
            username = username,
            timeStamp = timeStamp,
            message = message,
            id = id
        )
    }
}
