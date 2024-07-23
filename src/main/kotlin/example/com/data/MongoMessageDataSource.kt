package example.com.data

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.set
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.data.model.Message
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.toList
import org.bson.Document
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set

class MongoMessageDataSource(db:MongoDatabase) : MessageDataSource{

    val messages = db.getCollection<Message>("messages")
    override suspend fun getAllMessages(): List<Message> {
        return messages.find().sort(Document("timeStamp",-1)).toList()
    }

    override suspend fun InsertMessage(message: Message) {
        messages.insertOne(message)
    }

    override suspend fun deleteMessage(id: String) {
        val filter = Document("id", id)
        messages.deleteOne(filter)
    }

    override suspend fun editMessage(message: Message) {
        val filter = eq("id", message.id)
        val update = combine(
            set("message", message.message),
            set("edited", true),
            set("username", message.username),
            set("timeStamp", message.timeStamp)

        )
        val result = messages.updateOne(filter, update)
        if (result.matchedCount == 0L) {
            println("No document found with id: ${message.id}")
        } else if (result.modifiedCount > 0) {
            println("Document with id: ${message.id} edited successfully")
        } else {
            println("Document with id: ${message.id} was not edited")
        }
    }
}