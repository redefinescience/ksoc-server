package com.kotlineering.ksoc.server.web.controllers

import com.kotlineering.ksoc.server.domain.repository.UserInfo
import com.kotlineering.ksoc.server.domain.service.ServiceResult
import com.kotlineering.ksoc.server.domain.service.AuthService
import com.kotlineering.ksoc.server.util.InstantSerializer
import com.kotlineering.ksoc.server.util.JwtProvider
import com.kotlineering.ksoc.server.util.checkUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

    suspend fun updateUserProfile(
        call: ApplicationCall
    ) = call.receive<UserInfo>().let { body ->
        authService.takeIf {
            call.authentication.checkUserId(body.id.toString())
        }?.let {
            authService.updateUserProfile(body).let { res ->
                when (res) {
                    is ServiceResult.Success -> call.respond(res.data)
                    is ServiceResult.Failure -> call.respond(500) // TODO: beef this up
                }
            }
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }

    @Serializable
    data class TokenRequest(
        val bearer: String,
        val refresh: String
    )

    suspend fun refresh(
        call: ApplicationCall
    ) = call.receive<TokenRequest>().let { body ->
        try { JwtProvider.verifier.verify(body.bearer) }
        catch(t: Throwable) { null }?.let { decodedBearer ->
            try { JwtProvider.verifier.verify(body.refresh) }
            catch(t: Throwable) { null }?.let { decodedRefresh ->
                authService.refresh(
                    decodedBearer,
                    decodedRefresh
                )?.also { res ->
                    when (res) {
                        is ServiceResult.Success -> call.respond(res.data)
                        is ServiceResult.Failure -> call.respond(500) // TODO: beef this up
                    }
                }
            }
        }
    } ?: call.respond(HttpStatusCode.Unauthorized)
}
