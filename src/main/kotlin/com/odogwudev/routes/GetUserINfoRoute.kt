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

fun Route.getUserInfoRoute(
    app: Application,
    userDataSource: UserDataSource
) {
    authenticate("auth-session") {
        get(Endpoints.GetUserInfo.path) {
            val userSession = call.principal<UserSession>()//this principal is to get user sessio object from thr client
            if (userSession == null) {
                app.log.info("Invalid Session")
                call.respondRedirect(Endpoints.Unauthorized.path)
            } else {
                try {
                    call.respond(
                        message = ApiResponse(
                            success = true,
                            user = userDataSource.getUserInfo(userId = userSession.id)
                        ),
                        status = HttpStatusCode.OK
                    )
                } catch (e: Exception) {
                    app.log.info("Error Fetching User Info")
                    call.respondRedirect(Endpoints.Unauthorized.path)
                }
            }
        }
    }
}