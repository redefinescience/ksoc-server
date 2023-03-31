package com.kotlineering.ksoc.server.domain.service

import com.kotlineering.ksoc.server.domain.remote.LoginRemoteApi
import com.kotlineering.ksoc.server.domain.repository.UserRepository
import com.kotlineering.ksoc.server.web.controllers.AuthInfo
import java.time.Instant

class LoginService(
    private val loginRemoteApi: LoginRemoteApi,
    private val userRepository: UserRepository
) {
    suspend fun loginWithOidcCode(
        type: String, code: String
    ) = when (type) {
        "microsoft" -> loginRemoteApi.idTokenFromMicrosoft(code)
        else -> null
    }?.let { idToken ->
        val userId = userRepository.getOrCreateUserId(
            idToken.iss,
            idToken.sub
        )

        ServiceResult.Success(
            AuthInfo(
                userId.toString(),
                "1234",
                Instant.now().plusSeconds(60),
                "4321",
                Instant.now().plusSeconds(300),
                userRepository.getUserInfo(userId)
            )
        )
    } ?: ServiceResult.Failure
}
