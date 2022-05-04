package com.odogwudev.routes

import com.odogwudev.domain.model.Endpoints
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unauthorizedRoute() {
    get(Endpoints.Unauthorized.path) {
        call.respond(
            message = "Please obtain authorization...",
            status = HttpStatusCode.Unauthorized
        )
    }
}