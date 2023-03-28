package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.web.controllers.LoginController
import org.koin.dsl.module

fun loginModule() = module {
    single { LoginController() }
}
