package com.kotlineering.ksoc.server.web.controllers

import com.kotlineering.ksoc.server.domain.repository.UserInfo
import com.kotlineering.ksoc.server.domain.service.ServiceResult
import com.kotlineering.ksoc.server.domain.service.LoginService
import com.kotlineering.ksoc.server.util.InstantSerializer
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class LoginRequest(
    val type: String,
    val code: String
)

@Serializable
data class AuthInfo(
    val userId: String,
    val bearer: String,
    @Serializable(with = InstantSerializer::class)
    val bearerExpiry: Instant,
    val refresh: String,
    @Serializable(with = InstantSerializer::class)
    val refreshExpiry: Instant,
    val userInfo: UserInfo?
)

class LoginController(
    private val loginService: LoginService,
) {
    suspend fun login(call: ApplicationCall) = call.receive<LoginRequest>().let { req ->
        loginService.loginWithOidcCode(req.type, req.code).let { res ->
            when (res) {
                is ServiceResult.Success -> call.respond(res.auth)
                is ServiceResult.Failure -> call.respond(500) // TODO: beef this up
            }
        }
    }
}
