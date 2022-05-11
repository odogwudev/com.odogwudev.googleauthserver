package com.odogwudev.routes

import com.odogwudev.domain.model.ApiResponse
import com.odogwudev.domain.model.Endpoints
import com.odogwudev.domain.model.UserSession
import com.odogwudev.domain.model.UserUpdate
import com.odogwudev.domain.repository.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.updateUserRoute(
    app: Application,
    userDataSource: UserDataSource,
) {
    authenticate("auth-session") {
        put(Endpoints.UpdateUserInfo.path) {
            val userSession = call.principal<UserSession>()
            val userUpdate = call.receive<UserUpdate>()
            if (userSession == null) {
                app.log.info("Invalid Session")
                call.respondRedirect(Endpoints.Unauthorized.path)
            } else {
                try {
                    updateUserInfo(
                        app = app,
                        userId = userSession.id,
                        userUpdate = userUpdate,
                        userDataSource = userDataSource
                    )

                } catch (e: Exception) {
                    app.log.info(" Updating User failed due to: ${e.message}")
                    call.respondRedirect(Endpoints.Unauthorized.path)
                }
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.updateUserInfo(
    app: Application,
    userId: String,
    userUpdate: UserUpdate,
    userDataSource: UserDataSource
) {
    val response = userDataSource.updateUserInfo(
        userId = userId,
        firstName = userUpdate.firstName,
        lastName = userUpdate.lastName
    )
    if (response) {
        app.log.info("USer succesfuly update")
        call.respond(
            message = ApiResponse(
                success = true,
                message = "Successfully Updated"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        app.log.info("Error Updating User Record")
        call.respond(
            message = ApiResponse(success = false),
            status = HttpStatusCode.BadRequest
        )
    }
}