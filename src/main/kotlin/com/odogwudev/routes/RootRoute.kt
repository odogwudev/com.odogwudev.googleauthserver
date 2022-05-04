package com.odogwudev.routes

import com.odogwudev.domain.model.Endpoints
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.rootRoute() {

    get(Endpoints.Root.path) {
        call.respondText("Welcome to the bullionvan Server")
    }

}
