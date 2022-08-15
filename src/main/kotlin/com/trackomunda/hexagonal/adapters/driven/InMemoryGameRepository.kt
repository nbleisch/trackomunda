package com.trackomunda.hexagonal.adapters.driven

import com.trackomunda.hexagonal.core.domain.game.Game
import com.trackomunda.hexagonal.ports.GameRepository
import java.util.*

class InMemoryGameRepository : GameRepository {

    private val games: MutableMap<String, Game> = mutableMapOf()

    override fun createGame(gameName: String): Game {
        val newGame = Game(id = UUID.randomUUID().toString(), name = gameName)
        games[newGame.id] = newGame
        return newGame
    }

    override fun findGame(id: String) = games[id]?.copy()

    override fun update(game: Game): Game {
        games[game.id] = game
        return game
    }

    override fun findAllGames(): List<Game> {
        return games.values.toList()
    }

}