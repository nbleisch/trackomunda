package com.trackomunda.plugins

import com.trackomunda.hexagonal.adapters.adaptersModule
import com.trackomunda.hexagonal.core.coreModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            coreModule,
            adaptersModule
        )
    }
}