package com.kotlineering.ksoc.server.koin.modules

import com.kotlineering.ksoc.server.util.JwtProvider
import org.koin.dsl.module

fun utilModule() = module {
    single { JwtProvider() }
}
