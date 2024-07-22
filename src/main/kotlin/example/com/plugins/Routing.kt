package example.com.plugins

import example.com.room.RoomController
import example.com.routes.chatSocket
import example.com.routes.deleteMessage
import example.com.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(roomController: RoomController) {
    routing {
        chatSocket(roomController)
        getAllMessages(roomController)
        deleteMessage(roomController)
    }
}
