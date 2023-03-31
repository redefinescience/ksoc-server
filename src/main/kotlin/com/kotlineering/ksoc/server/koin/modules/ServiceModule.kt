package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.domain.service.AuthService
import com.kotlineering.ksoc.server.domain.service.UserService
import org.koin.dsl.module

fun serviceModule() = module {
    single { AuthService(get(), get(), get()) }
    single { UserService() }
}
