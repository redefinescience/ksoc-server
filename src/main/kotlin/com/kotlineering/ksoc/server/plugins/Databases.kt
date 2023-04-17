package com.kotlineering.ksoc.server.plugins

import org.jetbrains.exposed.sql.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

fun Application.configureDatabases() {
    val database = Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE
}
