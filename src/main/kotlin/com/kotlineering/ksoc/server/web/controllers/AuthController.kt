package com.kotlineering.ksoc.server.web.controllers

import com.kotlineering.ksoc.server.domain.repository.UserInfo
import com.kotlineering.ksoc.server.domain.service.ServiceResult
import com.kotlineering.ksoc.server.domain.service.AuthService
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

class AuthController(
    private val authService: AuthService,
) {
    suspend fun authorizeUser(call: ApplicationCall) = call.receive<LoginRequest>().let { req ->
        authService.loginWithOidcCode(req.type, req.code).let { res ->
            when (res) {
                is ServiceResult.Success -> call.respond(res.data)
                is ServiceResult.Failure -> call.respond(500) // TODO: beef this up
            }
        }
    }

    // TODO: Protect this route (in router)
    // TODO: Validate user is updating their own user
    suspend fun updateUserProfile(call: ApplicationCall) = authService.updateUserProfile(call.receive()).let { res ->
        when (res) {
            is ServiceResult.Success -> call.respond(res.data)
            is ServiceResult.Failure -> call.respond(500) // TODO: beef this up
        }
    }
}
