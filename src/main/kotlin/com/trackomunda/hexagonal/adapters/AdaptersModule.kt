package com.trackomunda.hexagonal.adapters

import com.trackomunda.hexagonal.ports.GameRepository
import com.trackomunda.hexagonal.ports.GangImporter
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val adaptersModule = module {
    singleOf(::InMemoryGameRepository) { bind<GameRepository>() }
    singleOf(::YakTribeGangImporter) { bind<GangImporter>() }
}