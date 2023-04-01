package com.kotlineering.ksoc.server.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.kotlineering.ksoc.server.web.controllers.AuthInfo
import java.time.Instant
import java.util.*

class JwtProvider {


    private fun createAccessToken(
        userId: String,
        match: String
    ): Pair<String, Instant>? = Instant.now().plusSeconds(
        36 * 60 * 60
    ).let { expiry ->
        JWT.create()
            .withIssuedAt(Date(Instant.now().toEpochMilli()))
            .withSubject("ksoc-authorization")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("match", match)
            .withExpiresAt(Date(expiry.toEpochMilli()))
            .sign(algorithm)?.let { token ->
                Pair(token, expiry)
            }
    }

    private fun createRefreshToken(
        userId: String,
        match: String
    ): Pair<String, Instant>? = Instant.now().plusSeconds(
        10 * 24 * 60 * 60
    ).let { expiry ->
        JWT.create()
            .withIssuedAt(Date(Instant.now().toEpochMilli()))
            .withSubject("ksoc-authorization-refresh")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withClaim("match", match)
            .withExpiresAt(Date(expiry.toEpochMilli()))
            .sign(algorithm)?.let { token ->
                Pair(token, expiry)
            }
    }

    fun createTokens(
        userId: String
    ): AuthInfo? = UUID.randomUUID().toString().let { match ->
        createAccessToken(userId, match)?.let { access ->
            createRefreshToken(userId, match)?.let { refresh ->
                AuthInfo(
                    userId,
                    access.first,
                    access.second,
                    refresh.first,
                    refresh.second,
                    null
                )
            }
        }
    }

    companion object {
        // TODO: Config object with get(), injected
        private const val validityInMs = 36_000_00 * 10 // 10 hours
        const val issuer = "ksoc-server"
        const val audience = "ksoc-audience"
        private const val secret = "ksoc-super-secret-secret-thing"

        private val algorithm = Algorithm.HMAC256(secret)

        val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .withAudience(audience)
            .build()
    }
}
