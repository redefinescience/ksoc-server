package com.kotlineering.ksoc.server.web.controllers

import com.kotlineering.ksoc.server.util.IdToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// TODO: Move somehwere?
@Serializable
data class LoginFrom(
    val type: String,
    val code: String
)


// TODO: should move to LoginService
@Serializable
data class MSTokenResponse(
    val token_type: String,
    val scope: String,
    val expires_in: Int,
    val ext_expires_in: Int,
    val access_token: String,
    val id_token: String
)

class LoginController {
    // TODO: move to service



    suspend fun getIdTokenFromMicrosoft(code: String) {
        // TODO: Inject client singleton
        val submit = HttpClient(Apache) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }.submitForm(
            "https://login.microsoftonline.com/consumers/oauth2/v2.0/token",
            formParameters = Parameters.build {
                append("client_id", "9136b230-9139-446a-850d-9a72f7ed6a40")
                append("scope", "openid")
                append("code", code)
                append("redirect_uri", "http://localhost:5000/login/ms")
                append("grant_type", "authorization_code")
                append("client_secret", "9gs8Q~2dpMseI4V9NML6bCisb.k6d16H5KPk1aEJ")
            })
//        val text = submit.bodyAsText()
//        println(text)

        val asdf: MSTokenResponse = submit.body()
        val id = IdToken.decode(asdf.id_token)
        println(id)

    }

    suspend fun login(call: ApplicationCall) {
        call.receive<LoginFrom>().apply {
            when (type) {
                "microsoft" -> getIdTokenFromMicrosoft(code)
                else -> null
            }
        }
    }
}