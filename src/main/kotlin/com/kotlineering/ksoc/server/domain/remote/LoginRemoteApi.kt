package com.kotlineering.ksoc.server.domain.remote

import com.kotlineering.ksoc.server.getKoin
import com.kotlineering.ksoc.server.util.IdToken
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.koin.core.qualifier.named

class LoginRemoteApi {

    @Serializable
    data class TokenResponse(
        val id_token: String // This is all we care about
    )

    private fun getClient() = getKoin().get<HttpClient>(named("json"))

    suspend fun idTokenFromMicrosoft(code: String) = getClient().submitForm(
        url = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token",
        formParameters = Parameters.build {
            // TODO: Create class (with get() accessors) for some of these values, inject it
            append("client_id", "9136b230-9139-446a-850d-9a72f7ed6a40")
            append("scope", "openid")
            append("code", code)
            append("redirect_uri", "http://localhost:5000/login/ms")
            append("grant_type", "authorization_code")
            append("client_secret", "9gs8Q~2dpMseI4V9NML6bCisb.k6d16H5KPk1aEJ")
        }
    ).let { response ->
        if (response.status.isSuccess()) {
            IdToken.decode(response.body<TokenResponse>().id_token)
        } else {
            null
        }
    }
}
