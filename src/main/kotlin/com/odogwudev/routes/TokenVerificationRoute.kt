package com.odogwudev.routes

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.odogwudev.domain.model.ApiRequest
import com.odogwudev.domain.model.Endpoints
import com.odogwudev.domain.model.User
import com.odogwudev.domain.model.UserSession
import com.odogwudev.domain.repository.UserDataSource
import com.odogwudev.util.Constants.AUDIENCE
import com.odogwudev.util.Constants.ISSUER
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*


fun Route.tokenVerificationRoute(app: Application, userDataSource: UserDataSource) {
    post(Endpoints.TokenVerification.path) {
        val request = call.receive<ApiRequest>()
        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleToken(tokenId = request.tokenId)
            if (result != null) {
                saveUserToDb(
                    app = app, result = result, userDataSource = userDataSource
                )
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

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDb(
    app: Application,
    result: GoogleIdToken,
    userDataSource: UserDataSource
) {
    val sub = result.payload["sub"].toString() // subclaim which contains unique identifier for google account
    val name = result.payload["name"].toString()
    val emailAddress = result.payload["email"].toString()
    val profilePhoto = result.payload["picture"].toString()
//    app.log.info("TOKEN VERIFICATION SUCCESSFUL: $name,$emailAddress")

    val user = User(
        id = sub,
        name = name,
        emailAddress = emailAddress,
        profilePhoto = profilePhoto
    )

    val response = userDataSource.saveUserInfo(user = user)

    if (response) {
        app.log.info("Save User Complete")
        call.sessions.set(UserSession(id = sub, name = name))//dynamically save all google info
        call.respondRedirect(Endpoints.Authorized.path)
    } else {
        app.log.info("Save Failed")
        call.respondRedirect(Endpoints.Unauthorized.path)
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