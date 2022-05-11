package com.odogwudev.routes

import com.odogwudev.domain.model.ApiResponse
import com.odogwudev.domain.model.Endpoints
import com.odogwudev.domain.model.UserSession
import com.odogwudev.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import java.util.StringJoiner

fun Routing.deleteUserInfo(
    app: Application, userDataSource: UserDataSource
) {
    authenticate() {
        delete(Endpoints.DeleteUserInfo.path) {
            val userSession = call.principal<UserSession>()
            if (userSession == null) {
                app.log.info("Invalid Session")
                call.respondRedirect(Endpoints.Unauthorized.path)
            } else {
                //delete session along with the user
                call.sessions.clear<UserSession>()
                try {
                    deleteUserInfoFromDb(
                        app = app, userId = userSession.id, userDataSource = userDataSource
                    )

                } catch (e: Exception) {
                    app.log.info(" Deleting User Error due to: ${e.message}")
                    call.respondRedirect(Endpoints.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.deleteUserInfoFromDb(
    app: Application, userId: String, userDataSource: UserDataSource
) {
    val result = userDataSource.deleteUser(userId = userId)
    if (result) {
        app.log.info("User Sucesfully Deleted")
        call.respond(
            message = (ApiResponse(success = true)), status = HttpStatusCode.OK
        )
    } else {
        app.log.info("User data couldn't be deleted")
        call.respond(
            message = (ApiResponse(success = false)), status = HttpStatusCode.BadRequest
        )
    }
}