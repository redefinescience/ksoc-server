package com.kotlineering.ksoc.server.web

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.kotlineering.ksoc.server.util.JwtProvider
import com.kotlineering.ksoc.server.util.UserPrincipal
import io.ktor.server.application.*

fun Application.configureSecurity() {
    authentication {
        jwt {
            verifier(JwtProvider.verifier)
            authSchemes("Bearer")
            validate { credential ->
                credential.payload.takeIf {
                    it.claims.contains("userId") && it.claims.contains("match")
                }?.let { payload ->
                    UserPrincipal(
                        payload.claims["userId"]!!.asString(),
                        payload.claims["match"]!!.asString()
                    )
                }
            }
        }
    }
}
