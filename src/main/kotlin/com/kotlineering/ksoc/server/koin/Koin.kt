package com.kotlineering.ksoc.server.koin

import com.kotlineering.ksoc.server.koin.modules.loginModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        loginModule()
    )
}

