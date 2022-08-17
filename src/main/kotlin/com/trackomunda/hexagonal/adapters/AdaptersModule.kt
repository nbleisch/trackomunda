package com.trackomunda.hexagonal.adapters

import com.trackomunda.hexagonal.adapters.driven.InMemoryGameRepository
import com.trackomunda.hexagonal.ports.GameRepository
import com.trackomunda.hexagonal.ports.GangImporter
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val adaptersModule = module {
    singleOf(::InMemoryGameRepository) { bind<GameRepository>() }
    singleOf(::YakTribeGangImporter) { bind<GangImporter>() }
    single {
        HttpClient {
            expectSuccess = true
            followRedirects = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }
    }
}