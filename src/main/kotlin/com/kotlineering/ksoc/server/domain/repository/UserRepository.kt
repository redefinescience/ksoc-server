package com.kotlineering.ksoc.server.domain.repository

import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.*

data class User(
    val id: UUID,
    val oidIss: String,
    val oidSub: String
)

data class UserInfo(
    val id: UUID,
    val email: String,
    val displayName: String,
    val image: String
)

internal object Users : Table() {
    val id = uuid("id")
    val oidIss = varchar("oid_iss", 128)
    val oidSub = varchar("oid_sub", 128)

    override val primaryKey = PrimaryKey(id)

    init {
        index(true, oidIss, oidSub)
    }
}

internal object UsersInfo : Table() {
    val id = uuid("id")
    val email = varchar("email", 128)
    val displayName = varchar("display_name", 128)
    val image = varchar("image", 256)

    override val primaryKey = PrimaryKey(id)

    init {
        ForeignKeyConstraint(
            id, Users.id,
            ReferenceOption.CASCADE,
            ReferenceOption.CASCADE,
            null
        )
    }
}
