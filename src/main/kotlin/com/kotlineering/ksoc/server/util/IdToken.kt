package com.kotlineering.ksoc.server.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.lang.Exception
import java.util.*

data class IdToken(
    val iss: String,
    val sub: String,
    val email: String?
) {
    companion object {
        fun decode(token: String): IdToken? = token.split('.').map {
            try {
                Json.Default.parseToJsonElement(
                    Base64.getDecoder().decode(it).decodeToString()
                ).jsonObject
            } catch (_: Exception) {
                JsonObject(emptyMap())
            }
        }.find {
            it.containsKey("iss") && it.containsKey("sub")
        }?.let {
            IdToken(
                it["iss"].toString(),
                it["sub"].toString(),
                it["email"]?.toString()
            )
        }
    }
}
