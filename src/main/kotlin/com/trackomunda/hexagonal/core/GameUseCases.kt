package com.trackomunda.hexagonal.core

import com.trackomunda.hexagonal.ports.GameRepository
import com.trackomunda.hexagonal.ports.GangImporter

class GameUseCases(val gameRepository: GameRepository, val gangImporter: GangImporter) {

    fun createANewGame(nameOfGame: String): Game {
        return gameRepository.createGame(nameOfGame)
    }

    fun updateGame(game: Game) {
        gameRepository.update(game)
    }

    fun findAllGames(): List<Game> {
        return gameRepository.findAllGames()
    }

    fun findGame(id: String): Game? {
        return gameRepository.findGame(id)
    }


}