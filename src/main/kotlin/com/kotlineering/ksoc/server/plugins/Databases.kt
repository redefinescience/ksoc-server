package com.kotlineering.ksoc.server.plugins

import org.jetbrains.exposed.sql.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

/*
// In file
Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
// In memory
Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
// For both: set SQLite compatible isolation level, see
// https://github.com/JetBrains/Exposed/wiki/FAQ
TransactionManager.manager.defaultIsolationLevel =
    Connection.TRANSACTION_SERIALIZABLE
    // or Connection.TRANSACTION_READ_UNCOMMITTED
//Gradle
implementation("org.xerial:sqlite-jdbc:3.30.1")
 */
fun Application.configureDatabases() {
    val database = Database.connect("jdbc:sqlite:/sqlite/data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE

    val userService = UserService(database)
    routing {
        // Create user
        post("/users") {
            val user = call.receive<User>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<User>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
