package com.trackomunda.hexagonal.core

import com.trackomunda.hexagonal.core.services.GameService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::GameService)
}