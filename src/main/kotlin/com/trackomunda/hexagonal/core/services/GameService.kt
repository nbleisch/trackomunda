package com.trackomunda.hexagonal.core.services

import com.trackomunda.hexagonal.core.domain.Game
import com.trackomunda.hexagonal.ports.GameRepository
import com.trackomunda.hexagonal.ports.GangImporter

class GameService(val gameRepository: GameRepository, val gangImporter: GangImporter) {

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

    fun addGangToGame(gameId: String, gangUrl: String): Game? {
        return gameRepository.findGame(gameId)?.let { game ->
            game.addGang(gangImporter.importGang(gangUrl))
            gameRepository.update(game)
        }
    }
}