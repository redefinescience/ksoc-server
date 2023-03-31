package com.kotlineering.ksoc.server.koin

import com.kotlineering.ksoc.server.koin.modules.*
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        repositoryModule(),
        serviceModule(),
        controllerModule(),
        remoteModule(),
        utilModule()
    )
}
