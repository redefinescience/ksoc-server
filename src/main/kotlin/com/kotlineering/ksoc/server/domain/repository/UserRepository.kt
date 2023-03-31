package com.kotlineering.ksoc.server.domain.repository

import com.kotlineering.ksoc.server.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

data class User(
    val id: UUID,
    val oidIss: String,
    val oidSub: String
)

@Serializable
data class UserInfo(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String,
    val displayName: String,
    val image: String?
) {
    companion object {
        fun fromResultRow(resultRow: ResultRow) = UserInfo(
            resultRow[UsersInfo.id],
            resultRow[UsersInfo.email],
            resultRow[UsersInfo.displayName],
            resultRow[UsersInfo.image]
        )
    }
}

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
    val image = varchar("image", 256).nullable()

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

class UserRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(UsersInfo)
        }
    }

    fun getOrCreateUserId(
        iss: String, sub: String
    ): UUID = transaction {
        Users.select {
            Users.oidIss eq iss
            Users.oidSub eq sub
        }.map {
            it[Users.id]
        }.firstOrNull() ?: let {
            val uuid = UUID.randomUUID()
            Users.insert { row ->
                row[id] = uuid
                row[oidIss] = iss
                row[oidSub] = sub
            }
            return@let uuid
        }
    }

    fun getUserInfo(
        userId: UUID
    ): UserInfo? = transaction {
        UsersInfo.select {
            UsersInfo.id eq userId
        }.firstOrNull()?.let {
            UserInfo.fromResultRow(it)
        }
    }

    fun upsertUserInfo(
        info: UserInfo
    ) = transaction {
        UsersInfo.select {
            UsersInfo.id eq info.id
        }.firstOrNull()?.let {
            UsersInfo.update({
                UsersInfo.id eq info.id
            }) { row ->
                row[email] = info.email
                row[displayName] = info.displayName
                row[image] = info.image
            }
        } ?: let {
            UsersInfo.insert { row ->
                row[id] = info.id
                row[email] = info.email
                row[displayName] = info.displayName
                row[image] = info.image
            }
        }
        UsersInfo.select {
            UsersInfo.id eq info.id
        }.firstOrNull()?.let { row ->
            UserInfo.fromResultRow(row)
        }
    }
}
