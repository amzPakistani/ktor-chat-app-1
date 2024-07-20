package example.com

import com.mongodb.kotlin.client.coroutine.MongoClient
import example.com.data.MongoMessageDataSource
import example.com.plugins.*
import example.com.room.RoomController
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val db = MongoClient.create().getDatabase("message_db_1")
    val messageDataSource = MongoMessageDataSource(db)
    val roomController = RoomController(messageDataSource)

    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting(roomController)
}
