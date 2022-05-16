package com.odogwudev.plugins

import com.odogwudev.di.koinModule
import io.ktor.server.application.*

fun Application.configureKoin() {
    install(Koin) {
        modules = arrayListOf(
            koinModule
        )
    }
}

//faced a lot of issue setting it for ktor2.0 finally got a work around