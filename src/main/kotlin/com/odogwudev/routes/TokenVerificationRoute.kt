package com.odogwudev.routes

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.odogwudev.domain.model.ApiRequest
import com.odogwudev.domain.model.Endpoints
import com.odogwudev.domain.model.UserSession
import com.odogwudev.util.Constants.AUDIENCE
import com.odogwudev.util.Constants.ISSUER
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*


fun Route.tokenVerificationRoute(app: Application) {
    post(Endpoints.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleToken(tokenId = request.tokenId)
            if (result != null) {
                //    val sub = result.payload["sub"].toString()
                val name = result.payload["name"].toString()
                val emailAddress = result.payload["email"].toString()
                //    val profilePhoto = result.payload["photo"].toString()
                app.log.info("TOKEN VERIFICATION SUCCESSFUL: $name,$emailAddress")
                call.sessions.set(UserSession(id = "134", name = "odogwudev"))
                call.respondRedirect(Endpoints.Authorized.path)
            } else {
                app.log.info("TOKEN VERIFICATION UNSUCCESSFUL")
                call.respondRedirect(Endpoints.Unauthorized.path)
            }
        } else {
            app.log.info("NO TOKEN FOUND")
            call.respondRedirect(Endpoints.Unauthorized.path)
        }

    }
}

// if you face error of java.lang.NoSuch method error byte buffer upgrade java version to 11

fun verifyGoogleToken(tokenId: String): GoogleIdToken? {
    return try {
        val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()
        verifier.verify(tokenId)
    } catch (e: Exception) {
        null
    }

}

//return google if token is verified otherwise it would return null