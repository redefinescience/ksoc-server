package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.domain.repository.UserRepository
import org.koin.dsl.module

fun repositoryModule() = module {
    single { UserRepository() }
}
