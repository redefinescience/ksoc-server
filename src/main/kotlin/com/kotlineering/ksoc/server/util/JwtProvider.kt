package com.kotlineering.ksoc.server.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.kotlineering.ksoc.server.web.controllers.AuthInfo
import java.time.Instant
import java.util.*

class JwtProvider {

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun createJwt(userId: String): AuthInfo? = Instant.now().plusSeconds(
        36 * 60 * 60
    ).let { expiry ->
        JWT.create()
            .withIssuedAt(Date(Instant.now().toEpochMilli()))
            .withSubject("ksoc-authorization")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(Date(expiry.toEpochMilli()))
            .sign(algorithm)?.let { token ->
                AuthInfo(
                    userId,
                    token,
                    expiry,
                    "refreshtoken",
                    Instant.now().plusSeconds(
                        5 * 24 * 60 * 60
                    ),
                    null
                )
            }
    }


    companion object {
        // TODO: Config object with get(), injected
        private const val validityInMs = 36_000_00 * 10 // 10 hours
        const val issuer = "ksoc-server"
        const val audience = "ksoc-audience"
        const val secret = "ksoc-super-secret-secret-thing"
    }
}
