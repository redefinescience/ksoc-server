package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.domain.remote.jsonHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun remoteModule() = module {
    factory(named("json")) { jsonHttpClient() }
}
