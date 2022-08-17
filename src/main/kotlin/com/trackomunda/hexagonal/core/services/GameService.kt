package com.trackomunda.hexagonal.core.services

import com.trackomunda.hexagonal.core.ExecuteFighterActionCommand
import com.trackomunda.hexagonal.core.domain.game.Game
import com.trackomunda.hexagonal.core.domain.game.GameGang
import com.trackomunda.hexagonal.ports.GameRepository
import com.trackomunda.hexagonal.ports.GangImporter

class GameService(val gameRepository: GameRepository, val gangImporter: GangImporter) {

    fun run(command: ExecuteFighterActionCommand) {
        gameRepository.findGame(command.gameId)?.executeFighterAction(command.fighterId, command.fighterAction)?.also {
            gameRepository.update(it)
        }
    }

    fun createANewGame(nameOfGame: String): Game {
        return gameRepository.createGame(nameOfGame).also {
            gameRepository.update(it)
        }
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

    fun addGang1ToGame(gameId: String, gangUrl: String): Game? {
        return gameRepository.findGame(gameId)?.let { game ->
            game.addOrReplaceGang1(GameGang(gang = gangImporter.importGang(gangUrl)))
            gameRepository.update(game)
        }
    }

    fun addGang2ToGame(gameId: String, gangUrl: String): Game? {
        return gameRepository.findGame(gameId)?.let { game ->
            game.addOrReplaceGang2(GameGang(gang = gangImporter.importGang(gangUrl)))
            gameRepository.update(game)
        }
    }
}