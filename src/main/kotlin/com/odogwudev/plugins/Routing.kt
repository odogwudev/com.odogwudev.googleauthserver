package com.odogwudev.plugins

import com.odogwudev.routes.rootRoute
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        rootRoute()
    }
}
