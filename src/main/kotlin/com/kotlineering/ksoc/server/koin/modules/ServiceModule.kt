package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.domain.service.LoginService
import com.kotlineering.ksoc.server.domain.service.UserService
import org.koin.dsl.module

fun serviceModule() = module {
    single { LoginService(get(), get()) }
    single { UserService() }
}
