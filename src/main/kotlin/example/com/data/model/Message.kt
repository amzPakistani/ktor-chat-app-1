package example.com.data.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Message(
    val username:String,
    val timeStamp:Long,
    val message: String,
    val id:String = ObjectId().toString()
)