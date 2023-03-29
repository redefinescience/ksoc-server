package com.kotlineering.ksoc.server.koin

import com.kotlineering.ksoc.server.koin.modules.controllerModule
import com.kotlineering.ksoc.server.koin.modules.remoteModule
import com.kotlineering.ksoc.server.koin.modules.repositoryModule
import com.kotlineering.ksoc.server.koin.modules.serviceModule
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        repositoryModule(),
        serviceModule(),
        controllerModule(),
        remoteModule()
    )
}
