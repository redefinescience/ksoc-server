package com.kotlineering.ksoc.server.domain.service

import com.kotlineering.ksoc.server.domain.remote.LoginRemoteApi
import com.kotlineering.ksoc.server.domain.repository.UserInfo
import com.kotlineering.ksoc.server.domain.repository.UserRepository
import com.kotlineering.ksoc.server.util.JwtProvider
import com.kotlineering.ksoc.server.web.controllers.AuthInfo

class AuthService(
    private val loginRemoteApi: LoginRemoteApi,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {
    fun updateUserProfile(info: UserInfo) =
        userRepository.upsertUserInfo(info)?.let {
            ServiceResult.Success(info)
        } ?: ServiceResult.Failure

    suspend fun loginWithOidcCode(
        type: String, code: String
    ): ServiceResult<AuthInfo> = when (type) {
        "microsoft" -> loginRemoteApi.idTokenFromMicrosoft(code)
        else -> null
    }?.let { idToken ->
        userRepository.getOrCreateUserId(
            idToken.iss,
            idToken.sub
        ).let { userId ->
            jwtProvider.createJwt(
                userId.toString()
            )?.copy(
                userInfo = userRepository.getUserInfo(userId)
            )?.let {
                ServiceResult.Success(it)
            }
        }
    } ?: ServiceResult.Failure
}
