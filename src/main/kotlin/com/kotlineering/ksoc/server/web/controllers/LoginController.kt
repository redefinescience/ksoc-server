package com.kotlineering.ksoc.server.web.controllers

import com.kotlineering.ksoc.server.domain.service.LoginService
import com.kotlineering.ksoc.server.domain.service.UserService
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginFrom(
    val type: String,
    val code: String
)


class LoginController(
    private val loginService: LoginService,
    private val userService: UserService
) {

    suspend fun login(call: ApplicationCall) = call.receive<LoginFrom>().let { param ->
        when(param.type) {
            "microsoft" -> loginService.idTokenFromMicrosoft(param.code)
            else -> null
        }
    }?.let { idToken ->
        // Check if in db, create user if not
    }
}