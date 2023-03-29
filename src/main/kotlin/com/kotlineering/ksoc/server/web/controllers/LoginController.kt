package com.kotlineering.ksoc.server.web.controllers

import com.kotlineering.ksoc.server.domain.service.LoginService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginFrom(
    val type: String,
    val code: String
)


class LoginController(
    private val loginService: LoginService,
) {
    suspend fun login(call: ApplicationCall) = call.receive<LoginFrom>().let {
        loginService.loginWithOidcCode(it.type, it.code)?.let {
            call.respond(200)
        } ?: call.respond(400)
    }
}
