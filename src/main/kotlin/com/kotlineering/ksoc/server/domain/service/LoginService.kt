package com.kotlineering.ksoc.server.domain.service

import com.kotlineering.ksoc.server.domain.remote.LoginRemoteApi

class LoginService(
    private val loginRemoteApi: LoginRemoteApi
) {
    suspend fun loginWithOidcCode(
        type: String, code: String
    ): Any? = when (type) {
        "microsoft" -> loginRemoteApi.idTokenFromMicrosoft(code)
        else -> null
    }?.let { idToken ->
        // TODO: finish me!
        // Check if in db, create user if not
        println(idToken)
        Any()
    }
}
