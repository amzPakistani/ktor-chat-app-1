package example.com.data

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.data.model.Message
import kotlinx.coroutines.flow.toList
import org.bson.Document

class MongoMessageDataSource(db:MongoDatabase) : MessageDataSource{

    val messages = db.getCollection<Message>("messages")
    override suspend fun getAllMessages(): List<Message> {
        return messages.find().sort(Document("timeStamp",-1)).toList()
    }

    override suspend fun InsertMessage(message: Message) {
        messages.insertOne(message)
    }
}