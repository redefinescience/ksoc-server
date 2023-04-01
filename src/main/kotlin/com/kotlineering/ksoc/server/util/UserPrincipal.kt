package com.kotlineering.ksoc.server.util

import io.ktor.server.auth.*
data class UserPrincipal(
    val userId: String,
    val match: String
): Principal

fun AuthenticationContext.checkUserId(
    userId: String
) = principal<UserPrincipal>()?.userId == userId
