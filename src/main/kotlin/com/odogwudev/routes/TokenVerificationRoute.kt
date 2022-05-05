package com.odogwudev.routes

import com.odogwudev.domain.model.Endpoints
import com.odogwudev.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*



fun Route.tokenVerificationRoute() {
    post(Endpoints.TokenVerification.path) {
        call.sessions.set(UserSession(id = "134", name = "odogwudev"))
        call.respondRedirect(Endpoints.Authorized.path)
    }
}

// if you face error of java.lang.NoSuch method error byte buffer upgrade java version to 11