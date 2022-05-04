package com.odogwudev.plugins

import com.odogwudev.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.io.File

fun Application.configureSession() {
    val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
    val secretAuthKey = hex("02030405060708090a0b0c")
    install(Sessions) {// two ways to transport session cookie or header here its cookie been used
        cookie<UserSession>(
            name = "USER SESSION",
            storage = directorySessionStorage(File(".sessions"))
        ) {
            // cookie.secure = true when i move from local host
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretAuthKey))
        }
    }
}