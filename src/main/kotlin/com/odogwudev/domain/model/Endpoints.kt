package com.odogwudev.domain.model

sealed class Endpoints(val path: String) {
    object Root : Endpoints(path = "/")
    object TokenVerification : Endpoints(path = "/token_verification")
    object GetUserInfo : Endpoints(path = "/get_user_info")
    object DeleteUserInfo : Endpoints(path = "/delete_user_info")
    object UpdateUserInfo : Endpoints(path = "/update_user_info")
    object SignOut : Endpoints(path = "/sign_out")
    object Unauthorized : Endpoints(path = "/unauthorized")
    object Authorized : Endpoints(path = "/authorized")
}
