package com.kotlineering.ksoc.server

import com.kotlineering.ksoc.server.koin.initKoin
import io.ktor.server.application.*
import com.kotlineering.ksoc.server.plugins.*
import com.kotlineering.ksoc.server.web.login
import io.ktor.server.routing.*
import org.koin.core.Koin

private lateinit var koinInstance: Koin
fun getKoin(): Koin = koinInstance

fun main(args: Array<String>): Unit {
    koinInstance = initKoin().koin
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSerialization()

    routing {
        login(getKoin().get())
    }
}
