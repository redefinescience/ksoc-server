package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.web.controllers.AuthController
import org.koin.dsl.module

fun controllerModule() = module {
    single { AuthController(get()) }
}
