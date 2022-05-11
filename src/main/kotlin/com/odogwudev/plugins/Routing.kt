package com.odogwudev.plugins

import com.odogwudev.domain.repository.UserDataSource
import com.odogwudev.routes.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import org.koin.java.KoinJavaComponent.inject

fun Application.configureRouting() {

    routing {
        val userDataSource: UserDataSource by inject(UserDataSource::class.java)//part of koin library
        rootRoute()
        tokenVerificationRoute(application, userDataSource)
        getUserInfoRoute(application, userDataSource)
        updateUserRoute(application, userDataSource)
        deleteUserInfo(application, userDataSource)
        authorizedRoute()
        unauthorizedRoute()

    }
}
