package example.com.routes

import example.com.room.RoomController
import example.com.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController){
    webSocket("chat-socket") {
        val session = call.sessions.get<ChatSession>()
        if(session==null){
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
        }
        try {
            if (session != null) {
                roomController.onJoin(username = session.username, sessionId = session.sessionId, socketSession = this )
                incoming.consumeEach { frame ->
                    if(frame is Frame.Text){
                        roomController.sendMessage(senderUsername = session.username, message = frame.readText())
                    }
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally { //its for the cleanup tasks
            if(session!=null){
                roomController.tryDisconnect(username = session.username)
            }
        }
    }
}

fun Route.deleteMessage(roomController: RoomController) {
    delete("/delete_message/{id}") {
        val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
        roomController.deleteMessage(id)
        call.respond(HttpStatusCode.OK, "Message deleted successfully")
    }
}

fun Route.getAllMessages(roomController: RoomController){
    get("messages"){
        call.respond(HttpStatusCode.OK, roomController.getAllMessages())
    }
}