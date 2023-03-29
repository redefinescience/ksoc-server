package com.kotlineering.ksoc.server.domain.service

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    val displayName: String,
    val image: String
)

class UserService {

}
