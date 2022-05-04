package com.odogwudev.plugins

import com.odogwudev.domain.model.Endpoints
import com.odogwudev.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSession>(name = "authentication-server") {
            validate { session ->
                session
            }
            challenge {
                call.respondRedirect(Endpoints.Unauthorized.path)
            }
        }
    }
}