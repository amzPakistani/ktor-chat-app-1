package example.com.data

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.data.model.Message
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.types.ObjectId

class MongoMessageDataSource(db:MongoDatabase) : MessageDataSource{

    val messages = db.getCollection<Message>("messages")
    override suspend fun getAllMessages(): List<Message> {
        return messages.find().sort(Document("timeStamp",-1)).toList()
    }

    override suspend fun InsertMessage(message: Message) {
        messages.insertOne(message)
    }

    override suspend fun deleteMessage(id: String) {
        val filter = Document("id", id) // Use the custom 'id' field

        // Proceed with deletion
        val result = messages.deleteOne(filter)
        if (result.deletedCount == 0L) {
            println("No document found with id: $id")
        } else {
            println("Document with id: $id deleted successfully")
        }
    }
}